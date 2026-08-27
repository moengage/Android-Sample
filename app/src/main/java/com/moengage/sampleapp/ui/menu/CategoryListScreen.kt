package com.moengage.sampleapp.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moengage.sampleapp.data.MenuRepository
import com.moengage.sampleapp.data.Store
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.ui.components.BrewAppBar
import com.moengage.sampleapp.ui.components.FilterPill
import com.moengage.sampleapp.ui.components.MenuRow
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 5 — the full list for the active tab.
 *
 * MoEngage moment: `Category_Browsed` fires on arrival (and on every tab switch upstream).
 */
@Composable
fun CategoryListScreen(
    category: MenuCategory,
    onBack: () -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onAdd: (MenuItem) -> Unit,
    onDemoTools: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activeFilter by remember { mutableStateOf(MenuRepository.filters.first()) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.PageBackground),
    ) {
        BrewAppBar(
            title = category.sectionTitle,
            subtitle = Store.PICKUP_LINE,
            onBack = onBack,
            onTitleLongPress = onDemoTools,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrewColors.Surface)
                .padding(horizontal = Sizes.screenPadding, vertical = Space.x12),
            horizontalArrangement = Arrangement.spacedBy(Space.x8),
        ) {
            MenuRepository.filters.forEach { filter ->
                FilterPill(
                    label = filter,
                    selected = filter == activeFilter,
                    onClick = { activeFilter = filter },
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                top = Space.x16,
                bottom = Space.x28,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x12),
        ) {
            items(MenuRepository.catalogue(category), key = { it.id }) { item ->
                MenuRow(item, onClick = { onItemClick(item) }, onAdd = { onAdd(item) })
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun CategoryListPreview() {
    BrewBarTheme {
        CategoryListScreen(
            category = MenuCategory.Coffee,
            onBack = {},
            onItemClick = {},
            onAdd = {},
            onDemoTools = {},
        )
    }
}
