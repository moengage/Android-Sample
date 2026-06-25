package com.moengage.example.ordertracking

/**
 * WorkManager integration for local notification updates every [LIVE_UPDATE_INTERVAL_SEC] seconds
 * (chip countdown and tracker motion) until the next MoEngage stage push or stale freeze.
 */

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal suspend fun tickLiveUpdate(context: Context, orderId: String): Boolean {
    val appContext = context.applicationContext
    val sessionRepository = OrderSessionRepository(appContext)
    if (sessionRepository.isDismissed(orderId)) return false

    val payload = sessionRepository.getPayload(orderId) ?: return false
    val receivedAtMs = sessionRepository.getReceivedAtMs(orderId)
    val display = computeLiveUpdateDisplay(payload, receivedAtMs, System.currentTimeMillis())

    routeOrderNotification(
        context = appContext,
        payload = payload,
        chipText = display.chipText,
        trackerPosition = display.trackerPosition,
    )
    Log.d(LOG_TAG, "Local tick stage ${payload.stage}, chip=${display.chipText}")

    return shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)
}

internal fun scheduleNextLiveUpdate(context: Context, orderId: String) {
    val request =
        OneTimeWorkRequestBuilder<LiveUpdateWorker>()
            .setInitialDelay(LIVE_UPDATE_INTERVAL_SEC, TimeUnit.SECONDS)
            .setInputData(Data.Builder().putString(EXTRA_ORDER_ID, orderId).build())
            .addTag(liveUpdateWorkTag(orderId))
            .build()

    WorkManager.getInstance(context.applicationContext)
        .enqueueUniqueWork(liveUpdateUniqueWorkName(orderId), ExistingWorkPolicy.REPLACE, request)
}

internal fun cancelLiveUpdateWork(context: Context, orderId: String) {
    WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(liveUpdateWorkTag(orderId))
}

private fun liveUpdateWorkTag(orderId: String): String = "$WORK_TAG_PREFIX$orderId"

private fun liveUpdateUniqueWorkName(orderId: String): String = "order_tracking_live_update_$orderId"
