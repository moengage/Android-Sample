package com.moengage.example.ordertracking

/**
 * All shared constants for the order-tracking sample.
 */

/** Dashboard custom key containing the order-tracking JSON string. */
internal const val PAYLOAD_KEY = "pct_payload"

internal const val DEFAULT_STALE_CHIP_TEXT = "Soon"

internal const val CHANNEL_ID = "order_tracking"
internal const val LIVE_CHANNEL_ID = "order_tracking_live"
internal const val NOTIFICATION_ID = 9001
internal const val TERMINAL_DISMISS_DELAY_MS = 3_000L

internal const val LOG_TAG = "OrderTracking"

internal const val SESSIONS_DIR_NAME = "order_tracking_sessions"
internal const val EXTRA_ORDER_ID = "order_id"
internal const val WORK_TAG_PREFIX = "ORDER_TRACKING_LIVE_UPDATE_"
internal const val LIVE_UPDATE_INTERVAL_SEC = 60L

/** Android API level that supports Notification ProgressStyle and Live Updates. */
internal const val PROGRESS_STYLE_MIN_SDK = 36

/** Android 14–15: BigPicture fallback with client-drawn progress strip. */
internal const val BIG_PICTURE_MIN_SDK = 34
internal const val BIG_PICTURE_MAX_SDK = 35

/** Android 12–13: BigText fallback with emoji segment indicators. */
internal const val BIG_TEXT_MIN_SDK = 31

/** Total progress units on the notification progress bar (food-delivery sample). */
internal const val PROGRESS_BAR_MAX = 3000

/** Number of stages in the food-delivery order journey. */
internal const val ORDER_STAGE_COUNT = 6

/** Milliseconds in one minute — used for chip countdown math. */
internal const val MS_PER_MINUTE = 60_000L

/** Rounds remaining time up to the next whole minute when formatting chip text. */
internal const val CHIP_MINUTE_ROUND_UP_MS = 59_999L

/** BigPicture fallback: fill colour for the tracker dot on the drawn progress strip. */
internal const val STRIP_TRACKER_DOT_COLOR_HEX = "#111827"
