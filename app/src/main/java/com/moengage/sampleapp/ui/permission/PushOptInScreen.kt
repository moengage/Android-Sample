package com.moengage.sampleapp.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ui.components.ImageBanner
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.components.QuietTextButton
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

private val VALUE_PROPS = listOf(
    "A ping the moment your order hits the bar",
    "Croissants out of the oven at 8:30 am",
    "Star milestones and free-drink reminders",
)

/**
 * Screen 3 — the value-prop screen that precedes the OS permission dialog.
 *
 * MoEngage moment: "Enable notifications" runs the Android 13+ `POST_NOTIFICATIONS` request
 * and reports the outcome back with `pushPermissionResponse` + the `push_opt_in` attribute.
 */
@Composable
fun PushOptInScreen(onEnable: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.PageBackground),
    ) {
        Box(Modifier.background(BrewColors.PrimaryLightTint)) {
            ImageBanner(R.drawable.cs_permission, Sizes.permissionImageHeight)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = Space.x22, end = Space.x22, top = 26.dp, bottom = Space.x22),
            verticalArrangement = Arrangement.spacedBy(Space.x14),
        ) {
            Text(
                "Know the moment it's ready",
                style = BrewType.screenTitleSmall,
                color = BrewColors.TextPrimary,
            )
            Text(
                "We only send what's useful: your order, the bakes you like and the stars " +
                    "you're about to earn.",
                style = BrewType.subtitle,
                color = BrewColors.TextSecondary,
            )
            VALUE_PROPS.forEach { line ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.x10),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = BrewColors.Primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(line, style = BrewType.body, color = BrewColors.TextPrimary)
                }
            }
        }
        Column(
            modifier = Modifier.padding(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                bottom = Space.x22,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x8),
        ) {
            PrimaryButton("Enable notifications", onEnable)
            QuietTextButton("Not now", onSkip)
        }
    }
}

/**
 * The simulated OS dialog. Real devices show the platform prompt; this stands in on API < 33
 * and in DemoTools so the moment is demonstrable anywhere.
 */
@Composable
fun PermissionDialogOverlay(onAllow: () -> Unit, onDeny: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.DialogScrim),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .clip(BrewShapes.osDialog)
                .background(BrewColors.OsDialogSurface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Space.x16),
        ) {
            Box(
                modifier = Modifier
                    .size(Sizes.touchTarget)
                    .clip(BrewShapes.pill)
                    .background(BrewColors.OsDialogIconCircle),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = BrewColors.TextPrimary,
                    modifier = Modifier.size(22.dp),
                )
            }
            Text(
                "Allow Brew Bar to send you notifications?",
                style = BrewType.cardTitle,
                color = BrewColors.TextPrimary,
                textAlign = TextAlign.Center,
            )
            DialogAction("Allow", onAllow)
            DialogAction("Don't allow", onDeny)
        }
    }
}

@Composable
private fun DialogAction(label: String, onClick: () -> Unit) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(Sizes.touchTarget),
    ) {
        Text(label, style = BrewType.cardTitle, color = BrewColors.OsDialogAction)
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun PushOptInPreview() {
    BrewBarTheme { PushOptInScreen(onEnable = {}, onSkip = {}) }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun PermissionDialogPreview() {
    BrewBarTheme { PermissionDialogOverlay(onAllow = {}, onDeny = {}) }
}
