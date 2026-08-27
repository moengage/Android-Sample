package com.moengage.sampleapp.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale and fixed control metrics from the design handoff.
 * `Space` is the 4/6/8/…/28 ramp; `Sizes` is everything with a spec'd height or diameter.
 */
object Space {
    val x4 = 4.dp
    val x6 = 6.dp
    val x8 = 8.dp
    val x10 = 10.dp
    val x12 = 12.dp
    val x14 = 14.dp
    val x16 = 16.dp
    val x18 = 18.dp
    val x20 = 20.dp
    val x22 = 22.dp
    val x28 = 28.dp
}

object Sizes {
    /** Horizontal safe padding on every screen. */
    val screenPadding = 20.dp

    /** Bottom padding that clears the bottom nav on scrolling screens. */
    val bottomNavClearance = 96.dp

    val buttonHeight = 52.dp
    val inputHeight = 48.dp
    val searchHeight = 46.dp
    val bottomNavHeight = 66.dp
    val bottomNavIcon = 22.dp

    /** Category-list rows never shrink below this. */
    val listRowMinHeight = 112.dp
    val listRowImageWidth = 100.dp

    val featuredImageHeight = 104.dp
    val heroImageHeight = 220.dp
    val permissionImageHeight = 290.dp

    val brandTile = 52.dp
    val iconTileLarge = 52.dp
    val iconTileMedium = 44.dp
    val iconTileSmall = 34.dp
    val cartThumb = 52.dp

    val avatar = 56.dp
    val statusCircle = 60.dp
    val bellButton = 38.dp
    val badge = 17.dp
    val backTile = 36.dp
    val heroBackButton = 34.dp

    val toggleWidth = 42.dp
    val toggleHeight = 24.dp
    val toggleKnob = 18.dp

    val progressSegmentHeight = 5.dp
    val unreadStripe = 3.dp

    /** Minimum touch target. */
    val touchTarget = 44.dp
}
