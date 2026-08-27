package com.moengage.sampleapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moengage.sampleapp.data.CartRepository
import com.moengage.sampleapp.domain.model.Bill
import com.moengage.sampleapp.domain.model.CartLine
import com.moengage.sampleapp.domain.model.CupPreference
import com.moengage.sampleapp.domain.model.Fulfilment
import com.moengage.sampleapp.ui.components.BrewAppBar
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.CartLineRow
import com.moengage.sampleapp.ui.components.DetailRow
import com.moengage.sampleapp.ui.components.FooterBar
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.components.SelectCard
import com.moengage.sampleapp.ui.components.SelectPill
import com.moengage.sampleapp.ui.components.ThinDivider
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * Screen 7 — the cart.
 *
 * MoEngage moment: `Cart_Viewed` with `items_count` and `amount` on arrival.
 * (`Add_To_Cart` was already fired on the item screen that filled this list.)
 */
@Composable
fun CartScreen(
    lines: List<CartLine>,
    bill: Bill,
    fulfilment: Fulfilment,
    cupPreference: CupPreference,
    onBack: () -> Unit,
    onFulfilmentChange: (Fulfilment) -> Unit,
    onCupPreferenceChange: (CupPreference) -> Unit,
    onAddAnother: () -> Unit,
    onProceed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        BrewAppBar(title = "Your order", onBack = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(Sizes.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Space.x18),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Space.x12)) {
                Fulfilment.entries.forEach { option ->
                    SelectCard(
                        title = option.label,
                        subtitle = option.eta,
                        selected = option == fulfilment,
                        onClick = { onFulfilmentChange(option) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(Space.x12)) {
                lines.forEach { line ->
                    CartLineRow(
                        name = line.name,
                        options = line.options,
                        amount = line.amount * line.quantity,
                        imageRes = line.image,
                    )
                }
                Text(
                    "+ Add another item",
                    style = BrewType.supportMedium,
                    color = BrewColors.Link,
                    modifier = Modifier.clickable(onClick = onAddAnother),
                )
            }

            BillCard(bill)

            Row(horizontalArrangement = Arrangement.spacedBy(Space.x8)) {
                CupPreference.entries.forEach { option ->
                    SelectPill(
                        label = if (option.discount > 0) {
                            "${option.label} · −₹${option.discount}"
                        } else {
                            option.label
                        },
                        selected = option == cupPreference,
                        onClick = { onCupPreferenceChange(option) },
                    )
                }
            }
        }
        FooterBar {
            PrimaryButton("Proceed to pay · ${rupees(bill.toPay)}", onProceed)
        }
    }
}

@Composable
private fun BillCard(bill: Bill) {
    BrewCard(contentPadding = PaddingValues(Space.x16)) {
        Column(verticalArrangement = Arrangement.spacedBy(Space.x10)) {
            DetailRow("Item total", rupees(bill.itemTotal))
            DetailRow("Taxes", rupees(bill.taxes))
            if (bill.couponCode != null) {
                DetailRow(
                    label = "${bill.couponCode} discount",
                    value = "−${rupees(bill.discount)}",
                    valueColor = BrewColors.SuccessText,
                )
            }
            if (bill.cupDiscount > 0) {
                DetailRow(
                    label = "Own cup",
                    value = "−${rupees(bill.cupDiscount)}",
                    valueColor = BrewColors.SuccessText,
                )
            }
            ThinDivider()
            DetailRow(
                label = "To pay",
                value = rupees(bill.toPay),
                labelStyle = BrewType.cardTitle,
                labelColor = BrewColors.TextPrimary,
                valueStyle = BrewType.cardTitleBold,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun CartPreview() {
    val lines = CartRepository.seedLines()
    val bill = Bill(
        itemTotal = 560,
        taxes = 28,
        couponCode = CartRepository.COUPON_CODE,
        discount = 112,
        cupDiscount = 15,
    )
    BrewBarTheme {
        CartScreen(
            lines = lines,
            bill = bill,
            fulfilment = Fulfilment.Pickup,
            cupPreference = CupPreference.OwnCup,
            onBack = {},
            onFulfilmentChange = {},
            onCupPreferenceChange = {},
            onAddAnother = {},
            onProceed = {},
        )
    }
}
