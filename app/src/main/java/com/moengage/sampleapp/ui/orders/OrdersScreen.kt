package com.moengage.sampleapp.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.OrderRepository
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.Order
import com.moengage.sampleapp.ui.components.BrewAppBar
import com.moengage.sampleapp.ui.components.FilterPill
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.StatusPill
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * Screen 13 — order history.
 *
 * MoEngage moment: `Reorder_Tapped` (item, order_id) when a past order is sent back to the cart.
 */
@Composable
fun OrdersScreen(
    orders: List<Order>,
    onBack: () -> Unit,
    onTrack: (Order) -> Unit,
    onReorder: (Order) -> Unit,
    onSubscribe: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activeFilter by remember { mutableStateOf(OrderRepository.filters.first()) }
    val visible = when (activeFilter) {
        "Pickup" -> orders.filter { it.mode == Fulfilment.Pickup }
        "Delivery" -> orders.filter { it.mode == Fulfilment.Delivery }
        else -> orders
    }

    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        BrewAppBar(
            title = "Your orders",
            subtitle = OrderRepository.HEADER_META,
            onBack = onBack,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrewColors.Surface)
                .padding(horizontal = Sizes.screenPadding, vertical = Space.x12),
            horizontalArrangement = Arrangement.spacedBy(Space.x8),
        ) {
            OrderRepository.filters.forEach { filter ->
                FilterPill(filter, filter == activeFilter, { activeFilter = filter })
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Sizes.screenPadding,
                end = Sizes.screenPadding,
                top = Space.x16,
                bottom = Sizes.bottomNavClearance,
            ),
            verticalArrangement = Arrangement.spacedBy(Space.x12),
        ) {
            items(visible, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    onTrack = { onTrack(order) },
                    onReorder = { onReorder(order) },
                )
            }
            item(key = "subscription-nudge") { SubscriptionNudge(onSubscribe) }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onTrack: () -> Unit, onReorder: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(androidx.compose.foundation.layout.IntrinsicSize.Min)
            .clip(BrewShapes.card)
            .background(BrewColors.Surface)
            .border(1.dp, BrewColors.BorderSubtle, BrewShapes.card),
    ) {
        if (order.active) {
            Box(
                Modifier
                    .width(Sizes.unreadStripe)
                    .fillMaxHeight()
                    .background(BrewColors.Primary),
            )
        }
        Column(
            modifier = Modifier.padding(Space.x14),
            verticalArrangement = Arrangement.spacedBy(Space.x10),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.x8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "#${order.id} · ${order.placedAt}",
                    style = BrewType.bodyMedium,
                    color = BrewColors.TextPrimary,
                    modifier = Modifier.weight(1f),
                )
                if (order.active) StatusPill("Brewing")
            }
            Text(
                order.lines.joinToString(", ") { it.name },
                style = BrewType.support,
                color = BrewColors.TextSecondary,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(rupees(order.amount), style = BrewType.subtitleBold, color = BrewColors.TextPrimary)
                if (order.active) {
                    OrderAction("Track", filled = false, onClick = onTrack)
                } else {
                    OrderAction("Reorder", filled = true, onClick = onReorder)
                }
            }
        }
    }
}

@Composable
private fun OrderAction(label: String, filled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(BrewShapes.chip)
            .background(if (filled) BrewColors.Primary else BrewColors.Surface)
            .then(
                if (filled) {
                    Modifier
                } else {
                    Modifier.border(1.dp, BrewColors.BorderDefault, BrewShapes.chip)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x14, vertical = Space.x6),
    ) {
        Text(
            label,
            style = BrewType.captionMedium,
            color = if (filled) BrewColors.OnDarkPrimary else BrewColors.TextPrimary,
        )
    }
}

/** Dashed-border nudge card at the end of the list. */
@Composable
private fun SubscriptionNudge(onSetUp: () -> Unit) {
    val dash = remember { PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = BrewColors.BorderDefault,
                    cornerRadius = CornerRadius(14.dp.toPx()),
                    style = Stroke(width = 1.dp.toPx(), pathEffect = dash),
                )
            }
            .padding(Space.x14),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(Icons.Filled.FreeBreakfast, size = 40.dp)
        Text(
            OrderRepository.SUBSCRIPTION_NUDGE,
            style = BrewType.support,
            color = BrewColors.TextSecondary,
            modifier = Modifier.weight(1f),
        )
        Text(
            "Set up",
            style = BrewType.captionMedium,
            color = BrewColors.Link,
            modifier = Modifier.clickable(onClick = onSetUp),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun OrdersPreview() {
    BrewBarTheme {
        OrdersScreen(
            orders = OrderRepository.all(),
            onBack = {},
            onTrack = {},
            onReorder = {},
            onSubscribe = {},
        )
    }
}
