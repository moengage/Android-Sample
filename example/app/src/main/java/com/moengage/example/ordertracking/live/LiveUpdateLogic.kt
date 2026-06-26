package com.moengage.example.ordertracking.live

import com.moengage.example.ordertracking.CHIP_MINUTE_ROUND_UP_MS
import com.moengage.example.ordertracking.MS_PER_MINUTE
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import com.moengage.example.ordertracking.model.orderStage

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
 * When the countdown should hit zero: prefer [OrderTrackingPayload.etaEpochMs]; otherwise
 * [OrderTrackingPayload.chipText] only when it matches `"N min"` (food-delivery convenience).
 * Non-minute chips (e.g. ride-hailing OTP `"4421"`, status `"Placing"`) do not start a countdown.
 */
private fun effectiveEtaMs(payload: OrderTrackingPayload, receivedAtMs: Long): Long? {
    payload.etaEpochMs?.let { return it }
    val minutes = minutesFromMinuteChip(payload.chipText) ?: return null
    return receivedAtMs + minutes * MS_PER_MINUTE
}

private val minuteChipPattern = Regex("""^(\d+)\s*min$""", RegexOption.IGNORE_CASE)

private fun minutesFromMinuteChip(chipText: String): Int? {
    val minutes = minuteChipPattern.matchEntire(chipText.trim())?.groupValues?.get(1)?.toIntOrNull()
    return minutes?.takeIf { it > 0 }
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
