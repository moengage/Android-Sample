package com.moengage.sampleapp.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.MenuRepository
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.domain.model.PromoPayload
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.FeaturedCard
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.SectionHeader
import com.moengage.sampleapp.ui.inapp.SelfHandledPromoCard
import com.moengage.sampleapp.ui.nav.PromoState
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 4 — Menu home.
 *
 * MoEngage moments:
 * - `Menu_Viewed` on arrival and `Category_Browsed` on every tab switch.
 * - The native in-app modal is requested once per session ~900 ms after arrival.
 * - The dark card at the top of the body is a **self-handled** in-app campaign.
 */
@Composable
fun MenuHomeScreen(
    category: MenuCategory,
    unreadCount: Int,
    promo: PromoState?,
    onCategorySelected: (MenuCategory) -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onFullMenu: () -> Unit,
    onReorderUsual: () -> Unit,
    onInboxClick: () -> Unit,
    onPromoClick: () -> Unit,
    onPromoDismiss: () -> Unit,
    onDemoTools: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.PageBackground),
    ) {
        MenuHeader(
            category = category,
            unreadCount = unreadCount,
            onCategorySelected = onCategorySelected,
            onInboxClick = onInboxClick,
            onGreetingLongPress = onDemoTools,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                top = Space.x18,
                bottom = Sizes.bottomNavClearance,
            ),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalArrangement = Arrangement.spacedBy(Space.x12),
        ) {
            // The grid gap is 12 between cards; sections sit 22 apart, so the section-leading
            // composables carry the extra 10 rather than a spacer item.
            if (promo != null) {
                fullWidth {
                    SelfHandledPromoCard(
                        payload = promo.payload,
                        onClick = onPromoClick,
                        onDismiss = onPromoDismiss,
                    )
                }
            }
            fullWidth {
                SectionHeader(
                    title = category.sectionTitle,
                    actionLabel = "Full menu",
                    onAction = onFullMenu,
                    modifier = if (promo != null) Modifier.padding(top = Space.x10) else Modifier,
                )
            }
            items(MenuRepository.featured(category), key = { it.id }) { item ->
                FeaturedCard(item, onClick = { onItemClick(item) })
            }
            fullWidth { UsualCard(onReorderUsual, Modifier.padding(top = Space.x10)) }
        }
    }
}

/** Spans a grid row — used for the promo, headers and the two full-width cards. */
private fun androidx.compose.foundation.lazy.grid.LazyGridScope.fullWidth(content: @Composable () -> Unit) =
    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) { content() }

@Composable
private fun UsualCard(onReorder: () -> Unit, modifier: Modifier = Modifier) {
    BrewCard(modifier = modifier, contentPadding = PaddingValues(Space.x14)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconTile(Icons.Filled.LocalCafe)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    MenuRepository.usual.summary,
                    style = BrewType.bodyMedium,
                    color = BrewColors.TextPrimary,
                )
                Text(
                    MenuRepository.usual.detail,
                    style = BrewType.caption,
                    color = BrewColors.TextSecondary,
                )
            }
            Text(
                "Reorder",
                style = BrewType.supportMedium,
                color = BrewColors.Link,
                modifier = Modifier.clickable(onClick = onReorder),
            )
        }
    }
}

@Composable
private fun MenuHomePreview() {
    BrewBarTheme {
        MenuHomeScreen(
            category = MenuCategory.Coffee,
            unreadCount = 3,
            promo = PromoState(PromoPayload.FALLBACK, null),
            onCategorySelected = {},
            onItemClick = {},
            onFullMenu = {},
            onReorderUsual = {},
            onInboxClick = {},
            onPromoClick = {},
            onPromoDismiss = {},
            onDemoTools = {},
        )
    }
}
