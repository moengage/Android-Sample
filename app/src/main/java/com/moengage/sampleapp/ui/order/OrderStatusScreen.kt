package com.moengage.sampleapp.ui.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.OrderRepository
import com.moengage.sampleapp.domain.model.Order
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.DetailRow
import com.moengage.sampleapp.ui.components.FooterBar
import com.moengage.sampleapp.ui.components.FooterButtonRow
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.components.ProgressSteps
import com.moengage.sampleapp.ui.components.SecondaryButton
import com.moengage.sampleapp.ui.components.ThinDivider
import com.moengage.sampleapp.ui.nav.SimulatedPush
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * Screen 9 — order status.
 *
 * MoEngage moments: `Order_Placed` was fired on the way in; a push campaign targeted at this
 * moment lands while the screen is open and deep-links back to `status/{orderId}`.
 * `Order_Picked_Up` fires when the order reaches the final step.
 */
@Composable
fun OrderStatusScreen(
    order: Order,
    simulatedPush: SimulatedPush?,
    onMyOrders: () -> Unit,
    onBackToMenu: () -> Unit,
    onPushTapped: (SimulatedPush) -> Unit,
    onPushDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        Column(Modifier.fillMaxSize()) {
            StatusHeader(order)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(Sizes.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Space.x16),
            ) {
                ProgressCard(order)
                ItemsCard(order)
            }
            FooterBar {
                FooterButtonRow {
                    SecondaryButton("My orders", onMyOrders, Modifier.weight(1f))
                    PrimaryButton("Back to menu", onBackToMenu, Modifier.weight(1f))
                }
            }
        }

        // Slides in from -120% over 300 ms with cubic-bezier(.2,.8,.2,1).
        AnimatedVisibility(
            visible = simulatedPush != null,
            enter = slideInVertically(
                animationSpec = tween(300, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)),
                initialOffsetY = { full -> -(full * 12) / 10 },
            ),
            exit = slideOutVertically(
                animationSpec = tween(300, easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)),
                targetOffsetY = { full -> -(full * 12) / 10 },
            ),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            simulatedPush?.let { push ->
                PushShade(
                    push = push,
                    onClick = { onPushTapped(push) },
                    onDismiss = onPushDismissed,
                )
            }
        }
    }
}

@Composable
private fun StatusHeader(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrewColors.PrimaryLightTint)
            .padding(start = Sizes.screenPadding, end = Sizes.screenPadding, top = 32.dp, bottom = Space.x20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Space.x12),
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.statusCircle)
                .clip(BrewShapes.pill)
                .background(BrewColors.Primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = BrewColors.OnDarkPrimary,
                modifier = Modifier.size(30.dp),
            )
        }
        Text("Order placed", style = BrewType.heroHeader, color = BrewColors.TextPrimary)
        Text(
            "#${order.id} · show this at the bar",
            style = BrewType.support,
            color = BrewColors.TextSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ProgressCard(order: Order) {
    BrewCard(contentPadding = PaddingValues(Space.x16)) {
        Column(verticalArrangement = Arrangement.spacedBy(Space.x14)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Space.x10),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.Timer,
                    contentDescription = null,
                    tint = BrewColors.Primary,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    "Grinding & brewing",
                    style = BrewType.subtitleMedium,
                    color = BrewColors.TextPrimary,
                    modifier = Modifier.weight(1f),
                )
                Text(order.readyAt, style = BrewType.caption, color = BrewColors.TextSecondary)
            }
            ProgressSteps(order.stage)
        }
    }
}

@Composable
private fun ItemsCard(order: Order) {
    BrewCard {
        order.lines.forEachIndexed { index, line ->
            if (index > 0) ThinDivider()
            DetailRow(
                label = line.name,
                value = rupees(line.amount),
                labelColor = BrewColors.TextPrimary,
                modifier = Modifier.padding(horizontal = Space.x16, vertical = Space.x14),
            )
        }
        ThinDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrewColors.PageBackground)
                .padding(horizontal = Space.x16, vertical = Space.x14),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(order.paidVia, style = BrewType.body, color = BrewColors.TextSecondary)
            Text(rupees(order.amount), style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
        }
    }
}

@Composable
private fun PushShade(push: SimulatedPush, onClick: () -> Unit, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Space.x8)
            .shadow(12.dp, BrewShapes.notification)
            .clip(BrewShapes.notification)
            .background(BrewColors.NotificationSurface)
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x16, vertical = Space.x14),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconTile(
            icon = Icons.Filled.LocalCafe,
            size = Sizes.iconTileSmall,
            shape = BrewShapes.iconTile,
            background = BrewColors.Primary,
            tint = BrewColors.OnDarkPrimary,
            iconSize = 17.dp,
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(push.timestamp, style = BrewType.micro, color = BrewColors.TextSecondary)
            Text(push.title, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
            Text(push.body, style = BrewType.support, color = BrewColors.TextSecondary)
        }
        Icon(
            Icons.Filled.Close,
            contentDescription = "Dismiss notification",
            tint = BrewColors.TextTertiary,
            modifier = Modifier
                .size(Space.x16)
                .clickable(onClick = onDismiss),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun OrderStatusPreview() {
    BrewBarTheme {
        OrderStatusScreen(
            order = OrderRepository.latest(),
            simulatedPush = SimulatedPush(
                title = "Your flat white is at the bar",
                body = "Order #BB-4821 · tap to see your order.",
                deeplink = "brewbar://status/BB-4821",
            ),
            onMyOrders = {},
            onBackToMenu = {},
            onPushTapped = {},
            onPushDismissed = {},
        )
    }
}
