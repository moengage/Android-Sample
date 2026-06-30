package com.moengage.example.ordertracking.render

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.moengage.example.ordertracking.live.effectiveEtaMs
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/**
 * Status-bar chip for API 36+ Live Updates.
 *
 * - **Countdown (stages 2–5):** [NotificationCompat.Builder.setWhen] with [effectiveEtaMs].
 *   Android shows rounded minutes and updates on minute boundaries; no app ticks for the chip.
 *   Do not call `setShowWhen(false)`; on some devices that hides chip text (icon-only chip).
 *   OEM-specific wording (same ETA): e.g. Pixel 8a `39m`, Nothing Phone 3 `in 39m`.
 * - **Static / stale:** [NotificationCompat.Builder.setShortCriticalText] (`"Placing"`, `"Soon"`, `"Done ✓"`).
 */
@RequiresApi(Build.VERSION_CODES.N)
internal fun NotificationCompat.Builder.applyLiveUpdateChip(
    payload: OrderTrackingPayload,
    chipText: String,
    receivedAtMs: Long,
    nowMs: Long,
): NotificationCompat.Builder {
    val etaMs = effectiveEtaMs(payload, receivedAtMs)
    if (etaMs != null && nowMs < etaMs) {
        return setWhen(etaMs)
    }
    return setShortCriticalText(chipText)
}
