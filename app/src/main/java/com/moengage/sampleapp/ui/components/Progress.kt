package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.OrderStage
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Order-status progress: four 5 dp segments, filled up to and including the current stage,
 * with 11/regular labels underneath.
 */
@Composable
fun ProgressSteps(stage: OrderStage, modifier: Modifier = Modifier) {
    val stages = OrderStage.entries
    val reached = stages.indexOf(stage)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Space.x8)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x6),
        ) {
            stages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Sizes.progressSegmentHeight)
                        .clip(BrewShapes.track)
                        .background(
                            if (index <= reached) BrewColors.Primary else BrewColors.NeutralFill,
                        ),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x6),
        ) {
            stages.forEach { entry ->
                Text(
                    entry.label,
                    style = BrewType.micro,
                    color = if (entry == stage) BrewColors.TextPrimary else BrewColors.TextTertiary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 372)
@Composable
private fun ProgressPreview() {
    BrewBarTheme {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProgressSteps(OrderStage.Brewing)
        }
    }
}
