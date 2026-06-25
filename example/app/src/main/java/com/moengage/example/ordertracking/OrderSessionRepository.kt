package com.moengage.example.ordertracking

/**
 * Persists order session under [Context.getFilesDir]: MoEngage push [Bundle] (includes `pct_payload`)
 * plus small metadata (receive time, dismiss flag).
 */

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class OrderSessionRepository(context: Context) {

    private val appContext = context.applicationContext
    private val sessionsDir = File(appContext.filesDir, SESSIONS_DIR_NAME)

    /** Saves the MoEngage push bundle and stage receive time; keeps the current dismiss flag. */
    suspend fun saveSession(
        orderId: String,
        moeBundle: Bundle,
        receivedAtMs: Long,
    ) = withContext(Dispatchers.IO) {
        writeBundle(orderId, moeBundle)
        val dismissed = readMeta(orderId)?.dismissed ?: false
        writeMeta(orderId, SessionMeta(receivedAtMs, dismissed))
    }

    /** Parses [OrderTrackingPayload] from the saved bundle's `pct_payload` key. */
    suspend fun getPayload(orderId: String): OrderTrackingPayload? =
        withContext(Dispatchers.IO) {
            payloadFromBundle(readBundle(orderId))
        }

    suspend fun getReceivedAtMs(orderId: String): Long =
        withContext(Dispatchers.IO) {
            readMeta(orderId)?.receivedAtMs ?: System.currentTimeMillis()
        }

    /** Original MoEngage push bundle for [com.moengage.pushbase.MoEPushHelper.logNotificationClick]. */
    suspend fun getMoeBundle(orderId: String): Bundle? =
        withContext(Dispatchers.IO) {
            readBundle(orderId)
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
            bundleFile(orderId).delete()
            metaFile(orderId).delete()
        }

    private fun bundleFile(orderId: String): File = File(sessionsDir, "$orderId.bundle")

    private fun metaFile(orderId: String): File = File(sessionsDir, "$orderId.meta")

    private fun writeBundle(orderId: String, bundle: Bundle) {
        sessionsDir.mkdirs()
        val parcel = Parcel.obtain()
        try {
            bundle.writeToParcel(parcel, 0)
            bundleFile(orderId).writeBytes(parcel.marshall())
        } finally {
            parcel.recycle()
        }
    }

    private fun readBundle(orderId: String): Bundle? {
        val bytes = bundleFile(orderId).takeIf { it.exists() }?.readBytes() ?: return null
        val parcel = Parcel.obtain()
        return try {
            parcel.unmarshall(bytes, 0, bytes.size)
            parcel.setDataPosition(0)
            Bundle.CREATOR.createFromParcel(parcel)
        } catch (_: Exception) {
            null
        } finally {
            parcel.recycle()
        }
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

    private fun payloadFromBundle(bundle: Bundle?): OrderTrackingPayload? {
        val raw = bundle?.getString(PAYLOAD_KEY)?.takeIf { it.isNotBlank() } ?: return null
        return runCatching { decodeOrderTrackingPayload(raw) }.getOrNull()
    }

    private data class SessionMeta(
        val receivedAtMs: Long,
        val dismissed: Boolean,
    )
}
