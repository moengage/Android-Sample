package com.moengage.example.ordertracking.live

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.ordertracking.LIVE_UPDATE_INTERVAL_MS
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.PROGRESS_STYLE_MIN_SDK
import com.moengage.example.ordertracking.TERMINAL_DISMISS_DELAY_MS
import com.moengage.example.ordertracking.data.decodeOrderTrackingPayload
import com.moengage.example.ordertracking.data.pctPayloadJson
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import com.moengage.example.ordertracking.model.orderStage
import com.moengage.example.ordertracking.notification.OrderTrackingDismissPrefs
import com.moengage.example.ordertracking.render.buildOrderTrackingNotification
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Drives order-tracking notification updates from a process-scoped coroutine — no foreground
 * service. Each MoEngage stage push re-posts the notification via
 * [NotificationManagerCompat.notify]; between pushes a local tick loop interpolates the chip
 * countdown and tracker position (see [computeLiveUpdateDisplay]).
 *
 * Trade-off vs a foreground service: the tick loop only runs while the app process is alive. If
 * the OS kills the process the local ticks stop, but the next stage push restarts everything and
 * the notification content is always re-derived from the latest payload, so no state is lost.
 */
internal object OrderTrackingLiveUpdater {

    /** Backstop so an unexpected throw inside a launched job is logged, not crashed on. */
    private val exceptionHandler =
        CoroutineExceptionHandler { _, error -> Log.e(LOG_TAG, "Live update coroutine failed", error) }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + exceptionHandler)
    private var tickJob: Job? = null
    private var terminalDismissJob: Job? = null

    @Volatile private var activeOrderId: String? = null
    @Volatile private var moeBundle: Bundle? = null
    @Volatile private var activePayload: OrderTrackingPayload? = null
    @Volatile private var receivedAtMs: Long = 0L

    /** Posts/updates the notification for a stage push and (re)arms local ticks or terminal dismiss. */
    @RequiresApi(Build.VERSION_CODES.N)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun startOrUpdate(context: Context, pushBundle: Bundle) {
        val appContext = context.applicationContext
        val payloadJson = pctPayloadJson(pushBundle) ?: return
        val payload =
            runCatching { decodeOrderTrackingPayload(payloadJson) }
                .getOrElse {
                    Log.w(LOG_TAG, "Malformed order-tracking payload — skip", it)
                    return
                }

        if (
            !payload.terminal &&
            OrderTrackingDismissPrefs.isDismissed(appContext, payload.orderId) &&
            payload.respectUserDismiss
        ) {
            Log.d(LOG_TAG, "Respecting dismiss — skip notify, orderId=${payload.orderId}")
            return
        }

        stopAllJobs()
        OrderTrackingDismissPrefs.setDismissed(appContext, payload.orderId, false)
        activeOrderId = payload.orderId
        moeBundle = pushBundle
        activePayload = payload
        receivedAtMs = System.currentTimeMillis()

        Log.d(
            LOG_TAG,
            "Stage ${payload.orderStage()?.name ?: payload.stage}, orderId=${payload.orderId}, " +
                "terminal=${payload.terminal}",
        )

        logPromotionEligibility(appContext)
        val display = computeLiveUpdateDisplay(payload, receivedAtMs, receivedAtMs)
        postNotification(appContext, pushBundle, payload, display)
        Log.d(LOG_TAG, "Notify stage ${payload.stage}, orderId=${payload.orderId}")

        if (payload.terminal) {
            scheduleTerminalDismiss(appContext, payload.orderId)
        } else if (shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) {
            startTickLoop(appContext)
        }
    }

    /** Stops local ticks for [orderId] and removes its notification (e.g. on user dismiss). */
    fun stop(context: Context, orderId: String) {
        if (orderId != activeOrderId) return
        stopAllJobs()
        NotificationManagerCompat.from(context.applicationContext).cancel(orderId, NOTIFICATION_ID)
        clearState()
    }

    /**
     * Logs whether the system will actually promote this notification to a Live Update. The user
     * can disable promoted notifications per-app (Settings → ... → Promoted notifications); when
     * off, [postNotification] still posts an ordinary ongoing notification — promotion is silently
     * dropped — so this surfaces the reason the status-bar chip / floating chip is missing.
     */
    private fun logPromotionEligibility(context: Context) {
        if (Build.VERSION.SDK_INT < PROGRESS_STYLE_MIN_SDK) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (!manager.canPostPromotedNotifications()) {
            Log.w(LOG_TAG, "Promoted notifications disabled — Live Update will not be promoted")
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun postNotification(
        context: Context,
        bundle: Bundle,
        payload: OrderTrackingPayload,
        display: LiveUpdateDisplayState,
    ) {
        val notification =
            buildOrderTrackingNotification(
                context = context,
                moeBundle = bundle,
                payload = payload,
                chipText = display.chipText,
                trackerPosition = display.trackerPosition,
            )
        NotificationManagerCompat.from(context).notify(payload.orderId, NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startTickLoop(context: Context) {
        stopTickLoop()
        tickJob =
            scope.launch {
                while (isActive) {
                    delay(LIVE_UPDATE_INTERVAL_MS)
                    val payload = activePayload ?: break
                    val bundle = moeBundle ?: break
                    // Permission can be revoked at runtime while this long-running loop is active.
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.w(LOG_TAG, "POST_NOTIFICATIONS not granted — stop ticks")
                        break
                    }
                    val display =
                        computeLiveUpdateDisplay(payload, receivedAtMs, System.currentTimeMillis())
                    postNotification(context, bundle, payload, display)
                    Log.d(LOG_TAG, "Tick stage ${payload.stage}")
                    if (!shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) break
                }
            }
    }

    private fun scheduleTerminalDismiss(context: Context, orderId: String) {
        stopTickLoop()
        terminalDismissJob?.cancel()
        terminalDismissJob =
            scope.launch {
                delay(TERMINAL_DISMISS_DELAY_MS)
                NotificationManagerCompat.from(context).cancel(orderId, NOTIFICATION_ID)
                OrderTrackingDismissPrefs.clearDismissed(context, orderId)
                clearState()
            }
    }

    private fun stopTickLoop() {
        tickJob?.cancel()
        tickJob = null
    }

    private fun stopAllJobs() {
        stopTickLoop()
        terminalDismissJob?.cancel()
        terminalDismissJob = null
    }

    private fun clearState() {
        activeOrderId = null
        moeBundle = null
        activePayload = null
        receivedAtMs = 0L
    }
}
