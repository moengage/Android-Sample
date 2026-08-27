package com.moengage.sampleapp.ui.nav

/** The 13 destinations of the nav graph, plus the deep-link mapping used by push campaigns. */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val PERMISSION = "permission"
    const val MENU = "menu"
    const val CATEGORY = "category/{categoryId}"
    const val ITEM = "item/{itemId}"
    const val CART = "cart"
    const val PAYMENT = "payment"
    const val STATUS = "status/{orderId}"
    const val PROFILE = "profile"
    const val INBOX = "inbox"

    /**
     * The Self-handled cards screen. The route token stays `rewards`: it is the `brewbar://rewards`
     * deep link campaigns are configured against and the in-app context reported to the SDK, so
     * renaming it would break dashboard targeting. Only the app-facing name changed.
     */
    const val REWARDS = "rewards"
    const val ORDERS = "orders"

    fun category(categoryId: String) = "category/$categoryId"
    fun item(itemId: String) = "item/$itemId"
    fun status(orderId: String) = "status/$orderId"

    /**
     * Campaign key/value pairs and `brewbar://` deep links both resolve through here:
     * `order_status` → `status/{orderId}`, `category` → `category/{id}`, `rewards` → `rewards`.
     */
    fun fromDeeplink(link: String): String? {
        val path = link
            .substringAfter("brewbar://", link)
            .substringAfter("://")
            .trim('/')
            .ifBlank { return null }
        val segments = path.split('/')
        return when (segments.first()) {
            "order_status", "status" -> status(segments.getOrNull(1) ?: "BB-4821")
            "category" -> category(segments.getOrNull(1) ?: "coffee")
            "item" -> segments.getOrNull(1)?.let { item(it) }
            "rewards" -> REWARDS
            "orders" -> ORDERS
            "inbox" -> INBOX
            "profile" -> PROFILE
            "cart" -> CART
            "menu" -> MENU
            else -> null
        }
    }

    /** The in-app "context" reported to the SDK for each destination. */
    fun inAppContext(route: String?): String = when {
        route == null -> "menu"
        route.startsWith("category") -> "category"
        route.startsWith("item") -> "item"
        route.startsWith("status") -> "order_status"
        else -> route
    }
}
