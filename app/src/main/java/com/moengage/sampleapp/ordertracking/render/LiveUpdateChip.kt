package com.moengage.sampleapp.ordertracking.render

import androidx.core.app.NotificationCompat
import com.moengage.sampleapp.ordertracking.live.effectiveEtaMs
import com.moengage.sampleapp.ordertracking.model.OrderTrackingPayload

/**
 * Status-bar chip for API 36+ Live Updates.
 *
 * - **Countdown (mid-journey stages):** [NotificationCompat.Builder.setWhen] with [effectiveEtaMs].
 *   Android shows rounded minutes and updates on minute boundaries; no app ticks for the chip.
 *   Do not call `setShowWhen(false)`; on some devices that hides chip text (icon-only chip).
 *   OEM-specific wording (same ETA): e.g. Pixel 8a `39m`, Nothing Phone 3 `in 39m`.
 * - **Static / stale:** [NotificationCompat.Builder.setShortCriticalText] (`"Placing"`, `"Soon"`, `"Done ✓"`).
 */
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
