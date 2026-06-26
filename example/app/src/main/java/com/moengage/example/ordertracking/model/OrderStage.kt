package com.moengage.example.ordertracking.model

import com.moengage.example.ordertracking.PROGRESS_BAR_MAX

/**
 * Food-delivery order stages mapped from dashboard JSON field `"stage": 1..6`.
 * JSON keeps [id] as int; app logic uses [defaultTrackerEnd] when JSON omits `tracker_position_end`.
 */
internal enum class OrderStage(
    val id: Int,
    val defaultTrackerEnd: Int,
) {
    PLACED(1, 0),
    PREPARING(2, 600),
    RIDER_ASSIGNED(3, 1600),
    PICKED_UP(4, 2750),
    APPROACHING(5, 2950),
    DELIVERED(6, PROGRESS_BAR_MAX),
    ;

    companion object {
        fun fromId(id: Int): OrderStage? = entries.find { it.id == id }
    }
}

/** Maps [OrderTrackingPayload.stage] to [OrderStage], or null if the dashboard sent an unknown value. */
internal fun OrderTrackingPayload.orderStage(): OrderStage? = OrderStage.fromId(stage)
