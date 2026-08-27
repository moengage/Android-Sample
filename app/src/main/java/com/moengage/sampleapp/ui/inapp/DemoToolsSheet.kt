package com.moengage.sampleapp.ui.inapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/** What the demo sheet can fire locally, so the app demos without a live campaign. */
data class DemoActions(
    val onRequestPermission: () -> Unit,
    val onShowNativeInApp: () -> Unit,
    val onRenderSelfHandled: () -> Unit,
    val onStartGeofence: () -> Unit,
)

/**
 * The hidden demo affordance — reached by long-pressing the app-bar title (or the Menu
 * greeting). Every entry triggers one MoEngage moment locally so the flow is demonstrable on
 * a device with no campaign targeting it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoToolsSheet(actions: DemoActions, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BrewColors.Surface,
    ) {
        DemoToolsContent(actions)
    }
}

@Composable
private fun DemoToolsContent(actions: DemoActions) {
    Column(
        modifier = Modifier.padding(
            start = Sizes.screenPadding,
            end = Sizes.screenPadding,
            bottom = Space.x28,
        ),
        verticalArrangement = Arrangement.spacedBy(Space.x14),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Space.x4)) {
            Text("Demo tools", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
            Text(
                if (MoEngageSDKHelper.isConfigured) {
                    "Fires each SDK moment locally. Live campaigns still arrive normally."
                } else {
                    "No YOUR_MOENGAGE_WORKSPACE_ID configured — these fire the UI moments only."
                },
                style = BrewType.caption,
                color = BrewColors.TextSecondary,
            )
        }
        DemoRow(
            icon = Icons.Filled.Notifications,
            title = "Request push permission",
            subtitle = "POST_NOTIFICATIONS on API 33+, then pushPermissionResponse",
            onClick = actions.onRequestPermission,
        )
        DemoRow(
            icon = Icons.Filled.Redeem,
            title = "Show native in-app",
            subtitle = "MoEInAppHelper.showInApp(context)",
            onClick = actions.onShowNativeInApp,
        )
        DemoRow(
            icon = Icons.Filled.LocalOffer,
            title = "Render self-handled promo",
            subtitle = "Draws the Menu promo card from the fallback payload",
            onClick = actions.onRenderSelfHandled,
        )
        DemoRow(
            icon = Icons.Filled.LocationOn,
            title = "Start geofence monitoring",
            subtitle = "Location permission, then startGeofenceMonitoring(context)",
            onClick = actions.onStartGeofence,
        )
    }
}

@Composable
private fun DemoRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Space.x6),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(icon, size = 40.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
            Text(subtitle, style = BrewType.caption, color = BrewColors.TextSecondary)
        }
    }
}

@Preview(showBackground = true, widthDp = 412)
@Composable
private fun DemoToolsPreview() {
    BrewBarTheme {
        Column(Modifier.background(BrewColors.Surface)) {
            DemoToolsContent(DemoActions({}, {}, {}, {}))
        }
    }
}
