package com.moengage.example.ordertracking.live

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.moengage.example.ordertracking.ACTION_STOP_ORDER_TRACKING
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.LIVE_UPDATE_INTERVAL_MS
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.TERMINAL_DISMISS_DELAY_MS
import com.moengage.example.ordertracking.data.decodeOrderTrackingPayload
import com.moengage.example.ordertracking.data.pctPayloadJson
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
            val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return START_NOT_STICKY
            if (orderId == activeOrderId) {
                stopAllJobs()
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            return START_NOT_STICKY
        }

        val pushExtras = intent?.extras ?: return START_NOT_STICKY
        val payloadJson = pctPayloadJson(pushExtras) ?: return START_NOT_STICKY
        val payload = decodeOrderTrackingPayload(payloadJson)

        if (
            !payload.terminal &&
            OrderTrackingDismissPrefs.isDismissed(this, payload.orderId) &&
            payload.respectUserDismiss
        ) {
            Log.d(LOG_TAG, "Respecting dismiss — skip notify, orderId=${payload.orderId}")
            stopSelf()
            return START_NOT_STICKY
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
        return START_STICKY
    }

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
            val intent =
                Intent(context, OrderTrackingForegroundService::class.java).apply {
                    putExtras(moeBundle)
                }
            context.startForegroundService(intent)
        }

        fun stop(context: Context, orderId: String) {
            val intent =
                Intent(context, OrderTrackingForegroundService::class.java).apply {
                    action = ACTION_STOP_ORDER_TRACKING
                    putExtra(EXTRA_ORDER_ID, orderId)
                }
            context.startService(intent)
        }
    }
}
