package com.moengage.sampleapp.ui.menu

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.DemoCatalogue
import com.moengage.sampleapp.domain.model.AddOn
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.ui.components.BrewCard
import com.moengage.sampleapp.ui.components.CroppedImage
import com.moengage.sampleapp.ui.components.FooterBar
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.components.SelectCard
import com.moengage.sampleapp.ui.components.SelectPill
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees
import com.moengage.sampleapp.ui.util.surcharge

/** What the user configured, handed back to the caller so it can be tracked and carted. */
data class ItemSelection(
    val size: String,
    val milk: String,
    val addOns: List<String>,
    val quantity: Int,
    val amount: Int,
)

/**
 * Screen 6 — item detail.
 *
 * MoEngage moments: `Item_Viewed` on arrival, a native in-app show call (this screen is a
 * campaign target), and `Add_To_Cart` with size / milk / add-ons / amount on "Add".
 */
@Composable
fun ItemDetailScreen(
    item: MenuItem,
    onBack: () -> Unit,
    onAdd: (ItemSelection) -> Unit,
    modifier: Modifier = Modifier,
) {
    var sizeIndex by remember(item.id) { mutableIntStateOf(1) }
    var milkIndex by remember(item.id) { mutableIntStateOf(1) }
    var quantity by remember(item.id) { mutableIntStateOf(1) }
    val selectedAddOns = remember(item.id) { mutableStateOf(emptySet<String>()) }

    val size = DemoCatalogue.sizes[sizeIndex]
    val milk = DemoCatalogue.milks[milkIndex]
    val addOnTotal = DemoCatalogue.addOns
        .filter { it.itemId in selectedAddOns.value }
        .sumOf { it.price }
    val unitPrice = item.price + size.surcharge + milk.surcharge
    val total = (unitPrice + addOnTotal) * quantity

    Column(modifier = modifier.fillMaxSize().background(BrewColors.PageBackground)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Box {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(Sizes.heroImageHeight)
                        .background(BrewColors.NeutralFill),
                ) {
                    CroppedImage(item.image, Modifier.fillMaxSize())
                }
                Box(
                    modifier = Modifier
                        .padding(Space.x14)
                        .size(Sizes.heroBackButton)
                        .clip(BrewShapes.pill)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrewColors.TextPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Column(
                modifier = Modifier.padding(Sizes.screenPadding),
                verticalArrangement = Arrangement.spacedBy(Space.x18),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Space.x8)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(item.name, style = BrewType.heroHeader, color = BrewColors.TextPrimary)
                        Text(
                            rupees(item.price),
                            style = BrewType.titleBold,
                            color = BrewColors.TextPrimary,
                        )
                    }
                    Text(item.note, style = BrewType.support, color = BrewColors.TextSecondary)
                }

                OptionSection("Size") {
                    Row(horizontalArrangement = Arrangement.spacedBy(Space.x8)) {
                        DemoCatalogue.sizes.forEachIndexed { index, option ->
                            SelectCard(
                                title = option.label,
                                subtitle = option.volume + surcharge(option.surcharge),
                                selected = index == sizeIndex,
                                onClick = { sizeIndex = index },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                OptionSection("Milk") {
                    Row(horizontalArrangement = Arrangement.spacedBy(Space.x8)) {
                        DemoCatalogue.milks.forEachIndexed { index, option ->
                            SelectPill(
                                label = option.label + surcharge(option.surcharge),
                                selected = index == milkIndex,
                                onClick = { milkIndex = index },
                            )
                        }
                    }
                }

                OptionSection("Make it a meal") {
                    Column(verticalArrangement = Arrangement.spacedBy(Space.x8)) {
                        DemoCatalogue.addOns.forEach { addOn ->
                            AddOnRow(
                                addOn = addOn,
                                checked = addOn.itemId in selectedAddOns.value,
                                onToggle = { checked ->
                                    selectedAddOns.value = if (checked) {
                                        selectedAddOns.value + addOn.itemId
                                    } else {
                                        selectedAddOns.value - addOn.itemId
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        FooterBar {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.x12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QuantityStepper(
                    quantity = quantity,
                    onDecrement = { quantity = (quantity - 1).coerceAtLeast(1) },
                    onIncrement = { quantity += 1 },
                )
                PrimaryButton(
                    label = "Add · ${rupees(total)}",
                    onClick = {
                        onAdd(
                            ItemSelection(
                                size = size.label,
                                milk = milk.label,
                                addOns = DemoCatalogue.addOns
                                    .filter { it.itemId in selectedAddOns.value }
                                    .map { it.label },
                                quantity = quantity,
                                amount = total,
                            ),
                        )
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun OptionSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.x10)) {
        Text(title, style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        content()
    }
}

@Composable
private fun AddOnRow(addOn: AddOn, checked: Boolean, onToggle: (Boolean) -> Unit) {
    BrewCard(
        shape = BrewShapes.button,
        contentPadding = PaddingValues(Space.x14),
        onClick = { onToggle(!checked) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.x12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(BrewShapes.checkbox)
                    .background(if (checked) BrewColors.Primary else Color.Transparent)
                    .border(
                        1.dp,
                        if (checked) BrewColors.Primary else BrewColors.BorderDefault,
                        BrewShapes.checkbox,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (checked) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = BrewColors.OnDarkPrimary,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            Text(
                addOn.label,
                style = BrewType.body,
                color = BrewColors.TextPrimary,
                modifier = Modifier.weight(1f),
            )
            Text(rupees(addOn.price), style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
        }
    }
}

/** Outlined "− 1 +" stepper, radius 10. */
@Composable
private fun QuantityStepper(quantity: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(
        modifier = Modifier
            .height(Sizes.buttonHeight)
            .clip(BrewShapes.input)
            .border(1.dp, BrewColors.BorderDefault, BrewShapes.input),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepperButton("−", onDecrement)
        Text(
            quantity.toString(),
            style = BrewType.bodyMedium,
            color = BrewColors.TextPrimary,
            modifier = Modifier.width(24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        StepperButton("+", onIncrement)
    }
}

@Composable
private fun StepperButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(Sizes.touchTarget)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, style = BrewType.cardTitle, color = BrewColors.TextPrimary)
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun ItemDetailPreview() {
    BrewBarTheme {
        ItemDetailScreen(item = DemoCatalogue.byId("flat-white"), onBack = {}, onAdd = {})
    }
}
