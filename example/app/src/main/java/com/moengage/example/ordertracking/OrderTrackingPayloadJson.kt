package com.moengage.example.ordertracking

/**
 * JSON decode for [OrderTrackingPayload] from the dashboard `pct_payload` string inside the
 * MoEngage push bundle.
 */

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val payloadJson =
    Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

/** Parses the raw JSON string from the MoEngage custom key into an [OrderTrackingPayload]. */
internal fun decodeOrderTrackingPayload(raw: String): OrderTrackingPayload = payloadJson.decodeFromString(raw)
