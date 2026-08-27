package com.moengage.sampleapp.ui.inapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.PromoPayload
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * The dark promo card on Menu home — this app's rendering of a **self-handled** in-app
 * campaign. Title, subtitle and deep link come from the campaign payload; the prototype copy
 * is the fallback when a field is missing.
 *
 * The card is only composed when a campaign is live, so a paused campaign leaves the Menu
 * layout untouched rather than showing an empty slot.
 */
@Composable
fun SelfHandledPromoCard(
    payload: PromoPayload,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(BrewShapes.card)
            .background(BrewColors.PrimaryDarkSurface)
            .clickable(onClick = onClick)
            .padding(Space.x16),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(
            icon = Icons.Filled.LocalOffer,
            size = Sizes.iconTileLarge,
            background = BrewColors.PrimaryDark2,
            tint = BrewColors.AccentTealOnDark,
            iconSize = 22.dp,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Space.x4),
        ) {
            Text(payload.title, style = BrewType.bodyMedium, color = BrewColors.OnDarkPrimary)
            Text(payload.subtitle, style = BrewType.caption, color = BrewColors.OnDarkTertiary)
        }
        Icon(
            Icons.Filled.Close,
            contentDescription = "Dismiss offer",
            tint = BrewColors.OnDarkTertiary,
            modifier = Modifier
                .size(Space.x16)
                .clickable(onClick = onDismiss),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F3, widthDp = 412)
@Composable
private fun PromoCardPreview() {
    BrewBarTheme {
        SelfHandledPromoCard(
            payload = PromoPayload.FALLBACK,
            onClick = {},
            onDismiss = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
