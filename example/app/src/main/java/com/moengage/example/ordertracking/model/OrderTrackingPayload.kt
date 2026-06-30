package com.moengage.example.ordertracking.model

import com.moengage.example.ordertracking.DEFAULT_STALE_CHIP_TEXT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** One coloured section of the progress bar (length is relative, not pixels). */
@Serializable
internal data class OrderTrackingSegment(
    @SerialName("color") val color: String,
    @SerialName("size") val size: Int,
)

/** A milestone dot on the progress bar at a fixed position on the 0–3000 scale. */
@Serializable
internal data class OrderTrackingPoint(
    @SerialName("color") val color: String,
    @SerialName("position") val position: Int,
)

/**
 * Full order-tracking payload from the MoEngage dashboard key `pct_payload`.
 * Same JSON is used on all Android versions; optional fields control local countdown and dismiss behaviour.
 */
@Serializable
internal data class OrderTrackingPayload(
    @SerialName("template") val template: String,
    @SerialName("order_id") val orderId: String,
    @SerialName("stage") val stage: Int,
    @SerialName("title") val title: String,
    @SerialName("message") val message: String,
    @SerialName("chip_text") val chipText: String = "",
    @SerialName("tracker_position") val trackerPosition: Int,
    @SerialName("tracker_position_end") val trackerPositionEnd: Int? = null,
    @SerialName("eta_epoch_ms") val etaEpochMs: Long? = null,
    @SerialName("stale_chip_text") val staleChipText: String = DEFAULT_STALE_CHIP_TEXT,
    @SerialName("respect_user_dismiss") val respectUserDismiss: Boolean = false,
    @SerialName("styled_by_progress") val styledByProgress: Boolean = true,
    @SerialName("terminal") val terminal: Boolean = false,
    @SerialName("segments") val segments: List<OrderTrackingSegment>,
    @SerialName("points") val points: List<OrderTrackingPoint>,
    @SerialName("tracker_icon") val trackerIcon: String,
    @SerialName("start_icon") val startIcon: String,
    @SerialName("end_icon") val endIcon: String,
)
