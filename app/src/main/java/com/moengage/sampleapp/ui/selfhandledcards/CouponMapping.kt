package com.moengage.sampleapp.ui.selfhandledcards

import androidx.core.text.HtmlCompat
import com.moengage.cards.core.model.Card
import com.moengage.cards.core.model.Widget
import com.moengage.cards.core.model.action.Action
import com.moengage.cards.core.model.action.NavigationAction
import com.moengage.cards.core.model.enums.WidgetType
import com.moengage.sampleapp.domain.model.Coupon

// Widget ids of the basic template. A card's role for each widget is carried by its id, not by its
// position in the list — these are the same ids the SDK's own BasicViewHolder switches on.
private const val WIDGET_ID_IMAGE = 0
private const val WIDGET_ID_TITLE = 1
private const val WIDGET_ID_DESCRIPTION = 2
private const val WIDGET_ID_CTA = 3

private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 60L * 60L
private const val SECONDS_PER_DAY = 24L * 60L * 60L

/**
 * A coupon plus the bits of the originating [Card] the screen has to report back on.
 *
 * The UI only ever sees [coupon]. The rest exists so a tap can be attributed to the exact widget
 * the user pressed, which is what `cardClicked` expects.
 */
internal data class CouponCard(
    val coupon: Coupon,
    val card: Card,
    /** Id of the CTA widget, for click attribution. Null when the card carries no CTA. */
    val ctaWidgetId: Int?,
    /** Deep link on the CTA widget — what the SDK follows when the button itself is tapped. */
    val ctaDeeplink: String?,
    /** Deep link on the container — what the SDK follows when the card body is tapped. */
    val cardDeeplink: String?,
)

/**
 * Maps a MoEngage card onto the app's own [Coupon], following the same contract as the SDK's
 * `BasicViewHolder` so this screen and the SDK's own card list read a payload identically.
 *
 * A card is one [com.moengage.cards.core.model.Template] of containers of widgets. The basic
 * template puts everything in the **first container**, and each widget's role comes from its
 * **id paired with its type**, not from its position:
 *
 * | id | type   | role                                  |
 * |----|--------|---------------------------------------|
 * | 0  | IMAGE  | artwork                               |
 * | 1  | TEXT   | title                                 |
 * | 2  | TEXT   | description — omitted when empty      |
 * | 3  | BUTTON | CTA label + action — omitted when empty |
 *
 * Empty content means "do not show that part", matching the SDK, which skips the widget rather
 * than rendering a blank. Returns `null` when there is no usable title, since a coupon row with
 * nothing to say should drop out rather than render blank.
 */
internal fun Card.toCouponCard(nowSeconds: Long = System.currentTimeMillis() / 1000): CouponCard? {
    // BasicViewHolder reads containers[0]; firstOrNull rather than [0] so a malformed payload
    // drops the card instead of throwing on the render path.
    val container = template.containers.firstOrNull() ?: return null

    var imageUrl: String? = null
    var title: String? = null
    var description = ""
    var cta: Widget? = null

    for (widget in container.widgetList) {
        when {
            widget.id == WIDGET_ID_IMAGE && widget.widgetType == WidgetType.IMAGE ->
                imageUrl = widget.content.takeIf { it.isNotEmpty() }

            widget.id == WIDGET_ID_TITLE && widget.widgetType == WidgetType.TEXT ->
                title = widget.content

            widget.id == WIDGET_ID_DESCRIPTION && widget.widgetType == WidgetType.TEXT ->
                description = widget.content

            widget.id == WIDGET_ID_CTA && widget.widgetType == WidgetType.BUTTON ->
                cta = widget.takeIf { it.content.isNotEmpty() }
        }
    }

    val resolvedTitle = title?.takeIf { it.isNotBlank() } ?: return null

    return CouponCard(
        coupon = Coupon(
            id = cardId,
            title = resolvedTitle.fromCardHtml(),
            description = description.fromCardHtml(),
            expiry = expiryLabel(metaData.deletionTime, nowSeconds),
            // Exactly what the campaign authored on the CTA widget; null when there is no usable
            // CTA, so the card renders without a pill rather than inventing a label.
            actionLabel = cta?.content?.fromCardHtml(),
            // A pinned card is the workspace saying "this is the hero offer", which is what the
            // filled pill means in this design.
            primaryAction = metaData.isPinned,
            imageUrl = imageUrl,
        ),
        card = this,
        ctaWidgetId = cta?.id,
        ctaDeeplink = cta?.actionList.navigationValue(),
        cardDeeplink = container.action.navigationValue(),
    )
}

/** First usable navigation target in an action list, or null when it carries none. */
private fun List<Action>?.navigationValue(): String? = this.orEmpty()
    .filterIsInstance<NavigationAction>()
    .firstOrNull { it.value.isNotBlank() }
    ?.value

/** Card text is authored in a rich-text field, so it can arrive with HTML markup. */
private fun String.fromCardHtml(): String =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim()

/**
 * Expiry comes from `metaData.deletionTime`, not `displayControl.expireAt`: the SDK has already
 * resolved the three expiry modes (absolute, after-delivered, after-seen) into that one field.
 * It is epoch **seconds**, and `-1` means the card never expires.
 */
private fun expiryLabel(deletionTimeSeconds: Long, nowSeconds: Long): String {
    if (deletionTimeSeconds <= 0L) return "No expiry"
    val remaining = deletionTimeSeconds - nowSeconds
    return when {
        remaining <= 0L -> "Expired"
        remaining < SECONDS_PER_HOUR -> "Expires in ${remaining / SECONDS_PER_MINUTE} min"
        remaining < SECONDS_PER_DAY -> "Expires in ${plural(remaining / SECONDS_PER_HOUR, "hour")}"
        else -> "Expires in ${plural(remaining / SECONDS_PER_DAY, "day")}"
    }
}

private fun plural(count: Long, unit: String): String = if (count == 1L) "1 $unit" else "$count ${unit}s"
