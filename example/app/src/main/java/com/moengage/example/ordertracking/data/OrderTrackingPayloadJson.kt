package com.moengage.example.ordertracking.data

import android.os.Bundle
import com.moengage.example.ordertracking.PAYLOAD_KEY
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.json.JSONObject

@OptIn(ExperimentalSerializationApi::class)
private val payloadJson =
    Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

/** Raw `pct_payload` JSON string from an incoming MoEngage push [Bundle], if present. */
internal fun pctPayloadJson(moeBundle: Bundle): String? =
    moeBundle.getString(PAYLOAD_KEY)?.takeIf { it.isNotBlank() }

/** Raw `pct_payload` JSON string from a saved `{orderId}.session.json` object. */
internal fun pctPayloadJson(sessionJson: JSONObject): String? =
    sessionJson.optString(PAYLOAD_KEY).takeIf { it.isNotBlank() }

/** Parses the dashboard `pct_payload` JSON string into an [OrderTrackingPayload]. */
internal fun decodeOrderTrackingPayload(raw: String): OrderTrackingPayload =
    payloadJson.decodeFromString(raw)
