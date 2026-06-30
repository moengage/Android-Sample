package com.moengage.example.ordertracking.render

import android.app.Notification
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.moengage.example.ordertracking.BIG_PICTURE_MAX_SDK
import com.moengage.example.ordertracking.BIG_PICTURE_MIN_SDK
import com.moengage.example.ordertracking.BIG_TEXT_MIN_SDK
import com.moengage.example.ordertracking.PROGRESS_STYLE_MIN_SDK
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/**
 * Routes each stage to the correct notification UI by API level:
 * - API 36+ → [buildProgressStyleNotification]
 * - API 34–35 → [buildBigPictureFallbackNotification]
 * - API 31–33 → [buildBigTextFallbackNotification]
 * - API ≤30 → [buildStandardFallbackNotification]
 */
@RequiresApi(Build.VERSION_CODES.N)
internal fun buildOrderTrackingNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String = payload.chipText,
    trackerPosition: Int = payload.trackerPosition,
    receivedAtMs: Long = System.currentTimeMillis(),
    nowMs: Long = receivedAtMs,
): Notification =
    when {
        Build.VERSION.SDK_INT >= PROGRESS_STYLE_MIN_SDK ->
            buildProgressStyleNotification(
                context,
                moeBundle,
                payload,
                chipText,
                trackerPosition,
                receivedAtMs,
                nowMs,
            )
        Build.VERSION.SDK_INT in BIG_PICTURE_MIN_SDK..BIG_PICTURE_MAX_SDK ->
            buildBigPictureFallbackNotification(context, moeBundle, payload, chipText, trackerPosition)
        Build.VERSION.SDK_INT >= BIG_TEXT_MIN_SDK ->
            buildBigTextFallbackNotification(context, moeBundle, payload, chipText)
        else ->
            buildStandardFallbackNotification(context, moeBundle, payload, chipText)
    }
