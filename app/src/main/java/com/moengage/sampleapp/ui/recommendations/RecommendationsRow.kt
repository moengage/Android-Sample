package com.moengage.sampleapp.ui.recommendations

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.moengage.sampleapp.domain.model.RecommendedProduct
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.ImageBanner
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * "Recommendations for you" — the strip between the search field and the category tabs.
 *
 * `horizontalScroll` not `LazyRow`: this app reserves `Lazy*` for lists that can grow. Emits
 * nothing until a fetch answers, so it appears late and pushes the tabs down — revisit once a
 * real network call is behind it.
 */
@Composable
fun RecommendationsRow(
    products: List<RecommendedProduct>,
    fetched: Boolean,
    onProductClick: (RecommendedProduct) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!fetched) return

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Space.x10)) {
        Text("Recommendations for you", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        if (products.isEmpty()) {
            EmptyLine()
        } else {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Space.x12),
            ) {
                products.forEach { product ->
                    RecommendationCard(
                        product = product,
                        // Null when the recommendation is not in this catalogue: better an
                        // unclickable card than one that opens an unrelated product.
                        onClick = if (product.itemId.isNotBlank()) {
                            { onProductClick(product) }
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }
}

/** Nothing found, or the fetch failed — one line, because this sits in the fixed header. */
@Composable
private fun EmptyLine() {
    Text(
        "No recommendations right now.",
        style = BrewType.caption,
        color = BrewColors.TextSecondary,
    )
}

@Composable
private fun RecommendationCard(product: RecommendedProduct, onClick: (() -> Unit)?) {
    BrewCard(modifier = Modifier.width(Sizes.recommendationCardWidth), onClick = onClick) {
        ImageBanner(product.image, Sizes.recommendationImageHeight)
        Column(
            modifier = Modifier.padding(
                start = Space.x12,
                end = Space.x12,
                top = Space.x10,
                bottom = Space.x12,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x4),
        ) {
            Text(
                product.name,
                style = BrewType.bodyMedium,
                color = BrewColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (product.description.isNotBlank()) {
                Text(
                    product.description,
                    style = BrewType.micro,
                    color = BrewColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(rupees(product.price), style = BrewType.bodyBold, color = BrewColors.TextPrimary)
        }
    }
}

@Preview(showBackground = true, widthDp = 412)
@Composable
private fun RecommendationsRowPreview() {
    BrewBarTheme {
        Column(
            modifier = Modifier.background(BrewColors.Surface).padding(PaddingValues(Sizes.screenPadding)),
        ) {
            RecommendationsRow(
                products = PREVIEW_RECOMMENDATIONS,
                fetched = true,
                onProductClick = {},
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, name = "Recommendations · empty")
@Composable
private fun RecommendationsRowEmptyPreview() {
    BrewBarTheme {
        Column(
            modifier = Modifier.background(BrewColors.Surface).padding(PaddingValues(Sizes.screenPadding)),
        ) {
            RecommendationsRow(products = emptyList(), fetched = true, onProductClick = {})
        }
    }
}
