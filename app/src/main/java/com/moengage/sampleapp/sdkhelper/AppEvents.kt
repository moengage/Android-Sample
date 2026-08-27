package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.sampleapp.data.Store
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.Order
import timber.log.Timber

/**
 * The event dictionary: every event Brew Bar reports, with its attributes.
 * Reached through [MoEngageSDKHelper].
 */
internal object AppEvents {

    /** Event names — these must match the campaign definitions in the MoEngage dashboard. */
    object Events {
        const val MENU_VIEWED = "Menu_Viewed"
        const val CATEGORY_BROWSED = "Category_Browsed"
        const val ITEM_VIEWED = "Item_Viewed"
        const val ADD_TO_CART = "Add_To_Cart"
        const val CART_VIEWED = "Cart_Viewed"
        const val CHECKOUT_STARTED = "Checkout_Started"
        const val ORDER_PLACED = "Order_Placed"
        const val ORDER_PICKED_UP = "Order_Picked_Up"
        const val REORDER_TAPPED = "Reorder_Tapped"
        const val REWARD_REDEEMED = "Reward_Redeemed"
        const val NOTIFICATION_OPENED = "Notification_Opened"
        const val INAPP_CTA_CLICKED = "InApp_Cta_Clicked"
    }

    /** Event-attribute keys. */
    object Attrs {
        const val STORE = "store"
        const val CATEGORY = "category"
        const val ITEM = "item"
        const val PRICE = "price"
        const val SIZE = "size"
        const val MILK = "milk"
        const val ADDONS = "addons"
        const val AMOUNT = "amount"
        const val ITEMS_COUNT = "items_count"
        const val FULFILMENT = "fulfilment"
        const val COUPON = "coupon"
        const val ORDER_ID = "order_id"
        const val MODE = "mode"
        const val REWARD = "reward"
        const val CAMPAIGN_ID = "campaign_id"
        const val DEEPLINK = "deeplink"
        const val CTA = "cta"
    }

    fun trackMenuViewed(context: Context, category: MenuCategory) = track(context, Events.MENU_VIEWED) {
        addAttribute(Attrs.STORE, Store.NAME)
        addAttribute(Attrs.CATEGORY, category.label)
    }

    fun trackCategoryBrowsed(context: Context, category: MenuCategory) = track(context, Events.CATEGORY_BROWSED) {
        addAttribute(Attrs.CATEGORY, category.label)
    }

    fun trackItemViewed(context: Context, item: MenuItem) = track(context, Events.ITEM_VIEWED) {
        addAttribute(Attrs.ITEM, item.name)
        addAttribute(Attrs.PRICE, item.price)
        addAttribute(Attrs.CATEGORY, item.category.label)
    }

    fun trackAddToCart(
        context: Context,
        item: MenuItem,
        size: String,
        milk: String,
        addOns: List<String>,
        amount: Int,
    ) = track(context, Events.ADD_TO_CART) {
        addAttribute(Attrs.ITEM, item.name)
        addAttribute(Attrs.SIZE, size)
        addAttribute(Attrs.MILK, milk)
        addAttribute(Attrs.ADDONS, if (addOns.isEmpty()) "none" else addOns.joinToString(", "))
        addAttribute(Attrs.AMOUNT, amount)
    }

    fun trackCartViewed(context: Context, lines: List<CartLine>, amount: Int) = track(context, Events.CART_VIEWED) {
        addAttribute(Attrs.ITEMS_COUNT, lines.size)
        addAttribute(Attrs.AMOUNT, amount)
    }

    fun trackCheckoutStarted(context: Context, amount: Int, fulfilment: Fulfilment, coupon: String?) =
        track(context, Events.CHECKOUT_STARTED) {
            addAttribute(Attrs.AMOUNT, amount)
            addAttribute(Attrs.FULFILMENT, fulfilment.label)
            addAttribute(Attrs.COUPON, coupon ?: "none")
        }

    fun trackOrderPlaced(context: Context, order: Order) = track(context, Events.ORDER_PLACED) {
        addAttribute(Attrs.ORDER_ID, order.id)
        addAttribute(Attrs.AMOUNT, order.amount)
        addAttribute(Attrs.MODE, order.mode.label)
        addAttribute(Attrs.ITEMS_COUNT, order.itemsCount)
    }

    fun trackOrderPickedUp(context: Context, orderId: String) = track(context, Events.ORDER_PICKED_UP) {
        addAttribute(Attrs.ORDER_ID, orderId)
    }

    fun trackReorderTapped(context: Context, item: String, orderId: String) = track(context, Events.REORDER_TAPPED) {
        addAttribute(Attrs.ITEM, item)
        addAttribute(Attrs.ORDER_ID, orderId)
    }

    fun trackRewardRedeemed(context: Context, reward: String) = track(context, Events.REWARD_REDEEMED) {
        addAttribute(Attrs.REWARD, reward)
    }

    /**
     * The SDK tracks notification opens itself; this mirrors the moment into a named event so
     * the demo dashboard shows one consistent funnel.
     */
    fun trackNotificationOpened(context: Context, campaignId: String?, deeplink: String?) =
        track(context, Events.NOTIFICATION_OPENED) {
            addAttribute(Attrs.CAMPAIGN_ID, campaignId ?: "unknown")
            addAttribute(Attrs.DEEPLINK, deeplink ?: "none")
        }

    fun trackInAppCtaClicked(context: Context, campaignId: String?, cta: String) =
        track(context, Events.INAPP_CTA_CLICKED) {
            addAttribute(Attrs.CAMPAIGN_ID, campaignId ?: "unknown")
            addAttribute(Attrs.CTA, cta)
        }

    private inline fun track(context: Context, event: String, build: Properties.() -> Unit) = guarded {
        val properties = Properties().apply(build)
        MoEAnalyticsHelper.trackEvent(context, event, properties)
        Timber.d("event: %s", event)
    }
}
