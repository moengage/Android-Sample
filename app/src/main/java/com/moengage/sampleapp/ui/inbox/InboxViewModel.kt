package com.moengage.sampleapp.ui.inbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.core.model.actions.NavigationAction
import com.moengage.sampleapp.domain.model.InboxAccent
import com.moengage.sampleapp.domain.model.InboxGroup
import com.moengage.sampleapp.domain.model.InboxMessageUi
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class InboxUiState(
    val messages: List<InboxMessageUi> = emptyList(),
    /** True once the cards/inbox API has answered, so the UI can say where the list came from. */
    val fromSdk: Boolean = false,
)

/**
 * Screen 11's state. Rows come from `MoEInboxHelper.fetchAllMessagesAsync`, so the list is empty
 * until a campaign has actually landed on the device.
 */
class InboxViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(InboxUiState())
    val state: StateFlow<InboxUiState> = _state.asStateFlow()

    /** SDK messages keyed by our UI id, so a tap can be reported back to the SDK. */
    private var sdkMessages: Map<String, InboxMessage> = emptyMap()

    private val context get() = getApplication<Application>().applicationContext

    fun refresh() {
        MoEngageSDKHelper.fetchInboxMessages(context) { messages ->
            if (messages.isEmpty()) return@fetchInboxMessages
            sdkMessages = messages.associateBy { it.id.toString() }
            _state.value = InboxUiState(messages = messages.map { it.toUi() }, fromSdk = true)
        }
    }

    /** Returns the route to navigate to, if the message carries a deep link. */
    fun onMessageClicked(message: InboxMessageUi): String? {
        sdkMessages[message.id]?.let { MoEngageSDKHelper.markInboxMessageRead(context, it) }
        MoEngageSDKHelper.trackNotificationOpened(context, message.campaignId, message.deeplink)
        _state.update { state ->
            state.copy(
                messages = state.messages.map {
                    if (it.id == message.id) it.copy(read = true) else it
                },
            )
        }
        return message.deeplink
    }

    fun markAllRead() {
        sdkMessages.values.forEach { MoEngageSDKHelper.markInboxMessageRead(context, it) }
        _state.update { state -> state.copy(messages = state.messages.map { it.copy(read = true) }) }
    }

    private fun InboxMessage.toUi(): InboxMessageUi {
        val navigation = action.orEmpty().filterIsInstance<NavigationAction>().firstOrNull()
        return InboxMessageUi(
            id = id.toString(),
            title = textContent.title,
            body = textContent.message,
            timestamp = relativeTime(sentTime),
            group = if (isToday(sentTime)) InboxGroup.Today else InboxGroup.Earlier,
            read = isClicked,
            accent = if (textContent.title.contains("star", ignoreCase = true)) {
                InboxAccent.Star
            } else {
                InboxAccent.Brand
            },
            deeplink = navigation?.value,
            campaignId = campaignId,
        )
    }
}

private fun isToday(date: Date?): Boolean {
    if (date == null) return false
    val now = Calendar.getInstance()
    val then = Calendar.getInstance().apply { time = date }
    return now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
        now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)
}

private fun relativeTime(date: Date?): String {
    if (date == null) return ""
    val minutes = (System.currentTimeMillis() - date.time) / 60_000
    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes min ago"
        minutes < 60 * 24 -> "${minutes / 60} h ago"
        else -> java.text.SimpleDateFormat("EEE", Locale.getDefault()).format(date)
    }
}
