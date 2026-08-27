package com.moengage.sampleapp.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Every colour in Brew Bar lives here — screens never spell out a hex literal.
 * Values are the resolved MoEngage design-system tokens from the design handoff.
 */
object BrewColors {
    /** CTAs, active tab, selected states, progress. */
    val Primary = Color(0xFF06A6B7)

    /** Splash, self-handled cards header, dark promo card. */
    val PrimaryDarkSurface = Color(0xFF06333A)

    /** Icon tile inside the dark card. */
    val PrimaryDark2 = Color(0xFF0B4B55)

    /** Icon tiles, success banner, confirmation header. */
    val PrimaryLightTint = Color(0xFFDFF4F5)

    /** Selected size / milk chips. */
    val PrimarySelectedTint = Color(0xFFEEFAFB)

    /** Progress and icons drawn on a dark surface. */
    val AccentTealOnDark = Color(0xFF7FE3EA)

    /** Screen background. */
    val PageBackground = Color(0xFFF8F6F3)

    /** Cards, sheets, app bars. */
    val Surface = Color(0xFFFFFFFF)

    /** Store strip, progress track. */
    val NeutralFill = Color(0xFFF1EFEC)

    /** Search field, filter-chip fill. */
    val ComponentBg2 = Color(0xFFE5E5E5)

    /** Card borders, dividers. */
    val BorderSubtle = Color(0xFFECEFF6)

    /** Inputs, outline buttons. */
    val BorderDefault = Color(0xFFD9DFED)

    /** Headings, values. */
    val TextPrimary = Color(0xFF1E1E1E)

    /** Supporting copy. */
    val TextSecondary = Color(0xFF485771)

    /** Timestamps, hints. */
    val TextTertiary = Color(0xFF8492AB)

    /** Inline actions. */
    val Link = Primary

    /** Star / loyalty tiles. */
    val WarmTint = Color(0xFFFFF3DD)
    val WarmIcon = Color(0xFFA86A12)

    /** Status pill, discount amount. */
    val SuccessTint = Color(0xFFE3F6E8)
    val SuccessText = Color(0xFF1F7A4D)

    /** Inbox count badge. */
    val UnreadBadge = Color(0xFFD3453F)

    /** Scrim behind the simulated OS permission dialog. */
    val DialogScrim = Color(0x8014181C)

    /** OS-dialog card + its icon circle (screen 3 overlay). */
    val OsDialogSurface = Color(0xFFF4F7F9)
    val OsDialogIconCircle = Color(0xFFDAE5E1)
    val OsDialogAction = Color(0xFF0B6B7A)

    /** Push-notification shade surface (screen 9 overlay). */
    val NotificationSurface = Color(0xFFEEF4F6)

    /** Off state of the notification toggles on Profile. */
    val ToggleTrackOff = Color(0xFFCFD6E4)

    /** Text drawn on the dark splash / self-handled cards surfaces. */
    val OnDarkPrimary = Color(0xFFFFFFFF)
    val OnDarkSecondary = Color(0xC7FFFFFF) // rgba(255,255,255,.78)
    val OnDarkTertiary = Color(0xB3FFFFFF) // rgba(255,255,255,.70)
    val OnDarkFootnote = Color(0x8CFFFFFF) // rgba(255,255,255,.55)
    val OnDarkTrack = Color(0x2EFFFFFF) // rgba(255,255,255,.18)
}
