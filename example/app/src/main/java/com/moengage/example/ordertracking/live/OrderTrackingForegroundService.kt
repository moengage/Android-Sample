package com.moengage.example.ordertracking.live

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.ACTION_STOP_ORDER_TRACKING
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.LIVE_UPDATE_INTERVAL_MS
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.TERMINAL_DISMISS_DELAY_MS
import com.moengage.example.ordertracking.data.decodeOrderTrackingPayload
import com.moengage.example.ordertracking.data.pctPayloadJson
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import com.moengage.example.ordertracking.model.orderStage
import com.moengage.example.ordertracking.notification.OrderTrackingDismissPrefs
import com.moengage.example.ordertracking.render.buildOrderTrackingNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** Foreground service for local chip/tracker ticks between MoEngage stage pushes. */
internal class OrderTrackingForegroundService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var tickJob: Job? = null
    private var terminalDismissJob: Job? = null
    private var activeOrderId: String? = null
    private var moeBundle: android.os.Bundle? = null
    private var receivedAtMs: Long = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_ORDER_TRACKING) {
            // stop() delivers this via startService (not startForegroundService), so no
            // startForeground() is owed on this path.
            val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return START_NOT_STICKY
            if (orderId == activeOrderId) {
                stopAllJobs()
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            return START_NOT_STICKY
        }

        // Every non-stop start arrived via startForegroundService, so the framework now requires a
        // startForeground() call within ~5s on EVERY path — otherwise it throws
        // ForegroundServiceDidNotStartInTimeException. So we promote first and only then decide
        // whether to bail (dismiss/malformed/null-intent restart).
        val pushExtras = intent?.extras ?: return promoteThenStop()
        val payload = parsePayload(pushExtras) ?: return promoteThenStop()

        if (
            !payload.terminal &&
            payload.respectUserDismiss &&
            OrderTrackingDismissPrefs.isDismissed(this, payload.orderId)
        ) {
            Log.d(LOG_TAG, "Respecting dismiss — skip notify, orderId=${payload.orderId}")
            return promoteThenStop()
        }

        OrderTrackingDismissPrefs.setDismissed(this, payload.orderId, false)
        activeOrderId = payload.orderId
        moeBundle = pushExtras
        receivedAtMs = System.currentTimeMillis()

        Log.d(
            LOG_TAG,
            "Stage ${payload.orderStage()?.name ?: payload.stage}, orderId=${payload.orderId}, " +
                "terminal=${payload.terminal}",
        )

        val display = computeLiveUpdateDisplay(payload, receivedAtMs, receivedAtMs)
        val notification =
            buildOrderTrackingNotification(
                context = this,
                moeBundle = pushExtras,
                payload = payload,
                chipText = display.chipText,
                trackerPosition = display.trackerPosition,
                receivedAtMs = receivedAtMs,
                nowMs = receivedAtMs,
            )
        promoteToForeground(notification)
        Log.d(LOG_TAG, "FGS stage ${payload.stage}, orderId=${payload.orderId}, chip=${display.chipText}")

        if (payload.terminal) {
            scheduleTerminalDismiss(payload.orderId)
        } else if (shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) {
            startTickLoop()
        } else {
            stopTickLoop()
        }
        // START_NOT_STICKY: after process death we cannot rebuild the notification (the push Bundle
        // is gone), and START_STICKY would restart us with a null intent → contract violation.
        return START_NOT_STICKY
    }

    /** Parses the PCT payload, returning null on absent/malformed JSON instead of throwing. */
    private fun parsePayload(pushExtras: android.os.Bundle): OrderTrackingPayload? {
        val payloadJson = pctPayloadJson(pushExtras) ?: return null
        return try {
            decodeOrderTrackingPayload(payloadJson)
        } catch (e: Exception) {
            Log.w(LOG_TAG, "Malformed PCT payload — skipping", e)
            null
        }
    }

    /**
     * Satisfies the startForeground() contract with a minimal notification, then tears the service
     * down immediately. Used on every bail-out path (null intent, malformed payload, honoured dismiss).
     */
    private fun promoteThenStop(): Int {
        promoteToForeground(buildMinimalForegroundNotification())
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
        return START_NOT_STICKY
    }

    private fun buildMinimalForegroundNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.small_icon)
            .setOngoing(true)
            .build()

    override fun onDestroy() {
        stopAllJobs()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun promoteToForeground(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            // API 26–33: no typed FGS; specialUse exists only from API 34 (manifest entry ignored below 34).
            ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, 0)
        }
    }

    private fun scheduleTerminalDismiss(orderId: String) {
        stopTickLoop()
        terminalDismissJob?.cancel()
        terminalDismissJob =
            serviceScope.launch {
                delay(TERMINAL_DISMISS_DELAY_MS)
                NotificationManagerCompat.from(this@OrderTrackingForegroundService)
                    .cancel(orderId, NOTIFICATION_ID)
                OrderTrackingDismissPrefs.clearDismissed(this@OrderTrackingForegroundService, orderId)
                ServiceCompat.stopForeground(this@OrderTrackingForegroundService, ServiceCompat.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startTickLoop() {
        terminalDismissJob?.cancel()
        terminalDismissJob = null
        stopTickLoop()
        tickJob =
            serviceScope.launch {
                while (isActive) {
                    delay(LIVE_UPDATE_INTERVAL_MS)
                    val bundle = moeBundle ?: break
                    val json = pctPayloadJson(bundle) ?: break
                    val payload = decodeOrderTrackingPayload(json)
                    val display =
                        computeLiveUpdateDisplay(payload, receivedAtMs, System.currentTimeMillis())
                    val notification =
                        buildOrderTrackingNotification(
                            context = this@OrderTrackingForegroundService,
                            moeBundle = bundle,
                            payload = payload,
                            chipText = display.chipText,
                            trackerPosition = display.trackerPosition,
                            receivedAtMs = receivedAtMs,
                            nowMs = System.currentTimeMillis(),
                        )
                    promoteToForeground(notification)
                    Log.d(LOG_TAG, "FGS tick stage ${payload.stage}, chip=${display.chipText}")
                    if (!shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) break
                }
                if (isActive) {
                    ServiceCompat.stopForeground(this@OrderTrackingForegroundService, ServiceCompat.STOP_FOREGROUND_DETACH)
                    stopSelf()
                }
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

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        fun startOrUpdate(context: Context, moeBundle: android.os.Bundle) {
            // Without POST_NOTIFICATIONS (Android 13+) the FGS notification is hidden, but the service
            // would still run — a stray, invisible specialUse FGS. Skip entirely instead: no permission,
            // no visible order tracking to show. (Auto-granted below API 33, so this is a no-op there.)
            if (
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(LOG_TAG, "POST_NOTIFICATIONS not granted — skipping order-tracking notification")
                return
            }
            val intent =
                Intent(context, OrderTrackingForegroundService::class.java).apply {
                    putExtras(moeBundle)
                }
            try {
                context.startForegroundService(intent)
            } catch (e: Exception) {
                // API 31+ throws ForegroundServiceStartNotAllowedException when an FGS is started from
                // the background without an exemption — e.g. the campaign push wasn't delivered
                // high-priority, or the app is in a restricted App Standby bucket. Fall back to a plain
                // notification: with promoted-ongoing it still renders as a live update; we only lose
                // the between-push tracker interpolation (the FGS tick loop).
                Log.w(LOG_TAG, "startForegroundService not allowed — falling back to notify()", e)
                postLiveNotification(context, moeBundle)
            }
        }

        /**
         * Non-FGS fallback for [startOrUpdate]: posts/updates the live notification directly.
         * On API 33+ without POST_NOTIFICATIONS this silently no-ops, matching platform behaviour.
         */
        @RequiresApi(Build.VERSION_CODES.N)
        private fun postLiveNotification(context: Context, moeBundle: android.os.Bundle) {
            val payloadJson = pctPayloadJson(moeBundle) ?: return
            val payload =
                try {
                    decodeOrderTrackingPayload(payloadJson)
                } catch (e: Exception) {
                    Log.w(LOG_TAG, "Malformed PCT payload — skipping fallback notify", e)
                    return
                }
            if (
                !payload.terminal &&
                payload.respectUserDismiss &&
                OrderTrackingDismissPrefs.isDismissed(context, payload.orderId)
            ) {
                return
            }
            OrderTrackingDismissPrefs.setDismissed(context, payload.orderId, false)
            val now = System.currentTimeMillis()
            val display = computeLiveUpdateDisplay(payload, now, now)
            val notification =
                buildOrderTrackingNotification(
                    context = context,
                    moeBundle = moeBundle,
                    payload = payload,
                    chipText = display.chipText,
                    trackerPosition = display.trackerPosition,
                    receivedAtMs = now,
                    nowMs = now,
                )
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(LOG_TAG, "Notification permission missing, cannot post the notification")
                    return
            }
            NotificationManagerCompat.from(context).notify(payload.orderId, NOTIFICATION_ID, notification)
        }

        fun stop(context: Context, orderId: String) {
            val intent =
                Intent(context, OrderTrackingForegroundService::class.java).apply {
                    action = ACTION_STOP_ORDER_TRACKING
                    putExtra(EXTRA_ORDER_ID, orderId)
                }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                // If the service isn't running there is nothing to stop; startService from the
                // background can also throw BackgroundServiceStartNotAllowedException on API 31+.
                Log.w(LOG_TAG, "stop() ignored — service not running or background start blocked", e)
            }
        }
    }
}
