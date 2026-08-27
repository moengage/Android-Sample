package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.cards.core.MoECardHelper
import com.moengage.cards.core.model.Card

/**
 * Cards (`cards-core`) — the feed behind the Self-handled cards screen's coupons. Reached through
 * [MoEngageSDKHelper].
 *
 * The section lifecycle matters here in a way it does not for the inbox: the SDK only syncs,
 * counts delivery and ages cards out if it is told when the section comes and goes. Every
 * screen showing cards must therefore pair [onSectionLoaded] with [onSectionUnloaded], and
 * report [cardShown] / [cardClicked] for the cards it actually renders.
 */
internal object MoEngageCards {

    /**
     * Announces that the cards section is on screen. The SDK syncs with the server, reports
     * delivery, and calls back once it has settled — the point at which [fetchCards] has fresh
     * data. `hasUpdates` is true when that sync actually changed the local store.
     */
    fun onSectionLoaded(context: Context, onSyncComplete: (Boolean) -> Unit) = guarded {
        MoECardHelper.onCardSectionLoaded(context) { data ->
            onSyncComplete(data?.hasUpdates == true)
        }
    }

    /** Counterpart to [onSectionLoaded]; call it when the section leaves the screen. */
    fun onSectionUnloaded(context: Context) = guarded {
        MoECardHelper.onCardSectionUnloaded(context)
    }

    /** Every card the workspace has delivered, across all categories. */
    fun fetchCards(context: Context, onResult: (List<Card>) -> Unit) = guarded {
        MoECardHelper.fetchCards(context) { cardData -> onResult(cardData?.cards.orEmpty()) }
    }

    /** The cards in one dashboard-defined category. */
    fun fetchCards(context: Context, category: String, onResult: (List<Card>) -> Unit) = guarded {
        MoECardHelper.getCardsForCategoryAsync(context, category) { cardData ->
            onResult(cardData?.cards.orEmpty())
        }
    }

    /** Forces a server sync rather than reading the local store — the pull-to-refresh path. */
    fun refresh(context: Context, onSyncComplete: (Boolean) -> Unit) = guarded {
        MoECardHelper.refreshCards(context) { data -> onSyncComplete(data?.hasUpdates == true) }
    }

    /** Impression. Must be reported per card actually rendered, or campaign stats go wrong. */
    fun cardShown(context: Context, card: Card) = guarded {
        MoECardHelper.cardShown(context, card)
    }

    /** Click, attributed to the widget the user actually tapped. */
    fun cardClicked(context: Context, card: Card, widgetId: Int) = guarded {
        MoECardHelper.cardClicked(context, card, widgetId)
    }
}
