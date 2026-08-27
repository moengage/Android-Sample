package com.moengage.sampleapp.ordertracking.live

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ordertracking.ACTION_STOP_ORDER_TRACKING
import com.moengage.sampleapp.ordertracking.CHANNEL_ID
import com.moengage.sampleapp.ordertracking.EXTRA_ORDER_ID
import com.moengage.sampleapp.ordertracking.LIVE_UPDATE_INTERVAL_MS
import com.moengage.sampleapp.ordertracking.NOTIFICATION_ID
import com.moengage.sampleapp.ordertracking.TERMINAL_DISMISS_DELAY_MS
import com.moengage.sampleapp.ordertracking.data.decodeOrderTrackingPayload
import com.moengage.sampleapp.ordertracking.data.pctPayloadJson
import com.moengage.sampleapp.ordertracking.model.OrderTrackingPayload
import com.moengage.sampleapp.ordertracking.model.orderStage
import com.moengage.sampleapp.ordertracking.notification.OrderTrackingDismissPrefs
import com.moengage.sampleapp.ordertracking.render.buildOrderTrackingNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

/** Foreground service for local chip/tracker ticks between MoEngage stage pushes. */
internal class OrderTrackingForegroundService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var tickJob: Job? = null
    private var terminalDismissJob: Job? = null
    private var activeOrderId: String? = null
    private var moeBundle: Bundle? = null
    private var receivedAtMs: Long = 0L

    override fun onBind(intent: Intent?): IBinder? = null

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
            Timber.d("Respecting dismiss — skip notify, orderId=%s", payload.orderId)
            return promoteThenStop()
        }

        OrderTrackingDismissPrefs.setDismissed(this, payload.orderId, false)
        activeOrderId = payload.orderId
        moeBundle = pushExtras
        receivedAtMs = System.currentTimeMillis()

        Timber.d(
            "Stage %s, orderId=%s, terminal=%s",
            payload.orderStage()?.name ?: payload.stage,
            payload.orderId,
            payload.terminal,
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
        Timber.d("FGS stage %d, orderId=%s, chip=%s", payload.stage, payload.orderId, display.chipText)

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
    private fun parsePayload(pushExtras: Bundle): OrderTrackingPayload? {
        val payloadJson = pctPayloadJson(pushExtras) ?: return null
        return try {
            decodeOrderTrackingPayload(payloadJson)
        } catch (e: Exception) {
            Timber.w(e, "Malformed PCT payload — skipping")
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

    private fun buildMinimalForegroundNotification(): Notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_small)
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
                    .cancel(NOTIFICATION_ID)
                OrderTrackingDismissPrefs.clearDismissed(this@OrderTrackingForegroundService, orderId)
                ServiceCompat.stopForeground(
                    this@OrderTrackingForegroundService,
                    ServiceCompat.STOP_FOREGROUND_REMOVE,
                )
                stopSelf()
            }
    }

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
                    Timber.d("FGS tick stage %d, chip=%s", payload.stage, display.chipText)
                    if (!shouldScheduleLiveUpdate(payload, display.stale, receivedAtMs)) break
                }
                if (isActive) {
                    ServiceCompat.stopForeground(
                        this@OrderTrackingForegroundService,
                        ServiceCompat.STOP_FOREGROUND_DETACH,
                    )
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
        fun startOrUpdate(context: Context, moeBundle: Bundle) {
            // Without POST_NOTIFICATIONS (Android 13+) the FGS notification is hidden, but the service
            // would still run — a stray, invisible specialUse FGS. Skip entirely instead: no permission,
            // no visible order tracking to show. (Auto-granted below API 33, so this is a no-op there.)
            if (
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                Timber.w("POST_NOTIFICATIONS not granted — skipping order-tracking notification")
                return
            }
            // API 24-25 predate startForegroundService(); post the notification directly instead of
            // skipping these devices entirely. They lose only the between-push tracker interpolation.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                postLiveNotification(context, moeBundle)
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
                Timber.w(e, "startForegroundService not allowed — falling back to notify()")
                postLiveNotification(context, moeBundle)
            }
        }

        /**
         * Non-FGS fallback for [startOrUpdate]: posts/updates the live notification directly.
         * On API 33+ without POST_NOTIFICATIONS this silently no-ops, matching platform behaviour.
         */
        private fun postLiveNotification(context: Context, moeBundle: Bundle) {
            val payloadJson = pctPayloadJson(moeBundle) ?: return
            val payload =
                try {
                    decodeOrderTrackingPayload(payloadJson)
                } catch (e: Exception) {
                    Timber.w(e, "Malformed PCT payload — skipping fallback notify")
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
            if (
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                Timber.w("Notification permission missing, cannot post the notification")
                return
            }
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
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
                Timber.w(e, "stop() ignored — service not running or background start blocked")
            }
        }
    }
}
