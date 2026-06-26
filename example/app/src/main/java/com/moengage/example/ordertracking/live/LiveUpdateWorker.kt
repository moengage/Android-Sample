package com.moengage.example.ordertracking.live

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result as WorkerResult
import androidx.work.WorkerParameters
import com.moengage.example.ordertracking.EXTRA_ORDER_ID

/** WorkManager [CoroutineWorker] that runs one local update tick and chains the next job when needed. */
internal class LiveUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): WorkerResult {
        val orderId = inputData.getString(EXTRA_ORDER_ID) ?: return WorkerResult.failure()
        val shouldContinue = tickLiveUpdate(applicationContext, orderId)
        if (shouldContinue) {
            scheduleNextLiveUpdate(applicationContext, orderId, replaceExisting = false)
        }
        return WorkerResult.success()
    }
}
