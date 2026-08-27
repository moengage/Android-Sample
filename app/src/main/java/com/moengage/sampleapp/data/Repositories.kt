package com.moengage.sampleapp.data

import com.moengage.sampleapp.R
import com.moengage.sampleapp.data.DemoCatalogue
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.NotificationPreference
import com.moengage.sampleapp.domain.model.Order
import com.moengage.sampleapp.domain.model.OrderLine
import com.moengage.sampleapp.domain.model.OrderStage
import com.moengage.sampleapp.domain.model.PaymentMethod
import com.moengage.sampleapp.domain.model.TasteProfile

/** Store the demo user is checked into. */
object Store {
    const val NAME = "Indiranagar"
    const val ADDRESS = "Indiranagar · 12th Main"
    const val HOURS = "Pickup in 6–8 min · open till 11 pm"
    const val PICKUP_LINE = "Indiranagar · pickup 6–8 min"
    const val SUMMARY_LINE = "Indiranagar 12th Main"
}

/** The signed-in demo persona. */
object DemoUser {
    const val ID = "brewbar-john-001"
    const val NAME = "John Doe"
    const val FIRST_NAME = "John"
    const val LAST_NAME = "Doe"
    const val PHONE = "+91 12345 67890"
    const val PHONE_E164 = "+911234567890"
    const val EMAIL = "john.doe@example.com"
    const val INITIALS = "JD"
    const val TIER = "Gold cup"
    const val OTP = "4816"

    val taste = TasteProfile(
        favouriteDrink = "Flat white",
        milk = "Oat",
        sweetness = "1 sugar",
        homeStore = Store.NAME,
        birthday = "14 Mar",
        birthdayIso = "1994-03-14T00:00:00.000Z",
    )
}

object MenuRepository {
    fun featured(category: MenuCategory): List<MenuItem> = DemoCatalogue.featured(category)
    fun catalogue(category: MenuCategory): List<MenuItem> = DemoCatalogue.byCategory(category)
    fun item(id: String): MenuItem = DemoCatalogue.byId(id)

    val filters = listOf("Popular", "Under ₹200", "Vegan")

    /** The "your usual" card on Menu home. */
    val usual = UsualOrder(
        summary = "Flat white · oat · medium",
        detail = "Ordered 14 times · ₹240",
        itemId = "flat-white",
    )

    data class UsualOrder(val summary: String, val detail: String, val itemId: String)
}

object CartRepository {
    /** Two seeded lines so Cart / Payment demo without walking the whole funnel. */
    fun seedLines(): List<CartLine> = listOf(
        CartLine(
            id = "line-1",
            itemId = "flat-white",
            name = "Flat white · medium",
            options = "Oat milk · 1 sugar",
            amount = 270,
            image = R.drawable.cs_hot_coffee,
        ),
        CartLine(
            id = "line-2",
            itemId = "berry-chia-bowl",
            name = "Berry chia yogurt bowl",
            options = "No honey",
            amount = 290,
            image = R.drawable.cs_yogurt_bowl,
        ),
    )

    const val COUPON_CODE = "CHILL20"
    const val COUPON_PERCENT = 20
    const val TAX_RATE = 5 // 5% of item total, rounded — ₹560 → ₹28
}

object PaymentRepository {
    val methods = listOf(
        PaymentMethod("wallet", "Brew Bar wallet", "₹1,240"),
    )
    const val DEFAULT_METHOD = "wallet"
}

object OrderRepository {
    private val seed = listOf(
        Order(
            id = "BB-4821",
            placedAt = "Today, 8:34 am",
            lines = listOf(
                OrderLine("Flat white · medium · oat", 270),
                OrderLine("Berry chia yogurt bowl", 290),
            ),
            amount = 476,
            mode = Fulfilment.Pickup,
            stage = OrderStage.Brewing,
            readyAt = "ready ~ 8:42 am",
            paidVia = "Paid via wallet",
            active = true,
        ),
        Order(
            id = "BB-4780",
            placedAt = "Sat, 9:12 am",
            lines = listOf(
                OrderLine("Cold brew · large", 260),
                OrderLine("Almond biscotti", 110),
            ),
            amount = 350,
            mode = Fulfilment.Pickup,
            stage = OrderStage.PickedUp,
            readyAt = "collected 9:24 am",
            paidVia = "Paid via UPI",
        ),
        Order(
            id = "BB-4712",
            placedAt = "Thu, 5:40 pm",
            lines = listOf(
                OrderLine("Tulsi ginger tea", 140),
                OrderLine("Masala cheese croissant", 180),
            ),
            amount = 320,
            mode = Fulfilment.Delivery,
            stage = OrderStage.PickedUp,
            readyAt = "delivered 6:08 pm",
            paidVia = "Paid via wallet",
        ),
    )

    fun all(): List<Order> = seed
    fun byId(id: String): Order = seed.firstOrNull { it.id == id } ?: seed.first()
    fun latest(): Order = seed.first()

    const val HEADER_META = "28 orders · 14 flat whites"
    val filters = listOf("All", "Pickup", "Delivery")
    const val SUBSCRIPTION_NUDGE = "Subscribe to a daily 8 am flat white and save 15%."
}

object ProfileRepository {
    val notificationPreferences = listOf(
        NotificationPreference("order_updates", "Notifications", enabled = true),
    )
}
