package com.moengage.sampleapp.ui.selfhandledcards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moengage.sampleapp.domain.model.Coupon
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SelfHandledCardsUiState(
    val coupons: List<Coupon> = emptyList(),
    /** True once the cards API has answered, so the UI can say where the list came from. */
    val fromSdk: Boolean = false,
)

/**
 * Screen 12's coupon list, sourced from MoEngage Cards and rendered by the app.
 *
 * The list is empty until a card campaign has actually landed on the device, which is why
 * `SelfHandledCardsScreen` hides its "Ready to use" section on an empty list rather than showing a
 * placeholder.
 */
class SelfHandledCardsViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SelfHandledCardsUiState())
    val state: StateFlow<SelfHandledCardsUiState> = _state.asStateFlow()

    /** Mapped cards keyed by the coupon id the UI hands back on a tap. */
    private var couponCards: Map<String, CouponCard> = emptyMap()

    /** Cards already reported as shown, so a re-fetch does not double-count the impression. */
    private val reportedShown = mutableSetOf<String>()

    private val context get() = getApplication<Application>().applicationContext

    /**
     * The screen has appeared. Announcing the section drives the SDK's sync and delivery reporting;
     * the local store is read straight away as well, so a returning user sees their coupons
     * immediately instead of waiting on the network.
     */
    fun onScreenEntered() {
        MoEngageSDKHelper.onCardSectionLoaded(context) { load() }
        load()
    }

    /** The screen has gone away. Required counterpart to [onScreenEntered]. */
    fun onScreenLeft() = MoEngageSDKHelper.onCardSectionUnloaded(context)

    /**
     * The CTA pill was tapped. Reported against the CTA's own widget id, which is what the SDK
     * attributes a button click to, and returns the deep link to follow.
     *
     * The pill only exists on a card that has a CTA, so [CouponCard.ctaWidgetId] is present on
     * every path that reaches here; the guard is for safety, not an expected case. Falls back to
     * the container's deep link when the CTA carries no navigation of its own.
     */
    fun onRedeem(coupon: Coupon): String? {
        val couponCard = couponCards[coupon.id] ?: return null
        couponCard.ctaWidgetId?.let { widgetId ->
            MoEngageSDKHelper.cardClicked(context, couponCard.card, widgetId)
        }
        return couponCard.ctaDeeplink ?: couponCard.cardDeeplink
    }

    private fun load() {
        MoEngageSDKHelper.fetchCards(context) { cards ->
            val mapped = cards.mapNotNull { it.toCouponCard() }
            couponCards = mapped.associateBy { it.coupon.id }
            _state.value = SelfHandledCardsUiState(coupons = mapped.map { it.coupon }, fromSdk = true)
            // The screen composes every coupon in one scrolling column, so anything mapped here
            // is on screen. A per-row visibility hook would be the stricter reading.
            mapped.forEach(::reportShown)
        }
    }

    private fun reportShown(couponCard: CouponCard) {
        if (!reportedShown.add(couponCard.coupon.id)) return
        MoEngageSDKHelper.cardShown(context, couponCard.card)
    }
}
