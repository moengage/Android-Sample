package com.moengage.sampleapp.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.InboxAccent
import com.moengage.sampleapp.domain.model.InboxGroup
import com.moengage.sampleapp.domain.model.InboxMessageUi
import com.moengage.sampleapp.ui.components.BrewAppBar
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.InlineLink
import com.moengage.sampleapp.ui.components.NotificationRow
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 11 — notification inbox, backed by the MoEngage cards/inbox API.
 *
 * Rows are grouped Today / Earlier; unread carries the 3px accent stripe, read rows drop to
 * 72% opacity. Tapping routes via the message's deep link and marks it read with
 * `trackMessageClicked`.
 */
@Composable
fun InboxScreen(
    messages: List<InboxMessageUi>,
    onBack: () -> Unit,
    onMessageClick: (InboxMessageUi) -> Unit,
    onMarkAllRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        BrewAppBar(
            title = "Notifications",
            onBack = onBack,
            trailing = { InlineLink("Mark all read", onMarkAllRead) },
        )
        if (messages.isEmpty()) {
            EmptyInbox()
            return@Column
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                top = Space.x16,
                bottom = Space.x28,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x10),
        ) {
            InboxGroup.entries.forEach { group ->
                val groupMessages = messages.filter { it.group == group }
                if (groupMessages.isEmpty()) return@forEach
                item(key = "header-${group.name}") {
                    Text(
                        group.header.uppercase(),
                        style = BrewType.label,
                        color = BrewColors.TextTertiary,
                        modifier = Modifier.padding(top = Space.x6, bottom = Space.x4),
                    )
                }
                items(groupMessages, key = { it.id }) { message ->
                    NotificationRow(message, onClick = { onMessageClick(message) })
                }
            }
        }
    }
}

/**
 * Shown until the cards/inbox API returns something. There is no seeded content behind this
 * screen, so on a fresh install this is what the user sees until a campaign lands.
 */
@Composable
private fun EmptyInbox() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Sizes.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconTile(
            icon = Icons.Outlined.Notifications,
            size = 56.dp,
            iconSize = 26.dp,
        )
        Text(
            "Your MoEngage Inbox messages will appear here",
            style = BrewType.support,
            color = BrewColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Space.x16),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun InboxPreview() {
    BrewBarTheme {
        InboxScreen(
            messages = previewMessages,
            onBack = {},
            onMessageClick = {},
            onMarkAllRead = {},
        )
    }
}

private val previewMessages = listOf(
    InboxMessageUi(
        id = "preview-1",
        title = "Your flat white is at the bar",
        body = "Order #BB-4821 · tap to see your order.",
        timestamp = "2 min ago",
        group = InboxGroup.Today,
        read = false,
        accent = InboxAccent.Brand,
        deeplink = null,
        campaignId = null,
    ),
    InboxMessageUi(
        id = "preview-2",
        title = "You're 3 stars from a free drink",
        body = "Order twice more this week and the tenth is on us.",
        timestamp = "6 h ago",
        group = InboxGroup.Today,
        read = false,
        accent = InboxAccent.Star,
        deeplink = null,
        campaignId = null,
    ),
    InboxMessageUi(
        id = "preview-3",
        title = "Happy hour is back",
        body = "20% off every cold brew, 2–5 pm. Code CHILL20.",
        timestamp = "Mon",
        group = InboxGroup.Earlier,
        read = true,
        accent = InboxAccent.Brand,
        deeplink = null,
        campaignId = null,
    ),
)

@Preview(name = "Empty", showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun InboxEmptyPreview() {
    BrewBarTheme {
        InboxScreen(
            messages = emptyList(),
            onBack = {},
            onMessageClick = {},
            onMarkAllRead = {},
        )
    }
}
