package com.moengage.example.ordertracking

/** Chip and tracker values to apply when re-posting the notification. */
internal data class LiveUpdateDisplayState(
    val chipText: String,
    val trackerPosition: Int,
    val stale: Boolean,
)

/**
 * Furthest tracker position allowed for this stage before the next push.
 */
private fun defaultTrackerEnd(payload: OrderTrackingPayload): Int {
    payload.trackerPositionEnd?.let { return it }
    return payload.orderStage()?.defaultTrackerEnd ?: payload.trackerPosition
}

/**
 * When the countdown should hit zero: explicit [OrderTrackingPayload.etaEpochMs], or
 * push time plus minutes parsed from chip text (e.g. "12 min").
 */
private fun effectiveEtaMs(payload: OrderTrackingPayload, receivedAtMs: Long): Long? {
    payload.etaEpochMs?.let { return it }
    val minutes = payload.chipText.filter { it.isDigit() }.toIntOrNull()
    if (minutes != null && minutes > 0) {
        return receivedAtMs + minutes * MS_PER_MINUTE
    }
    return null
}

/** True when background ticks should continue (countdown stage, not terminal, not stale). */
internal fun shouldScheduleLiveUpdate(
    payload: OrderTrackingPayload,
    stale: Boolean,
    receivedAtMs: Long,
): Boolean {
    if (payload.terminal || stale) return false
    return effectiveEtaMs(payload, receivedAtMs) != null
}

/**
 * Builds chip text and tracker position for the current time.
 * Returns stale state when ETA passed and no next push arrived.
 */
internal fun computeLiveUpdateDisplay(
    payload: OrderTrackingPayload,
    receivedAtMs: Long,
    nowMs: Long,
): LiveUpdateDisplayState {
    val trackerStart = payload.trackerPosition
    val trackerEnd = defaultTrackerEnd(payload)
    val etaMs = effectiveEtaMs(payload, receivedAtMs)

    if (etaMs != null && nowMs >= etaMs) {
        return LiveUpdateDisplayState(
            chipText = payload.staleChipText,
            trackerPosition = trackerEnd,
            stale = true,
        )
    }

    val chipText =
        if (etaMs != null) {
            val minutes = ((etaMs - nowMs + CHIP_MINUTE_ROUND_UP_MS) / MS_PER_MINUTE).toInt().coerceAtLeast(1)
            "$minutes min"
        } else {
            payload.chipText
        }

    val trackerPosition =
        if (trackerEnd <= trackerStart || etaMs == null) {
            trackerStart
        } else {
            val total = (etaMs - receivedAtMs).coerceAtLeast(1)
            val elapsed = (nowMs - receivedAtMs).coerceIn(0, total)
            trackerStart + ((trackerEnd - trackerStart) * elapsed / total).toInt()
        }

    return LiveUpdateDisplayState(chipText = chipText, trackerPosition = trackerPosition, stale = false)
}
