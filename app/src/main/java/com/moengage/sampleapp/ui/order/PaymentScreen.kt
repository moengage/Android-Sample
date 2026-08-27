package com.moengage.sampleapp.ui.order

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
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.PaymentRepository
import com.moengage.sampleapp.data.Store
import com.moengage.sampleapp.domain.model.Bill
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.domain.model.PaymentMethod
import com.moengage.sampleapp.ui.components.BrewAppBar
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.FooterBar
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * Screen 8 — payment.
 *
 * MoEngage moment: `Checkout_Started` (amount, fulfilment, coupon) on arrival; "Pay" then
 * hands over to Order status, which fires `Order_Placed`.
 */
@Composable
fun PaymentScreen(
    bill: Bill,
    fulfilment: Fulfilment,
    itemsCount: Int,
    selectedMethodId: String,
    onBack: () -> Unit,
    onMethodSelected: (String) -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        BrewAppBar(title = "Payment", onBack = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(Sizes.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Space.x18),
        ) {
            SummaryCard(bill, fulfilment, itemsCount)

            Column(verticalArrangement = Arrangement.spacedBy(Space.x12)) {
                PaymentRepository.methods.forEach { method ->
                    MethodRow(
                        method = method,
                        selected = method.id == selectedMethodId,
                        onClick = { onMethodSelected(method.id) },
                    )
                }
            }

            if (bill.couponCode != null) {
                CouponBanner(bill.couponCode, bill.discount)
            }
        }
        FooterBar {
            PrimaryButton("Pay ${rupees(bill.toPay)}", onPay)
        }
    }
}

@Composable
private fun SummaryCard(bill: Bill, fulfilment: Fulfilment, itemsCount: Int) {
    BrewCard(
        background = BrewColors.PageBackground,
        borderColor = BrewColors.BorderSubtle,
        contentPadding = PaddingValues(Space.x16),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Space.x4)) {
                Text(
                    "${fulfilment.label} · ${Store.SUMMARY_LINE}",
                    style = BrewType.caption,
                    color = BrewColors.TextSecondary,
                )
                Text(
                    "$itemsCount items · ready in ${fulfilment.eta}",
                    style = BrewType.bodyMedium,
                    color = BrewColors.TextPrimary,
                )
            }
            Text(rupees(bill.toPay), style = BrewType.titleBoldSmall, color = BrewColors.TextPrimary)
        }
    }
}

@Composable
private fun MethodRow(method: PaymentMethod, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioDot(selected)
        Text(
            "${method.label} · ${method.detail}",
            style = BrewType.body,
            color = BrewColors.TextPrimary,
        )
    }
}

/** 18 dp dot; selected draws the 5px `#06A6B7` ring the spec calls for. */
@Composable
private fun RadioDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(BrewShapes.pill)
            .border(
                width = if (selected) 5.dp else 1.dp,
                color = if (selected) BrewColors.Primary else BrewColors.BorderDefault,
                shape = BrewShapes.pill,
            ),
    )
}

@Composable
private fun CouponBanner(code: String, discount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(BrewShapes.button)
            .background(BrewColors.PrimaryLightTint)
            .padding(horizontal = Space.x14, vertical = Space.x12),
        horizontalArrangement = Arrangement.spacedBy(Space.x10),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.LocalOffer,
            contentDescription = null,
            tint = BrewColors.Primary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            "$code applied — you saved ${rupees(discount)}",
            style = BrewType.body,
            color = BrewColors.TextPrimary,
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun PaymentPreview() {
    BrewBarTheme {
        PaymentScreen(
            bill = Bill(560, 28, "CHILL20", 112, 0),
            fulfilment = Fulfilment.Pickup,
            itemsCount = 2,
            selectedMethodId = PaymentRepository.DEFAULT_METHOD,
            onBack = {},
            onMethodSelected = {},
            onPay = {},
        )
    }
}
