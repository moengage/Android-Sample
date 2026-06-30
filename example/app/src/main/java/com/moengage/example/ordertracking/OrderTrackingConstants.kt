/**
 * All shared constants for the order-tracking sample.
 * Kept in one file so customers can grep channel IDs, SDK gates, and tick interval in one place.
 */
package com.moengage.example.ordertracking

// — Push & payload —

/** Dashboard custom key containing the order-tracking JSON string. */
internal const val PAYLOAD_KEY = "pct_payload"

internal const val DEFAULT_STALE_CHIP_TEXT = "Soon"

// — Notification —

internal const val CHANNEL_ID = "order_tracking"
internal const val NOTIFICATION_ID = 9001
internal const val TERMINAL_DISMISS_DELAY_MS = 3_000L

internal const val LOG_TAG = "OrderTracking"

// — Foreground service & local ticks —

internal const val ACTION_STOP_ORDER_TRACKING = "com.moengage.example.ordertracking.STOP"
internal const val EXTRA_ORDER_ID = "order_id"
/** Interval between FGS ticks for tracker motion and fallback UI updates. */
internal const val LIVE_UPDATE_INTERVAL_MS = 120_000L

// — API-level routing —

/** Android API level that supports Notification ProgressStyle and Live Updates. */
internal const val PROGRESS_STYLE_MIN_SDK = 36

/** Android 14–15: BigPicture fallback with client-drawn progress strip. */
internal const val BIG_PICTURE_MIN_SDK = 34
internal const val BIG_PICTURE_MAX_SDK = 35

/** Android 12–13: BigText fallback with emoji segment indicators. */
internal const val BIG_TEXT_MIN_SDK = 31

// — Progress bar & countdown math —

/** Number of stages in the food-delivery order journey. */
internal const val ORDER_STAGE_COUNT = 6

/** Milliseconds in one minute — used for chip countdown math. */
internal const val MS_PER_MINUTE = 60_000L

/** Rounds remaining time up to the next whole minute when formatting chip text. */
internal const val CHIP_MINUTE_ROUND_UP_MS = 59_999L

// — BigPicture fallback rendering —

/** BigPicture fallback: fill colour for the tracker dot on the drawn progress strip. */
internal const val STRIP_TRACKER_DOT_COLOR_HEX = "#111827"
