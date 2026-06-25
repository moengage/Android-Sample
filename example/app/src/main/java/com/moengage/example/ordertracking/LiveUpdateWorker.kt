package com.moengage.example.ordertracking

/**
 * WorkManager [Worker] that runs one local update tick, then chains the next job if the countdown
 * is still active.
 */

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking

internal class LiveUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : Worker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        val orderId = inputData.getString(EXTRA_ORDER_ID) ?: return Result.failure()
        val shouldContinue =
            runBlocking {
                tickLiveUpdate(applicationContext, orderId)
            }
        if (shouldContinue) {
            scheduleNextLiveUpdate(applicationContext, orderId)
        }
        return Result.success()
    }
}
