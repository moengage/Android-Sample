package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/** 36×36 radius-10 outlined back tile — the design's back affordance. */
@Composable
fun BackTile(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(Sizes.backTile)
            .clip(BrewShapes.input)
            .border(1.dp, BrewColors.BorderDefault, BrewShapes.input)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = BrewColors.TextPrimary,
            modifier = Modifier.size(18.dp),
        )
    }
}

/**
 * White app bar with a bottom border: back tile, a 16/medium title with an optional 12/regular
 * subtitle, and a trailing slot.
 *
 * Long-pressing the title opens `DemoTools` — the hidden affordance that fires every SDK
 * moment locally, so the app demos without a live campaign.
 */
@Composable
fun BrewAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    onTitleLongPress: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth().background(BrewColors.Surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Sizes.screenPadding, vertical = Space.x14),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
        ) {
            if (onBack != null) BackTile(onBack)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (onTitleLongPress != null) {
                            Modifier.pointerInput(Unit) {
                                detectLongPress { onTitleLongPress() }
                            }
                        } else {
                            Modifier
                        },
                    ),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(title, style = BrewType.cardTitle, color = BrewColors.TextPrimary)
                if (subtitle != null) {
                    Text(subtitle, style = BrewType.caption, color = BrewColors.TextSecondary)
                }
            }
            trailing()
        }
        ThinDivider()
    }
}

private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectLongPress(onLongPress: () -> Unit) {
    detectTapGestures(onLongPress = { onLongPress() })
}

/** An inline 12/medium link, used in app bars ("Mark all read") and section headers. */
@Composable
fun InlineLink(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color = BrewColors.Link) {
    Text(
        label,
        style = BrewType.captionMedium,
        color = color,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = Space.x6, horizontal = Space.x4),
    )
}

@Composable
fun SpacerWidth(width: androidx.compose.ui.unit.Dp) = Spacer(Modifier.size(width))

@Preview(showBackground = true, widthDp = 412)
@Composable
private fun AppBarPreview() {
    BrewBarTheme {
        BrewAppBar(
            title = "Your orders",
            subtitle = "28 orders · 14 flat whites",
            onBack = {},
            trailing = { InlineLink("Mark all read", {}) },
        )
    }
}
