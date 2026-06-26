package com.moengage.example.ordertracking.data

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.SESSIONS_DIR_NAME
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

/**
 * Persists order session on disk: `{orderId}.session.json` (MoEngage push string keys) and
 * `{orderId}.meta` (receive time + dismiss flag).
 *
 * Ticks/re-render read `pct_payload` straight from JSON; click logging alone rebuilds a [Bundle].
 */
internal class OrderSessionRepository(context: Context) {

    private val sessionsDir = File(context.applicationContext.filesDir, SESSIONS_DIR_NAME)

    /** Saves the full MoEngage push [Bundle] (string keys) and stage receive time; keeps dismiss flag. */
    suspend fun saveSession(
        orderId: String,
        moeBundle: Bundle,
        receivedAtMs: Long,
    ) = withContext(Dispatchers.IO) {
        writePushPayload(orderId, moeBundle)
        val dismissed = readMeta(orderId)?.dismissed ?: false
        writeMeta(orderId, SessionMeta(receivedAtMs, dismissed))
    }

    /** Parses [OrderTrackingPayload] from the saved session file's `pct_payload` key. */
    suspend fun getPayload(orderId: String): OrderTrackingPayload? =
        withContext(Dispatchers.IO) {
            readSessionJson(orderId)?.let(::decodeSavedPayload)
        }

    /** Stage receive time — anchor for `"N min"` chip fallback and tracker interpolation. */
    suspend fun getReceivedAtMs(orderId: String): Long =
        withContext(Dispatchers.IO) {
            readMeta(orderId)?.receivedAtMs ?: System.currentTimeMillis()
        }

    /** Full MoEngage push [Bundle] for [com.moengage.pushbase.MoEPushHelper.logNotificationClick]. */
    suspend fun getMoeBundle(orderId: String): Bundle? =
        withContext(Dispatchers.IO) {
            readSessionJson(orderId)?.let(::bundleFromJson)
        }

    suspend fun isDismissed(orderId: String): Boolean =
        withContext(Dispatchers.IO) {
            readMeta(orderId)?.dismissed ?: false
        }

    suspend fun setDismissed(orderId: String, dismissed: Boolean) =
        withContext(Dispatchers.IO) {
            val receivedAtMs = readMeta(orderId)?.receivedAtMs ?: System.currentTimeMillis()
            writeMeta(orderId, SessionMeta(receivedAtMs, dismissed))
        }

    suspend fun clearSession(orderId: String) =
        withContext(Dispatchers.IO) {
            sessionFile(orderId).delete()
            metaFile(orderId).delete()
        }

    private fun sessionFile(orderId: String): File = File(sessionsDir, "$orderId.session.json")

    private fun metaFile(orderId: String): File = File(sessionsDir, "$orderId.meta")

    private fun writePushPayload(orderId: String, bundle: Bundle) {
        sessionsDir.mkdirs()
        sessionFile(orderId).writeText(pushPayloadToJson(bundle).toString())
    }

    private fun readSessionJson(orderId: String): JSONObject? {
        val file = sessionFile(orderId)
        if (!file.exists()) return null
        return runCatching { JSONObject(file.readText()) }
            .onFailure { error ->
                Log.w(LOG_TAG, "Corrupt saved push session — orderId=$orderId", error)
            }
            .getOrNull()
    }

    private fun pushPayloadToJson(bundle: Bundle): JSONObject =
        JSONObject().apply {
            bundle.keySet().forEach { key ->
                bundle.getString(key)?.let { value -> put(key, value) }
            }
        }

    private fun bundleFromJson(json: JSONObject): Bundle =
        Bundle().apply {
            json.keys().forEach { key -> putString(key, json.getString(key)) }
        }

    private fun decodeSavedPayload(json: JSONObject): OrderTrackingPayload? {
        val raw = pctPayloadJson(json) ?: return null
        return runCatching { decodeOrderTrackingPayload(raw) }
            .onFailure { error ->
                Log.w(LOG_TAG, "Corrupt saved pct_payload — treating session as missing", error)
            }
            .getOrNull()
    }

    private fun writeMeta(orderId: String, meta: SessionMeta) {
        sessionsDir.mkdirs()
        metaFile(orderId).writeText("${meta.receivedAtMs}\n${meta.dismissed}")
    }

    private fun readMeta(orderId: String): SessionMeta? {
        val lines = metaFile(orderId).takeIf { it.exists() }?.readLines() ?: return null
        if (lines.size < 2) return null
        val receivedAtMs = lines[0].toLongOrNull() ?: return null
        val dismissed = lines[1].toBooleanStrictOrNull() ?: false
        return SessionMeta(receivedAtMs, dismissed)
    }

    private data class SessionMeta(
        val receivedAtMs: Long,
        val dismissed: Boolean,
    )
}

private var orderSessionRepositoryInstance: OrderSessionRepository? = null

/** Application-scoped session store — one instance per process. */
internal fun orderSessionRepository(context: Context): OrderSessionRepository =
    orderSessionRepositoryInstance
        ?: OrderSessionRepository(context.applicationContext).also {
            orderSessionRepositoryInstance = it
        }
