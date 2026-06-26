package com.moengage.example.ordertracking.render

private val SEGMENT_EMOJIS = listOf("🟧", "🟨", "🟦", "🔵", "🟩")
private const val EMPTY_SEGMENT_EMOJI = "⚪"

/** Emoji segment line for API 31–33 BigText fallback (e.g. `🟧🟨🟦⚪⚪`). */
internal fun emojiProgressLine(stage: Int, segmentCount: Int): String {
    if (segmentCount <= 0) return ""
    val filledCount = stage.coerceIn(0, segmentCount)
    return buildString {
        repeat(segmentCount) { index ->
            append(
                if (index < filledCount) {
                    SEGMENT_EMOJIS.getOrElse(index) { SEGMENT_EMOJIS.last() }
                } else {
                    EMPTY_SEGMENT_EMOJI
                },
            )
        }
    }
}
