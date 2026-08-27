package com.moengage.sampleapp.data

import com.moengage.sampleapp.R
import com.moengage.sampleapp.domain.model.AddOn
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.MilkOption
import com.moengage.sampleapp.domain.model.SizeOption

/**
 * The whole catalogue. There is no network layer —
 * this is a demo app for the MoEngage SDK, not a real storefront.
 */
object DemoCatalogue {

    val items: List<MenuItem> = listOf(
        // ── Coffee ──────────────────────────────────────────────────────────────────
        MenuItem(
            id = "flat-white",
            name = "Flat white",
            note = "Double ristretto, steamed milk, thin microfoam",
            price = 220,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_hot_coffee,
            featured = true,
        ),
        MenuItem(
            id = "cappuccino",
            name = "Cappuccino",
            note = "Equal parts espresso, milk and foam · cocoa dust",
            price = 210,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_hot_coffee,
        ),
        MenuItem(
            id = "masala-cortado",
            name = "Masala cortado",
            note = "Espresso cut with cardamom-clove milk",
            price = 190,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_hot_coffee,
        ),
        MenuItem(
            id = "cold-brew",
            name = "Cold brew",
            note = "18-hour steep, served black over ice",
            price = 240,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_cold_coffee,
            featured = true,
        ),
        MenuItem(
            id = "iced-latte",
            name = "Iced latte",
            note = "Espresso, chilled milk, choice of syrup",
            price = 230,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_cold_coffee,
        ),
        MenuItem(
            id = "espresso-tonic",
            name = "Espresso tonic",
            note = "Single origin over tonic and orange peel",
            price = 250,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_cold_coffee,
        ),
        MenuItem(
            id = "filter-coffee",
            name = "Filter coffee",
            note = "South Indian filter decoction, frothed",
            price = 120,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_hot_coffee,
            featured = true,
        ),
        MenuItem(
            id = "vietnamese-cold",
            name = "Vietnamese cold",
            note = "Dark roast over condensed milk and ice",
            price = 260,
            category = MenuCategory.Coffee,
            image = R.drawable.cs_cold_coffee,
            featured = true,
        ),

        // ── Herbal teas ─────────────────────────────────────────────────────────────
        MenuItem(
            id = "chamomile",
            name = "Chamomile",
            note = "Whole flowers, slow steeped · caffeine-free",
            price = 150,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
            featured = true,
        ),
        MenuItem(
            id = "tulsi-ginger",
            name = "Tulsi ginger",
            note = "Holy basil with fresh ginger and a twist of lime",
            price = 140,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
            featured = true,
        ),
        MenuItem(
            id = "hibiscus-mint",
            name = "Hibiscus mint cooler",
            note = "Tart hibiscus, garden mint, served chilled",
            price = 160,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
            featured = true,
        ),
        MenuItem(
            id = "blue-pea-lemongrass",
            name = "Blue pea lemongrass",
            note = "Butterfly pea flower and lemongrass infusion",
            price = 170,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
            featured = true,
        ),
        MenuItem(
            id = "rooibos-vanilla",
            name = "Rooibos vanilla",
            note = "Red bush with vanilla pod · naturally sweet",
            price = 165,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
        ),
        MenuItem(
            id = "kashmiri-kahwa",
            name = "Kashmiri kahwa",
            note = "Saffron, almond and cardamom green tea",
            price = 180,
            category = MenuCategory.HerbalTeas,
            image = R.drawable.cs_herbal_tea,
        ),

        // ── Food ────────────────────────────────────────────────────────────────────
        MenuItem(
            id = "greek-yogurt-bowl",
            name = "Greek yogurt & granola bowl",
            note = "Thick curd, honey granola, seasonal fruit",
            price = 280,
            category = MenuCategory.Food,
            image = R.drawable.cs_yogurt_bowl,
            featured = true,
        ),
        MenuItem(
            id = "berry-chia-bowl",
            name = "Berry chia yogurt bowl",
            note = "Overnight chia, mixed berries, toasted coconut",
            price = 290,
            category = MenuCategory.Food,
            image = R.drawable.cs_yogurt_bowl,
            featured = true,
        ),
        MenuItem(
            id = "savoury-yogurt-bowl",
            name = "Savoury yogurt bowl",
            note = "Hung curd, cucumber, dukkah and olive oil",
            price = 270,
            category = MenuCategory.Food,
            image = R.drawable.cs_yogurt_bowl,
        ),
        MenuItem(
            id = "butter-biscuits",
            name = "Butter biscuits",
            note = "Bakery classic, baked twice daily",
            price = 90,
            category = MenuCategory.Food,
            image = R.drawable.cs_snacks,
        ),
        MenuItem(
            id = "almond-biscotti",
            name = "Almond biscotti",
            note = "Crisp, twice-baked · made for dunking",
            price = 110,
            category = MenuCategory.Food,
            image = R.drawable.cs_snacks,
            featured = true,
        ),
        MenuItem(
            id = "banana-walnut-cake",
            name = "Banana walnut cake",
            note = "Slow-baked loaf, dense and moist",
            price = 160,
            category = MenuCategory.Food,
            image = R.drawable.cs_snacks,
        ),
        MenuItem(
            id = "masala-croissant",
            name = "Masala cheese croissant",
            note = "Laminated overnight, spiced cheddar filling",
            price = 180,
            category = MenuCategory.Food,
            image = R.drawable.cs_snacks,
            featured = true,
        ),
        MenuItem(
            id = "egg-kejriwal",
            name = "Egg kejriwal sandwich",
            note = "Chilli cheese toast, runny yolk, sourdough",
            price = 300,
            category = MenuCategory.Food,
            image = R.drawable.cs_snacks,
        ),
    )

    val sizes = listOf(
        SizeOption("Small", "180 ml", 0),
        SizeOption("Medium", "240 ml", 20),
        SizeOption("Large", "330 ml", 40),
    )

    val milks = listOf(
        MilkOption("Dairy", 0),
        MilkOption("Oat", 30),
        MilkOption("Almond", 30),
    )

    val addOns = listOf(
        AddOn("almond-biscotti", "Almond biscotti", 110),
        AddOn("greek-yogurt-bowl", "Greek yogurt & granola bowl", 280),
    )

    fun byId(id: String): MenuItem = items.firstOrNull { it.id == id } ?: items.first()

    fun byCategory(category: MenuCategory): List<MenuItem> = items.filter { it.category == category }

    fun featured(category: MenuCategory): List<MenuItem> = byCategory(category).filter { it.featured }.take(4)
}
