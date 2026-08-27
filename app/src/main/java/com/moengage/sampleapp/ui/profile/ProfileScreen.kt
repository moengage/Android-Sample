package com.moengage.sampleapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.moengage.sampleapp.data.DemoUser
import com.moengage.sampleapp.data.ProfileRepository
import com.moengage.sampleapp.domain.model.LocationState
import com.moengage.sampleapp.domain.model.NotificationPreference
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.BrewToggle
import com.moengage.sampleapp.ui.components.DetailRow
import com.moengage.sampleapp.ui.components.SecondaryButton
import com.moengage.sampleapp.ui.components.ThinDivider
import com.moengage.sampleapp.ui.components.TierPill
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 10 — profile.
 *
 * MoEngage moments: the taste-profile values are pushed as user attributes, the notification
 * rows mirror the OS permission state (and deep-link to system settings when blocked), the
 * location card breaks out the three location grants that geofence campaigns depend on, and
 * "Log out" calls `MoECoreHelper.logoutUser`.
 */
@Composable
fun ProfileScreen(
    pushGranted: Boolean,
    pushBlocked: Boolean,
    preferences: List<NotificationPreference>,
    onPreferenceChange: (String, Boolean) -> Unit,
    onRequestPush: () -> Unit,
    onOpenSystemSettings: () -> Unit,
    location: LocationState,
    onRequestLocation: () -> Unit,
    onOpenAppSettings: () -> Unit,
    onLogout: () -> Unit,
    onDemoTools: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        ProfileHeader(onDemoTools)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = Sizes.screenPadding,
                    end = Sizes.screenPadding,
                    top = Space.x18,
                    bottom = Space.x28,
                ),
            verticalArrangement = Arrangement.spacedBy(Space.x18),
        ) {
            TasteProfileCard()
            NotificationsCard(
                pushGranted = pushGranted,
                pushBlocked = pushBlocked,
                preferences = preferences,
                onPreferenceChange = onPreferenceChange,
                onRequestPush = onRequestPush,
                onOpenSystemSettings = onOpenSystemSettings,
            )
            LocationCard(
                location = location,
                onRequestLocation = onRequestLocation,
                onOpenAppSettings = onOpenAppSettings,
            )
            SecondaryButton("Log out", onLogout)
        }
    }
}

@Composable
private fun ProfileHeader(onDemoTools: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrewColors.Surface)
            .padding(Sizes.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(Space.x14),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.avatar)
                .clip(BrewShapes.pill)
                .background(BrewColors.Primary)
                .clickable(onClick = onDemoTools),
            contentAlignment = Alignment.Center,
        ) {
            Text(DemoUser.INITIALS, style = BrewType.initials, color = BrewColors.OnDarkPrimary)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
            Text(DemoUser.NAME, style = BrewType.titleBoldSmall, color = BrewColors.TextPrimary)
            Text(DemoUser.PHONE, style = BrewType.support, color = BrewColors.TextSecondary)
        }
        TierPill(DemoUser.TIER)
    }
}

