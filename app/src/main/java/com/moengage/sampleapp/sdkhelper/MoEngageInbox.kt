package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxMessage

/** Notification inbox (cards). Reached through [MoEngageSDKHelper]. */
internal object MoEngageInbox {

    fun fetchMessages(context: Context, onResult: (List<InboxMessage>) -> Unit) = guarded {
        MoEInboxHelper.getInstance().fetchAllMessagesAsync(
            context,
        ) { inboxData -> onResult(inboxData?.inboxMessages.orEmpty()) }
    }

    fun fetchUnreadCount(context: Context, onResult: (Long) -> Unit) = guarded {
        MoEInboxHelper.getInstance().getUnClickedMessagesCountAsync(
            context,
        ) { unClickedCountData -> onResult(unClickedCountData?.count ?: 0L) }
    }

    /** Tapping an inbox row marks it read and reports the click. */
    fun markMessageRead(context: Context, message: InboxMessage) = guarded {
        MoEInboxHelper.getInstance().trackMessageClicked(context, message)
    }
}
