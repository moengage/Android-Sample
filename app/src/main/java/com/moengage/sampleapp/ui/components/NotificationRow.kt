package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.InboxAccent
import com.moengage.sampleapp.domain.model.InboxGroup
import com.moengage.sampleapp.domain.model.InboxMessageUi
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Inbox row: radius 12 white with a 1px border. Unread adds a 3px `#06A6B7` left border and
 * keeps full opacity; read rows drop to 72%.
 */
@Composable
fun NotificationRow(message: InboxMessageUi, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(BrewShapes.button)
            .background(BrewColors.Surface)
            .border(1.dp, BrewColors.BorderSubtle, BrewShapes.button)
            .clickable(onClick = onClick)
            .alpha(if (message.read) 0.72f else 1f)
            .height(androidx.compose.foundation.layout.IntrinsicSize.Min),
    ) {
        if (!message.read) {
            Box(
                Modifier
                    .width(Sizes.unreadStripe)
                    .fillMaxHeight()
                    .background(BrewColors.Primary),
            )
        }
        Row(
            modifier = Modifier.padding(Space.x14),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
        ) {
            IconTile(
                icon = if (message.accent == InboxAccent.Star) Icons.Filled.Star else Icons.Filled.LocalCafe,
                size = Sizes.iconTileSmall,
                shape = BrewShapes.iconTile,
                background = if (message.accent ==
                    InboxAccent.Star
                ) {
                    BrewColors.WarmTint
                } else {
                    BrewColors.PrimaryLightTint
                },
                tint = if (message.accent == InboxAccent.Star) BrewColors.WarmIcon else BrewColors.Primary,
                iconSize = 17.dp,
            )
            Column(verticalArrangement = Arrangement.spacedBy(Space.x4)) {
                Text(message.title, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
                Text(message.body, style = BrewType.support, color = BrewColors.TextSecondary)
                Text(message.timestamp, style = BrewType.micro, color = BrewColors.TextTertiary)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F3, widthDp = 412)
@Composable
private fun NotificationRowPreview() {
    BrewBarTheme {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            previewRows.forEach { NotificationRow(it, {}) }
        }
    }
}

private val previewRows = listOf(
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
        body = "20% off every cold brew, 2–5 pm.",
        timestamp = "Mon",
        group = InboxGroup.Earlier,
        read = true,
        accent = InboxAccent.Brand,
        deeplink = null,
        campaignId = null,
    ),
)
