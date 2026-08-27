package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Space

/**
 * Menu-home category tab: padding 8/14, radius 999.
 * Active = `#06A6B7` fill + white; inactive = 1px `#D9DFED` on white.
 */
@Composable
fun TabPill(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BrewShapes.pill)
            .background(if (selected) BrewColors.Primary else BrewColors.Surface)
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(1.dp, BrewColors.BorderDefault, BrewShapes.pill)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x14, vertical = Space.x8),
    ) {
        Text(
            label,
            style = BrewType.captionMedium,
            color = if (selected) BrewColors.OnDarkPrimary else BrewColors.TextSecondary,
        )
    }
}

/**
 * Category-list filter pill: padding 7/12. The first one is filled `#E5E5E5`,
 * the rest are outlined.
 */
@Composable
fun FilterPill(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BrewShapes.pill)
            .background(if (selected) BrewColors.ComponentBg2 else BrewColors.Surface)
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(1.dp, BrewColors.BorderDefault, BrewShapes.pill)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x12, vertical = 7.dp),
    ) {
        Text(label, style = BrewType.captionMedium, color = BrewColors.TextPrimary)
    }
}

/**
 * Item-detail size card: padding 12, radius 10. Unselected 1px `#ECEFF6`;
 * selected 1px `#06A6B7` over `#EEFAFB` with a medium label.
 */
@Composable
fun SelectCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(BrewShapes.input)
            .background(if (selected) BrewColors.PrimarySelectedTint else BrewColors.Surface)
            .border(
                1.dp,
                if (selected) BrewColors.Primary else BrewColors.BorderSubtle,
                BrewShapes.input,
            )
            .clickable(onClick = onClick)
            .padding(Space.x12),
        verticalArrangement = Arrangement.spacedBy(Space.x4),
    ) {
        Text(
            title,
            style = if (selected) BrewType.bodyMedium else BrewType.body,
            color = BrewColors.TextPrimary,
        )
        Text(subtitle, style = BrewType.micro, color = BrewColors.TextSecondary)
    }
}

/** Item-detail milk pill and cart cup-preference pill: radius 999, padding 8/14. */
@Composable
fun SelectPill(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BrewShapes.pill)
            .background(if (selected) BrewColors.PrimarySelectedTint else BrewColors.Surface)
            .border(
                1.dp,
                if (selected) BrewColors.Primary else BrewColors.BorderSubtle,
                BrewShapes.pill,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x14, vertical = Space.x8),
    ) {
        Text(
            label,
            style = if (selected) BrewType.bodyMedium else BrewType.body,
            color = BrewColors.TextPrimary,
        )
    }
}

/** The small "Add" affordance on a category-list row. */
@Composable
fun AddPill(onClick: () -> Unit, modifier: Modifier = Modifier, label: String = "Add") {
    Box(
        modifier = modifier
            .clip(BrewShapes.chip)
            .border(1.dp, BrewColors.BorderDefault, BrewShapes.chip)
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x14, vertical = Space.x6),
    ) {
        Text(label, style = BrewType.captionMedium, color = BrewColors.Primary)
    }
}

/** Status pill — `#E3F6E8` fill with `#1F7A4D` 11/medium text. */
@Composable
fun StatusPill(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BrewShapes.pill)
            .background(BrewColors.SuccessTint)
            .padding(horizontal = Space.x10, vertical = Space.x4),
    ) {
        Text(label, style = BrewType.microMedium, color = BrewColors.SuccessText)
    }
}

/** Tier pill on the Profile header — `#FFF3DD` fill. */
@Composable
fun TierPill(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(BrewShapes.pill)
            .background(BrewColors.WarmTint)
            .padding(horizontal = Space.x10, vertical = Space.x6),
    ) {
        Text(label, style = BrewType.microMedium, color = BrewColors.WarmIcon)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F3)
@Composable
private fun ChipsPreview() {
    BrewBarTheme {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TabPill("Coffee", true, {})
                TabPill("Herbal teas", false, {})
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterPill("Popular", true, {})
                FilterPill("Under ₹200", false, {})
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                SelectPill("Oat +₹30", true, {})
                AddPill({})
                StatusPill("Brewing")
                TierPill("Gold cup")
            }
        }
    }
}
