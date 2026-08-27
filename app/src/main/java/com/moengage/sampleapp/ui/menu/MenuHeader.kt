package com.moengage.sampleapp.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.DemoUser
import com.moengage.sampleapp.data.Store
import com.moengage.sampleapp.domain.model.MenuCategory
import com.moengage.sampleapp.ui.components.TabPill
import com.moengage.sampleapp.ui.components.ThinDivider
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/** The white header block of screen 4: greeting, store strip, search field and tabs. */
@Composable
fun MenuHeader(
    category: MenuCategory,
    unreadCount: Int,
    onCategorySelected: (MenuCategory) -> Unit,
    onInboxClick: () -> Unit,
    onGreetingLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrewColors.Surface),
    ) {
        Column(
            modifier = Modifier.padding(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                top = Space.x18,
                bottom = Space.x14,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x14),
        ) {
            GreetingRow(unreadCount, onInboxClick, onGreetingLongPress)
            StoreStrip()
            SearchField(category)
            Row(horizontalArrangement = Arrangement.spacedBy(Space.x8)) {
                MenuCategory.entries.forEach { entry ->
                    TabPill(
                        label = entry.label,
                        selected = entry == category,
                        onClick = { onCategorySelected(entry) },
                    )
                }
            }
        }
        ThinDivider()
    }
}

@Composable
private fun GreetingRow(unreadCount: Int, onInboxClick: () -> Unit, onLongPress: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.androidLongPress(onLongPress),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("Good morning,", style = BrewType.caption, color = BrewColors.TextSecondary)
            Text(DemoUser.NAME, style = BrewType.titleBold, color = BrewColors.TextPrimary)
        }
        BellButton(unreadCount, onInboxClick)
    }
}

/** 38 dp circle with a 17 dp `#D3453F` badge carrying a 2px white ring. */
@Composable
private fun BellButton(unreadCount: Int, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd) {
        Box(
            modifier = Modifier
                .size(Sizes.bellButton)
                .clip(BrewShapes.pill)
                .background(BrewColors.ComponentBg2)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = BrewColors.TextPrimary,
                modifier = Modifier.size(19.dp),
            )
        }
        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .offset(x = 3.dp, y = (-3).dp)
                    .size(Sizes.badge + 4.dp)
                    .clip(BrewShapes.pill)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(Sizes.badge)
                        .clip(BrewShapes.pill)
                        .background(BrewColors.UnreadBadge),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        unreadCount.toString(),
                        style = BrewType.microMedium,
                        color = BrewColors.OnDarkPrimary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreStrip() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(BrewShapes.button)
            .background(BrewColors.NeutralFill)
            .padding(horizontal = Space.x12, vertical = Space.x10),
        horizontalArrangement = Arrangement.spacedBy(Space.x10),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.Storefront,
            contentDescription = null,
            tint = BrewColors.Primary,
            modifier = Modifier.size(20.dp),
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(Store.ADDRESS, style = BrewType.supportMedium, color = BrewColors.TextPrimary)
            Text(Store.HOURS, style = BrewType.micro, color = BrewColors.TextSecondary)
        }
        Text("Change", style = BrewType.captionMedium, color = BrewColors.Link)
    }
}

/** 46 dp radius-12 `#E5E5E5` field; placeholder and trailing meta both follow the tab. */
@Composable
private fun SearchField(category: MenuCategory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Sizes.searchHeight)
            .clip(BrewShapes.button)
            .background(BrewColors.ComponentBg2)
            .padding(horizontal = Space.x14),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            category.searchPlaceholder,
            style = BrewType.body,
            color = BrewColors.TextTertiary,
        )
        Text(category.searchMeta, style = BrewType.caption, color = BrewColors.TextSecondary)
    }
}

/** Long-press the greeting to open DemoTools — the same hidden affordance as the app bar. */
private fun Modifier.androidLongPress(onLongPress: () -> Unit): Modifier =
    this.pointerInput(Unit) { detectTapGestures(onLongPress = { onLongPress() }) }

@Preview(showBackground = true, widthDp = 412)
@Composable
private fun MenuHeaderPreview() {
    BrewBarTheme {
        MenuHeader(
            category = MenuCategory.Coffee,
            unreadCount = 3,
            onCategorySelected = {},
            onInboxClick = {},
            onGreetingLongPress = {},
        )
    }
}
