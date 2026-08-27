package com.moengage.sampleapp.ui.selfhandledcards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.domain.model.Coupon
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.IconTile
import com.moengage.sampleapp.ui.components.RemoteImage
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 12 — self-handled cards.
 *
 * Coupons are supplied by the caller from MoEngage Cards and rendered by the app rather than by
 * the SDK, which is what makes them self-handled. The "Ready to use" section stays hidden until
 * cards have been fetched.
 *
 * MoEngage moments: redeeming a coupon fires `Reward_Redeemed`.
 */
@Composable
fun SelfHandledCardsScreen(
    coupons: List<Coupon>,
    onBack: () -> Unit,
    onRedeem: (Coupon) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        SelfHandledCardsHeader(onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = Sizes.screenPadding,
                    end = Sizes.screenPadding,
                    top = Space.x18,
                    bottom = 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(Space.x18),
        ) {
            if (coupons.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Space.x12)) {
                    Text("Ready to use", style = BrewType.cardTitle, color = BrewColors.TextPrimary)
                    coupons.forEach { coupon ->
                        CouponCard(coupon, onRedeem = { onRedeem(coupon) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SelfHandledCardsHeader(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrewColors.PrimaryDarkSurface)
            .padding(Sizes.screenPadding),
        verticalArrangement = Arrangement.spacedBy(Space.x16),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(Sizes.backTile)
                    .clip(BrewShapes.input)
                    .border(1.dp, BrewColors.OnDarkTrack, BrewShapes.input)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = BrewColors.OnDarkPrimary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text("Self-handled cards", style = BrewType.cardTitle, color = BrewColors.OnDarkPrimary)
        }
    }
}

@Composable
private fun CouponCard(coupon: Coupon, onRedeem: () -> Unit) {
    BrewCard(contentPadding = PaddingValues(Space.x14)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Campaign artwork when the card carries an IMAGE widget; the themed icon otherwise,
            // so a card authored without one still lines up with the rest of the list.
            val image = coupon.imageUrl
            if (image != null) {
                RemoteImage(
                    url = image,
                    contentDescription = coupon.title,
                    modifier = Modifier.size(40.dp),
                )
            } else {
                IconTile(
                    icon = if (coupon.primaryAction) Icons.Filled.FreeBreakfast else Icons.Filled.Redeem,
                    size = 40.dp,
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
                Text(coupon.title, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
                if (coupon.description.isNotBlank()) {
                    Text(
                        coupon.description,
                        style = BrewType.caption,
                        color = BrewColors.TextSecondary,
                    )
                }
                Text(coupon.expiry, style = BrewType.micro, color = BrewColors.TextTertiary)
            }
            // A card whose payload has no BUTTON widget shows no pill; the row is content only.
            val actionLabel = coupon.actionLabel
            if (actionLabel != null) {
                CouponAction(actionLabel, coupon.primaryAction, onRedeem)
            }
        }
    }
}

@Composable
private fun CouponAction(label: String, filled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(BrewShapes.pill)
            .background(if (filled) BrewColors.Primary else BrewColors.Surface)
            .then(
                if (filled) {
                    Modifier
                } else {
                    Modifier.border(1.dp, BrewColors.BorderDefault, BrewShapes.pill)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Space.x14, vertical = Space.x8),
    ) {
        Text(
            label,
            style = BrewType.captionMedium,
            color = if (filled) BrewColors.OnDarkPrimary else BrewColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun SelfHandledCardsPreview() {
    BrewBarTheme {
        SelfHandledCardsScreen(
            coupons = previewCoupons,
            onBack = {},
            onRedeem = {},
        )
    }
}

private val previewCoupons = listOf(
    Coupon(
        id = "preview-free-herbal-tea",
        title = "Free herbal tea",
        description = "Any 200ml herbal infusion, hot or iced.",
        expiry = "Expires in 4 days",
        actionLabel = "Redeem",
        primaryAction = true,
    ),
    Coupon(
        id = "preview-bowl-50-off",
        title = "₹50 off any bowl",
        description = "Valid on the berry chia and savoury yogurt bowls.",
        expiry = "Weekdays before 11 am",
        actionLabel = "Use",
        primaryAction = false,
    ),
)
