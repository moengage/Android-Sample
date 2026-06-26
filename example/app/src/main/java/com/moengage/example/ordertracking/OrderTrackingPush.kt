package com.moengage.example.ordertracking

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresPermission
import com.moengage.example.ordertracking.data.decodeOrderTrackingPayload
import com.moengage.example.ordertracking.data.orderSessionRepository
import com.moengage.example.ordertracking.data.pctPayloadJson
import com.moengage.example.ordertracking.live.cancelLiveUpdateWork
import com.moengage.example.ordertracking.live.computeLiveUpdateDisplay
import com.moengage.example.ordertracking.live.orderTrackingScope
import com.moengage.example.ordertracking.live.scheduleNextLiveUpdate
import com.moengage.example.ordertracking.live.shouldScheduleLiveUpdate
import com.moengage.example.ordertracking.model.orderStage
import com.moengage.example.ordertracking.render.routeOrderNotification
import kotlinx.coroutines.launch

/**
 * Entry point when MoEngage delivers a self-handled Background Update push with `pct_payload`.
 * Parses JSON, saves session, posts or updates the notification, and starts local countdown ticks.
 */
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun handleOrderTrackingPush(context: Context, bundle: Bundle) {
    orderTrackingScope.launch {
        processOrderTrackingPush(context, bundle)
    }
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private suspend fun processOrderTrackingPush(context: Context, bundle: Bundle) {
    val payloadJson = pctPayloadJson(bundle)
    if (payloadJson == null) {
        Log.d(LOG_TAG, "No pct_payload — skip")
        return
    }

    var orderId: String? = null
    try {
        val payload = decodeOrderTrackingPayload(payloadJson)
        orderId = payload.orderId
        val appContext = context.applicationContext
        val sessionRepository = orderSessionRepository(appContext)
        val receivedAtMs = System.currentTimeMillis()

        Log.d(
            LOG_TAG,
            "Stage ${payload.orderStage()?.name ?: payload.stage}, orderId=$orderId, terminal=${payload.terminal}",
        )

        cancelLiveUpdateWork(appContext, orderId)

        if (
            !payload.terminal &&
            sessionRepository.isDismissed(orderId) &&
            payload.respectUserDismiss
        ) {
            sessionRepository.saveSession(orderId, bundle, receivedAtMs)
            Log.d(LOG_TAG, "Respecting dismiss — skip notify, orderId=$orderId")
            return
        }
        sessionRepository.setDismissed(orderId, false)

        val display = computeLiveUpdateDisplay(payload, receivedAtMs, receivedAtMs)
        sessionRepository.saveSession(orderId, bundle, receivedAtMs)

        routeOrderNotification(
            context = appContext,
            payload = payload,
            chipText = display.chipText,
            trackerPosition = display.trackerPosition,
        )
        Log.d(LOG_TAG, "Posted stage ${payload.stage}, tag=$orderId")

        if (shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) {
            scheduleNextLiveUpdate(appContext, orderId)
        }
    } catch (error: Exception) {
        // Production: emit pct_render_failed telemetry here (orderId, stage, error).
        Log.e(LOG_TAG, "Order tracking push failed, orderId=${orderId ?: "unknown"}", error)
    }
}
