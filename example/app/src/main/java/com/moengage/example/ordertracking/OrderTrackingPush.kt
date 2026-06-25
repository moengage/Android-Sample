package com.moengage.example.ordertracking

/**
 * Entry point when MoEngage delivers a self-handled Background Update push with `pct_payload`.
 * Parses JSON, saves session, posts or updates the notification, and starts local countdown ticks.
 */

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.launch
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)

internal fun handleOrderTrackingPush(context: Context, bundle: Bundle) {
    orderTrackingScope.launch {
        processOrderTrackingPush(context, bundle)
    }
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private suspend fun processOrderTrackingPush(context: Context, bundle: Bundle) {
    val payloadJson = bundle.getString(PAYLOAD_KEY)?.takeIf { it.isNotBlank() }
    if (payloadJson == null) {
        Log.d(LOG_TAG, "No pct_payload — skip")
        return
    }

    try {
        val payload = decodeOrderTrackingPayload(payloadJson)
        val appContext = context.applicationContext
        val sessionRepository = OrderSessionRepository(appContext)
        val receivedAtMs = System.currentTimeMillis()

        Log.d(
            LOG_TAG,
            "Stage ${payload.orderStage()?.name ?: payload.stage}, orderId=${payload.orderId}, terminal=${payload.terminal}",
        )

        cancelLiveUpdateWork(appContext, payload.orderId)

        if (payload.terminal) {
            sessionRepository.setDismissed(payload.orderId, false)
        } else if (
            sessionRepository.isDismissed(payload.orderId) &&
            payload.respectUserDismiss
        ) {
            sessionRepository.saveSession(payload.orderId, bundle, receivedAtMs)
            Log.d(LOG_TAG, "Respecting dismiss — skip notify, orderId=${payload.orderId}")
            return
        } else {
            sessionRepository.setDismissed(payload.orderId, false)
        }

        val display = computeLiveUpdateDisplay(payload, receivedAtMs, receivedAtMs)
        sessionRepository.saveSession(payload.orderId, bundle, receivedAtMs)

        routeOrderNotification(
            context = appContext,
            payload = payload,
            chipText = display.chipText,
            trackerPosition = display.trackerPosition,
        )
        Log.d(LOG_TAG, "Posted stage ${payload.stage}, tag=${payload.orderId}")

        if (shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) {
            scheduleNextLiveUpdate(appContext, payload.orderId)
        }
    } catch (error: Exception) {
        Log.d(LOG_TAG, "Parse failed: ${error.message}")
    }
}
