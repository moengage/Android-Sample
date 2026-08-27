package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/** The four bottom-nav destinations. */
enum class BottomTab(val label: String, val icon: ImageVector) {
    Menu("Menu", Icons.Filled.Storefront),
    Orders("Orders", Icons.AutoMirrored.Filled.ReceiptLong),

    // Label shortened to fit a quarter-width tab; the screen itself is titled in full.
    SelfHandledCards("Cards", Icons.Filled.Redeem),
    Profile("Profile", Icons.Filled.PersonOutline),
}

/**
 * Height 66, four equal columns. Active `#06A6B7` + medium weight, inactive `#8492AB`.
 * 22 dp icon over an 11 dp label with a 3 dp gap.
 */
@Composable
fun BottomNavBar(selected: BottomTab, onSelect: (BottomTab) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().background(BrewColors.Surface)) {
        ThinDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Sizes.bottomNavHeight),
        ) {
            BottomTab.entries.forEach { tab ->
                val active = tab == selected
                val interaction = remember { MutableInteractionSource() }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClick = { onSelect(tab) },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        tab.icon,
                        contentDescription = tab.label,
                        tint = if (active) BrewColors.Primary else BrewColors.TextTertiary,
                        modifier = Modifier.size(Sizes.bottomNavIcon),
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.height(Space.x4 - 1.dp))
                    Text(
                        tab.label,
                        style = if (active) BrewType.microMedium else BrewType.micro,
                        color = if (active) BrewColors.Primary else BrewColors.TextTertiary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412)
@Composable
private fun BottomNavPreview() {
    BrewBarTheme { BottomNavBar(BottomTab.Menu, {}) }
}
