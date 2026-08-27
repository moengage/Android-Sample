package com.moengage.sampleapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.Sizes

/**
 * 42×24 track with an 18 dp knob that travels 3 → 21 px over 180 ms — the only animated
 * control in the design besides the notification slide-in.
 */
@Composable
fun BrewToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val knobOffset by animateDpAsState(
        targetValue = if (checked) 21.dp else 3.dp,
        animationSpec = tween(durationMillis = 180),
        label = "toggleKnob",
    )
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> BrewColors.BorderSubtle
            checked -> BrewColors.Primary
            else -> BrewColors.ToggleTrackOff
        },
        animationSpec = tween(durationMillis = 180),
        label = "toggleTrack",
    )
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .size(width = Sizes.toggleWidth, height = Sizes.toggleHeight)
            .clip(BrewShapes.pill)
            .background(trackColor)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobOffset)
                .size(Sizes.toggleKnob)
                .clip(BrewShapes.pill)
                .background(Color.White),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TogglePreview() {
    BrewBarTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        ) {
            BrewToggle(true, {})
            BrewToggle(false, {})
        }
    }
}