@Composable
private fun TasteProfileCard() {
    val taste = DemoUser.taste
    val rows = listOf(
        "Favourite drink" to taste.favouriteDrink,
        "Milk" to taste.milk,
        "Sweetness" to taste.sweetness,
        "Home store" to taste.homeStore,
        "Birthday" to taste.birthday,
    )
    Column(verticalArrangement = Arrangement.spacedBy(Space.x10)) {
        Text("Taste profile", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        BrewCard {
            rows.forEachIndexed { index, (label, value) ->
                if (index > 0) ThinDivider()
                DetailRow(
                    label = label,
                    value = value,
                    modifier = Modifier.padding(horizontal = Space.x16, vertical = Space.x14),
                )
            }
        }
    }
}

@Composable
private fun NotificationsCard(
    pushGranted: Boolean,
    pushBlocked: Boolean,
    preferences: List<NotificationPreference>,
    onPreferenceChange: (String, Boolean) -> Unit,
    onRequestPush: () -> Unit,
    onOpenSystemSettings: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.x10)) {
        Text("Notifications", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        BrewCard {
            preferences.forEachIndexed { index, preference ->
                if (index > 0) ThinDivider()
                val isOrderUpdates = preference.key == "order_updates"
                val statusLine = when {
                    !isOrderUpdates -> null
                    pushBlocked && !pushGranted -> "Blocked at OS level"
                    pushGranted -> "Allowed · token registered"
                    else -> "Not requested yet · tap to allow"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Space.x16, vertical = Space.x14),
                    horizontalArrangement = Arrangement.spacedBy(Space.x12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
                        Text(
                            preference.label,
                            style = BrewType.body,
                            color = BrewColors.TextPrimary,
                        )
                        if (statusLine != null) {
                            Text(
                                statusLine,
                                style = BrewType.caption,
                                color = if (pushBlocked && !pushGranted) {
                                    BrewColors.UnreadBadge
                                } else {
                                    BrewColors.TextSecondary
                                },
                            )
                        }
                    }
                    // When the OS has blocked us, the toggle deep-links to system settings
                    // rather than pretending the app can flip it.
                    if (isOrderUpdates && pushBlocked && !pushGranted) {
                        Text(
                            "Open settings",
                            style = BrewType.captionMedium,
                            color = BrewColors.Link,
                            modifier = Modifier.clickable(onClick = onOpenSystemSettings),
                        )
                    } else {
                        // Order updates ride on POST_NOTIFICATIONS, so before it is granted the
                        // toggle is the permission prompt's entry point rather than a preference.
                        val gatedOnPermission = isOrderUpdates && !pushGranted
                        BrewToggle(
                            checked = if (isOrderUpdates) pushGranted else preference.enabled,
                            onCheckedChange = { checked ->
                                if (gatedOnPermission) {
                                    onRequestPush()
                                } else {
                                    onPreferenceChange(preference.key, checked)
                                }
                            },
                            enabled = true,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationCard(location: LocationState, onRequestLocation: () -> Unit, onOpenAppSettings: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.x10)) {
        Text("Location", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        BrewCard {
            // Broken out per grant rather than shown as one "location" switch: they are separate
            // permissions with separate prompts, and only the foreground pair can still be asked
            // for in-app once the user has been through the dialog.
            PermissionRow(
                label = "Approximate location",
                caption = "ACCESS_COARSE_LOCATION",
                granted = location.approximate,
                asked = location.asked,
                action = "Allow".takeUnless { location.approximate },
                onAction = onRequestLocation,
            )
            ThinDivider()
            PermissionRow(
                label = "Precise location",
                caption = "ACCESS_FINE_LOCATION · what a fence is registered against",
                granted = location.precise,
                asked = location.asked,
                action = "Allow".takeUnless { location.precise },
                onAction = onRequestLocation,
            )
            if (location.backgroundRequired) {
                ThinDivider()
                PermissionRow(
                    label = "Allow all the time",
                    caption = "ACCESS_BACKGROUND_LOCATION · settings only on Android 11+",
                    granted = location.background,
                    asked = location.asked,
                    action = "Open settings".takeUnless { location.background },
                    onAction = onOpenAppSettings,
                )
            }
            ThinDivider()
            DetailRow(
                label = "Geofence monitoring",
                value = if (location.monitoring) "Active" else "Inactive",
                valueColor = if (location.monitoring) BrewColors.SuccessText else BrewColors.TextTertiary,
                modifier = Modifier.padding(horizontal = Space.x16, vertical = Space.x14),
            )
        }
    }
}

@Composable
private fun PermissionRow(
    label: String,
    caption: String,
    granted: Boolean,
    asked: Boolean,
    action: String?,
    onAction: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.x16, vertical = Space.x14),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
            Text(label, style = BrewType.body, color = BrewColors.TextPrimary)
            Text(caption, style = BrewType.caption, color = BrewColors.TextTertiary)
        }
        Text(
            when {
                granted -> "Granted"
                // checkSelfPermission cannot tell "never asked" from "denied", so the answer
                // comes from whether a prompt has come back this session.
                asked -> "Denied"
                else -> "Not requested"
            },
            style = BrewType.captionMedium,
            color = when {
                granted -> BrewColors.SuccessText
                asked -> BrewColors.UnreadBadge
                else -> BrewColors.TextSecondary
            },
        )
        if (action != null) {
            Text(
                action,
                style = BrewType.captionMedium,
                color = BrewColors.Link,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun ProfilePreview() {
    BrewBarTheme {
        ProfileScreen(
            pushGranted = true,
            pushBlocked = false,
            onRequestPush = {},
            preferences = ProfileRepository.notificationPreferences,
            onPreferenceChange = { _, _ -> },
            onOpenSystemSettings = {},
            location = LocationState(
                approximate = true,
                precise = true,
                background = true,
                asked = true,
                backgroundRequired = true,
                monitoring = true,
            ),
            onRequestLocation = {},
            onOpenAppSettings = {},
            onLogout = {},
            onDemoTools = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892, name = "Profile · push blocked")
@Composable
private fun ProfileBlockedPreview() {
    BrewBarTheme {
        ProfileScreen(
            pushGranted = false,
            pushBlocked = true,
            onRequestPush = {},
            preferences = ProfileRepository.notificationPreferences,
            onPreferenceChange = { _, _ -> },
            onOpenSystemSettings = {},
            location = LocationState(
                approximate = true,
                precise = true,
                asked = true,
                backgroundRequired = true,
            ),
            onRequestLocation = {},
            onOpenAppSettings = {},
            onLogout = {},
            onDemoTools = {},
        )
    }
}
