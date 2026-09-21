package com.moengage.sampleapp.ui.recommendations

import com.moengage.sampleapp.R
import com.moengage.sampleapp.data.DemoCatalogue
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.RecommendedProduct
import kotlin.math.roundToInt

/** Dashboard linkage for the recommendations row. Placeholders until the campaign is authored. */
internal object RecommendationsContract {

    const val RECOMMENDATION_ID = "brewbar_home_recommendations"

    /** A matching [DemoCatalogue] ID lets the product borrow that item's artwork and detail screen. */
    const val KEY_ITEM_ID = "item_id"

    const val KEY_NAME = "name"
    const val KEY_DESCRIPTION = "description"
    const val KEY_PRICE = "price"

    /** `coffee` | `teas` | `food`, matching [MenuCategory.id]. Only picks artwork. */
    const val KEY_CATEGORY = "category"

    /** Sent verbatim and hashed into the SDK's cache key: whitespace matters, order does not. */
    val INCLUDED_FIELDS = setOf(KEY_ITEM_ID, KEY_NAME, KEY_DESCRIPTION, KEY_PRICE, KEY_CATEGORY)
}

/** Maps the SDK's schema-less response onto Brew Bar's products. Unusable items are skipped. */
internal fun List<Map<String, Any>>.toRecommendedProducts(): List<RecommendedProduct> =
    mapNotNull { item -> item.toRecommendedProduct() }

private fun Map<String, Any>.toRecommendedProduct(): RecommendedProduct? {
    val itemId = string(RecommendationsContract.KEY_ITEM_ID).orEmpty()
    // Not DemoCatalogue.byId: it falls back to the first item, dressing an unknown
    // recommendation up as a flat white.
    val catalogueItem = DemoCatalogue.items.firstOrNull { it.id == itemId }

    val name = string(RecommendationsContract.KEY_NAME) ?: catalogueItem?.name ?: return null
    val price = int(RecommendationsContract.KEY_PRICE) ?: catalogueItem?.price ?: return null

    return RecommendedProduct(
        // Only a resolved catalogue ID, so an unknown recommendation cannot deep-link to an
        // unrelated product. Blank means "not in this catalogue" — the card renders, unclickable.
        itemId = catalogueItem?.id.orEmpty(),
        name = name,
        description = string(RecommendationsContract.KEY_DESCRIPTION)
            ?: catalogueItem?.note.orEmpty(),
        price = price,
        image = imageFor(catalogueItem, string(RecommendationsContract.KEY_CATEGORY)),
    )
}

private fun imageFor(catalogueItem: MenuItem?, category: String?): Int = catalogueItem?.image
    ?: CATEGORY_IMAGES[category?.trim()?.lowercase()]
    ?: R.drawable.cs_hot_coffee

private val CATEGORY_IMAGES: Map<String, Int> = mapOf(
    MenuCategory.Coffee.id to R.drawable.cs_hot_coffee,
    MenuCategory.HerbalTeas.id to R.drawable.cs_herbal_tea,
    MenuCategory.Food.id to R.drawable.cs_snacks,
)

private fun Map<String, Any>.string(key: String): String? = (this[key] as? String)?.trim()?.takeIf { it.isNotBlank() }

/**
 * The SDK types values from the JSON literal: `220` is an `Int`, `220.0` a `Double`, `"220"` a
 * `String`. A plain `as? Int` loses three of those, and truncating would drop a rupee.
 */
private fun Map<String, Any>.int(key: String): Int? = when (val value = this[key]) {
    is Number -> value.toDouble().roundToInt()
    is String -> value.trim().toDoubleOrNull()?.roundToInt()
    else -> null
}

/** Sample products for the `@Preview` composables only. */
internal val PREVIEW_RECOMMENDATIONS: List<RecommendedProduct> = listOf(
    "cold-brew",
    "masala-croissant",
    "tulsi-ginger",
    "almond-biscotti",
    "iced-latte",
    "berry-chia-bowl",
).mapNotNull { id -> DemoCatalogue.items.firstOrNull { it.id == id } }
    .map { item ->
        RecommendedProduct(
            itemId = item.id,
            name = item.name,
            description = item.note,
            price = item.price,
            image = item.image,
        )
    }
