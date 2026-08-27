package com.moengage.sampleapp.domain.model

import androidx.annotation.DrawableRes

/** The three menu tabs. `id` doubles as the `category/{categoryId}` nav argument. */
enum class MenuCategory(
    val id: String,
    /** Tab label. */
    val label: String,
    /** Section title on Menu home and the Category-list app bar. */
    val sectionTitle: String,
    /** Trailing meta on the search field. */
    val searchMeta: String,
    val searchPlaceholder: String,
) {
    Coffee(
        id = "coffee",
        label = "Coffee",
        sectionTitle = "Coffee · hot & cold",
        searchMeta = "Hot & cold",
        searchPlaceholder = "Search flat white, cold brew…",
    ),
    HerbalTeas(
        id = "teas",
        label = "Herbal teas",
        sectionTitle = "Herbal teas & infusions",
        searchMeta = "Caffeine-free",
        searchPlaceholder = "Search chamomile, tulsi ginger…",
    ),
    Food(
        id = "food",
        label = "Food",
        sectionTitle = "Bowls, bakes & snacks",
        searchMeta = "All day",
        searchPlaceholder = "Search bowls, bakes, snacks…",
    ),
    ;

    companion object {
        fun fromId(id: String?): MenuCategory = entries.firstOrNull { it.id == id } ?: Coffee
    }
}

data class MenuItem(
    val id: String,
    val name: String,
    val note: String,
    val price: Int,
    val category: MenuCategory,
    @param:DrawableRes val image: Int,
    /** Featured items appear in the 2-column grid on Menu home. */
    val featured: Boolean = false,
)

data class SizeOption(val label: String, val volume: String, val surcharge: Int)

data class MilkOption(val label: String, val surcharge: Int)

data class AddOn(val itemId: String, val label: String, val price: Int)

data class CartLine(
    val id: String,
    val itemId: String,
    val name: String,
    val options: String,
    val amount: Int,
    val quantity: Int = 1,
    @param:DrawableRes val image: Int,
)

enum class Fulfilment(val label: String, val eta: String) {
    Pickup("Pickup", "6–8 min"),
    Delivery("Delivery", "25–35 min"),
}

enum class CupPreference(val label: String, val discount: Int) {
    OwnCup("Bring my own cup", 15),
    StoreCup("Store cup", 0),
}

data class Bill(val itemTotal: Int, val taxes: Int, val couponCode: String?, val discount: Int, val cupDiscount: Int) {
    val toPay: Int get() = itemTotal + taxes - discount - cupDiscount
}

data class PaymentMethod(val id: String, val label: String, val detail: String)

enum class OrderStage(val label: String) {
    Received("Received"),
    Brewing("Brewing"),
    AtTheBar("At the bar"),
    PickedUp("Picked up"),
}

data class OrderLine(val name: String, val amount: Int)

data class Order(
    val id: String,
    val placedAt: String,
    val lines: List<OrderLine>,
    val amount: Int,
    val mode: Fulfilment,
    val stage: OrderStage,
    val readyAt: String,
    val paidVia: String,
    val active: Boolean = false,
) {
    val itemsCount: Int get() = lines.size
}

data class Coupon(
    val id: String,
    val title: String,
    /** Second line of the card. Blank when the campaign only authored a title. */
    val description: String,
    val expiry: String,
    /** Pill label from the card's BUTTON widget; null when the payload carries none, so no pill. */
    val actionLabel: String?,
    /** Filled pill when true, outlined when false. */
    val primaryAction: Boolean,
    /** Campaign-supplied image; null when the card carries no IMAGE widget. */
    val imageUrl: String? = null,
)

/**
 * The dark promo card's content on Menu home.
 *
 * App-specific by design: a self-handled in-app campaign carries author-defined JSON, and the app
 * maps that payload onto this shape (see `SelfHandledCampaignData.toPromoPayload`). Anything the
 * campaign omits falls back to [FALLBACK] so a half-configured campaign still renders.
 */
data class PromoPayload(val title: String, val subtitle: String, val deeplink: String?, val campaignId: String?) {
    companion object {
        val FALLBACK = PromoPayload(
            title = "Happy hour · 20% off cold brew",
            subtitle = "2–5 pm today. Code CHILL20.",
            deeplink = "brewbar://category/coffee",
            campaignId = null,
        )
    }
}

/** UI shape for an inbox row; hydrated either from the MoEngage inbox API or the seed. */
data class InboxMessageUi(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: String,
    val group: InboxGroup,
    val read: Boolean,
    val accent: InboxAccent,
    val deeplink: String?,
    val campaignId: String?,
)

enum class InboxGroup(val header: String) { Today("Today"), Earlier("Earlier") }

enum class InboxAccent { Brand, Star }

data class TasteProfile(
    val favouriteDrink: String,
    val milk: String,
    val sweetness: String,
    val homeStore: String,
    val birthday: String,
    val birthdayIso: String,
)

data class NotificationPreference(val key: String, val label: String, val enabled: Boolean)

/**
 * Everything Profile needs to say about location, and everything the geofence flow depends on.
 *
 * The three grants are separate permissions rather than levels: picking "Approximate" on the OS
 * prompt leaves [precise] false, and [background] is a distinct later grant that Android 11+ only
 * offers from the app's settings page. The geofence module needs [background] when
 * [backgroundRequired], [precise] otherwise — see `SessionViewModel.satisfiesGeofence`.
 */
data class LocationState(
    val approximate: Boolean = false,
    val precise: Boolean = false,
    val background: Boolean = false,
    /** False until an OS prompt has answered once, so Profile can say "Not requested". */
    val asked: Boolean = false,
    /** Android 10+: a fence needs "Allow all the time", so Profile shows that row too. */
    val backgroundRequired: Boolean = false,
    /** True while the SDK is monitoring the workspace's geofences. */
    val monitoring: Boolean = false,
)
