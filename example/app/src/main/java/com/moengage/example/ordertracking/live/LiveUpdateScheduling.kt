package com.moengage.example.ordertracking.live

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.LIVE_UPDATE_INTERVAL_SEC
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.WORK_TAG_PREFIX
import com.moengage.example.ordertracking.data.orderSessionRepository
import com.moengage.example.ordertracking.render.routeOrderNotification
import java.util.concurrent.TimeUnit

/** Runs one local notification tick (chip countdown and tracker motion). */
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal suspend fun tickLiveUpdate(context: Context, orderId: String): Boolean {
    val appContext = context.applicationContext
    val sessionRepository = orderSessionRepository(appContext)
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

internal fun scheduleNextLiveUpdate(context: Context, orderId: String, replaceExisting: Boolean = true) {
    val request =
        OneTimeWorkRequestBuilder<LiveUpdateWorker>()
            .setInitialDelay(LIVE_UPDATE_INTERVAL_SEC, TimeUnit.SECONDS)
            .setInputData(Data.Builder().putString(EXTRA_ORDER_ID, orderId).build())
            .addTag(liveUpdateWorkTag(orderId))
            .build()

    val policy = if (replaceExisting) ExistingWorkPolicy.REPLACE else ExistingWorkPolicy.APPEND
    WorkManager.getInstance(context.applicationContext)
        .enqueueUniqueWork(liveUpdateUniqueWorkName(orderId), policy, request)
}

internal fun cancelLiveUpdateWork(context: Context, orderId: String) {
    WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(liveUpdateWorkTag(orderId))
}

private fun liveUpdateWorkTag(orderId: String): String = "$WORK_TAG_PREFIX$orderId"

private fun liveUpdateUniqueWorkName(orderId: String): String = "order_tracking_live_update_$orderId"
