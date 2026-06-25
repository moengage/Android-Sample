package com.moengage.example.ordertracking

/**
 * Routes each stage to the correct notification UI by API level:
 * - API 36+ → [renderProgressStyleNotification]
 * - API 34–35 → [renderBigPictureFallbackNotification]
 * - API 31–33 → [renderBigTextFallbackNotification]
 * - API ≤30 → [renderStandardFallbackNotification]
 *
 * Reuses the same notification tag ([OrderTrackingPayload.orderId]) for in-place updates.
 */

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.launch

private val terminalDismissHandler = Handler(Looper.getMainLooper())

/** Chooses notification UI by API level and posts to the same slot for each [OrderTrackingPayload.orderId]. */
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun routeOrderNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String = payload.chipText,
    trackerPosition: Int = payload.trackerPosition,
) {
    val appContext = context.applicationContext
    when {
        Build.VERSION.SDK_INT >= PROGRESS_STYLE_MIN_SDK ->
            renderProgressStyleNotification(appContext, payload, chipText, trackerPosition)
        Build.VERSION.SDK_INT in BIG_PICTURE_MIN_SDK..BIG_PICTURE_MAX_SDK ->
            renderBigPictureFallbackNotification(appContext, payload, chipText, trackerPosition)
        Build.VERSION.SDK_INT >= BIG_TEXT_MIN_SDK ->
            renderBigTextFallbackNotification(appContext, payload, chipText)
        else ->
            renderStandardFallbackNotification(appContext, payload, chipText)
    }

    if (payload.terminal) {
        val orderId = payload.orderId
        terminalDismissHandler.postDelayed({
            NotificationManagerCompat.from(appContext)
                .cancel(orderId, NOTIFICATION_ID)
            cancelLiveUpdateWork(appContext, orderId)
            orderTrackingScope.launch {
                OrderSessionRepository(appContext).clearSession(orderId)
            }
        }, TERMINAL_DISMISS_DELAY_MS)
    }
}
