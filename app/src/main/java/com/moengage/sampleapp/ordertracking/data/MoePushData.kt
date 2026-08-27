package com.moengage.sampleapp.ordertracking.data

import android.os.Bundle
import com.moengage.sampleapp.ordertracking.PAYLOAD_KEY
import com.moengage.sampleapp.ordertracking.model.OrderTrackingPayload
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val payloadJson =
    Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

internal fun hasPctPayload(moeBundle: Bundle): Boolean = !moeBundle.getString(PAYLOAD_KEY).isNullOrBlank()

internal fun pctPayloadJson(moeBundle: Bundle): String? = moeBundle.getString(PAYLOAD_KEY)?.takeIf { it.isNotBlank() }

internal fun decodeOrderTrackingPayload(raw: String): OrderTrackingPayload = payloadJson.decodeFromString(raw)
