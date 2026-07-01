package com.moengage.example.ordertracking.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import com.moengage.example.ordertracking.STRIP_TRACKER_DOT_COLOR_HEX
import com.moengage.example.ordertracking.model.OrderTrackingPayload

private const val STRIP_WIDTH_PX = 960
private const val STRIP_HEIGHT_PX = 200
private const val HORIZONTAL_PADDING_PX = 48f
private const val BAR_HEIGHT_PX = 28f
private const val BAR_CORNER_RADIUS_PX = 14f
private const val TRACKER_RADIUS_PX = 16f
private const val POINT_RADIUS_PX = 8f

/** Renders the progress strip as a bitmap for API 34–35 [NotificationCompat.BigPictureStyle]. */
internal fun createProgressStripBitmap(
    payload: OrderTrackingPayload,
    trackerPosition: Int,
): Bitmap {
    val bitmap = createBitmap(STRIP_WIDTH_PX, STRIP_HEIGHT_PX)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)

    val totalLength = payload.segments.sumOf { it.size }.coerceAtLeast(1)
    val barTop = (STRIP_HEIGHT_PX - BAR_HEIGHT_PX) / 2f
    val barLeft = HORIZONTAL_PADDING_PX
    val barRight = STRIP_WIDTH_PX - HORIZONTAL_PADDING_PX
    val barWidth = barRight - barLeft

    val segmentPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    val pointPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    val trackerFillPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = STRIP_TRACKER_DOT_COLOR_HEX.toColorInt()
            style = Paint.Style.FILL
        }
    val trackerStrokePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

    var segmentStartX = barLeft
    payload.segments.forEach { segment ->
        val segmentWidth = barWidth * segment.size / totalLength.toFloat()
        segmentPaint.color = segment.color.toColorInt()
        val segmentEndX = segmentStartX + segmentWidth
        val segmentRect = RectF(segmentStartX, barTop, segmentEndX, barTop + BAR_HEIGHT_PX)
        val radius = minOf(BAR_CORNER_RADIUS_PX, segmentWidth / 2f, BAR_HEIGHT_PX / 2f)
        canvas.drawRoundRect(segmentRect, radius, radius, segmentPaint)
        segmentStartX = segmentEndX
    }

    payload.points.forEach { point ->
        val x = barLeft + barWidth * point.position / totalLength.toFloat()
        val y = barTop + BAR_HEIGHT_PX / 2f
        pointPaint.color = point.color.toColorInt()
        canvas.drawCircle(x, y, POINT_RADIUS_PX, pointPaint)
    }

    val trackerX =
        barLeft + barWidth * trackerPosition.coerceIn(0, totalLength) / totalLength.toFloat()
    val trackerY = barTop + BAR_HEIGHT_PX / 2f
    canvas.drawCircle(trackerX, trackerY, TRACKER_RADIUS_PX, trackerFillPaint)
    canvas.drawCircle(trackerX, trackerY, TRACKER_RADIUS_PX, trackerStrokePaint)

    return bitmap
}
