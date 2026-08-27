package com.moengage.sampleapp.ordertracking.model

import com.moengage.sampleapp.domain.model.OrderStage

/**
 * Maps the dashboard's `"stage": 1..4` onto Brew Bar's own [OrderStage], so the notification and
 * [com.moengage.sampleapp.ui.order.OrderStatusScreen] always describe the same journey.
 *
 * Positions are on the 0–3000 tracker scale the payload's segments and points share.
 */
internal val OrderStage.stageId: Int get() = ordinal + 1

/**
 * Furthest tracker position this stage may drift to before the next push lands. Used when the
 * dashboard omits `tracker_position_end`.
 */
internal val OrderStage.defaultTrackerEnd: Int
    get() = when (this) {
        OrderStage.Received -> 600
        OrderStage.Brewing -> 1800
        OrderStage.AtTheBar -> 2900
        OrderStage.PickedUp -> 3000
    }

internal fun orderStageFromId(id: Int): OrderStage? = OrderStage.entries.find { it.stageId == id }

/** Maps [OrderTrackingPayload.stage] to an [OrderStage], or null if the dashboard sent an unknown value. */
internal fun OrderTrackingPayload.orderStage(): OrderStage? = orderStageFromId(stage)
