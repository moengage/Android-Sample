package com.moengage.sampleapp.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ui.components.CroppedImage
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 1 — brand intro.
 *
 * MoEngage moment: nothing happens here beyond what already happened. The SDK was
 * initialised in `BrewBarApp.onCreate()` before this composition ran, which is what the
 * footnote is telling the person watching the demo.
 */
@Composable
fun SplashScreen(onGetStarted: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.PrimaryDarkSurface),
    ) {
        CroppedImage(
            R.drawable.cs_splash,
            Modifier
                .fillMaxSize()
                .alpha(0.45f),
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(start = Space.x28, end = Space.x28, top = 56.dp),
                verticalArrangement = Arrangement.spacedBy(Space.x16),
            ) {
                Box(
                    modifier = Modifier
                        .size(Sizes.brandTile)
                        .clip(BrewShapes.card)
                        .background(BrewColors.Primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.LocalCafe,
                        contentDescription = null,
                        tint = BrewColors.OnDarkPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text("Brew Bar", style = BrewType.display, color = BrewColors.OnDarkPrimary)
                Text(
                    "Slow-roast coffee, herbal brews and fresh bakes — ordered before you " +
                        "reach the counter.",
                    style = BrewType.subtitle,
                    color = BrewColors.OnDarkSecondary,
                    modifier = Modifier.widthIn(max = 260.dp),
                )
            }
            Column(
                modifier = Modifier.padding(
                    start = Sizes.screenPadding,
                    end = Sizes.screenPadding,
                    bottom = Space.x28,
                ),
                verticalArrangement = Arrangement.spacedBy(Space.x12),
            ) {
                PrimaryButton("Get started", onGetStarted)
                Text(
                    "MoEngage SDK initialised in Application.onCreate()",
                    style = BrewType.caption,
                    color = BrewColors.OnDarkFootnote,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun SplashPreview() {
    BrewBarTheme { SplashScreen(onGetStarted = {}) }
}
