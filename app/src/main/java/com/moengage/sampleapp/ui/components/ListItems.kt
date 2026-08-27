package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moengage.sampleapp.data.DemoCatalogue
import com.moengage.sampleapp.domain.model.MenuItem
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space
import com.moengage.sampleapp.ui.util.rupees

/**
 * Menu-home featured card: radius 14 white with a 1px border, a 104 dp image,
 * then 14/medium name · 11/regular note · 14/bold price.
 */
@Composable
fun FeaturedCard(item: MenuItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    BrewCard(modifier = modifier, onClick = onClick) {
        ImageBanner(item.image, Sizes.featuredImageHeight)
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
                item.name,
                style = BrewType.bodyMedium,
                color = BrewColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                item.note,
                style = BrewType.micro,
                color = BrewColors.TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(rupees(item.price), style = BrewType.bodyBold, color = BrewColors.TextPrimary)
        }
    }
}

/**
 * Category-list row: min height 112 and it never shrinks. A 100 dp full-height image on the
 * left; on the right the name and note, with the price and "Add" pinned to the bottom.
 */
@Composable
fun MenuRow(item: MenuItem, onClick: () -> Unit, onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Fixed 112: the design's rows are uniform and the image fills the full height.
            // The note is capped at two lines so a long one can never force the row taller.
            .height(Sizes.listRowMinHeight)
            .clip(BrewShapes.card)
            .background(BrewColors.Surface)
            .border(1.dp, BrewColors.BorderSubtle, BrewShapes.card)
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .width(Sizes.listRowImageWidth)
                .fillMaxHeight()
                .background(BrewColors.NeutralFill),
        ) {
            CroppedImage(item.image, Modifier.fillMaxSize())
        }
        // Name and note at the top, price + Add pinned to the bottom of the row.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = Space.x14, vertical = Space.x12),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Space.x6)) {
                Text(item.name, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
                Text(
                    item.note,
                    style = BrewType.caption.copy(lineHeight = 16.2.sp),
                    color = BrewColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            SpaceBetweenRow {
                Text(
                    rupees(item.price),
                    style = BrewType.subtitleBold,
                    color = BrewColors.TextPrimary,
                    maxLines = 1,
                    softWrap = false,
                )
                AddPill(onAdd)
            }
        }
    }
}

/** Cart line: 52 dp thumb, name, options, amount. */
@Composable
fun CartLineRow(name: String, options: String, amount: Int, imageRes: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Space.x12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(Sizes.cartThumb)
                .clip(BrewShapes.input)
                .background(BrewColors.NeutralFill),
        ) {
            CroppedImage(imageRes, Modifier.size(Sizes.cartThumb))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Space.x4)) {
            Text(name, style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
            Text(options, style = BrewType.caption, color = BrewColors.TextSecondary)
        }
        Text(rupees(amount), style = BrewType.bodyMedium, color = BrewColors.TextPrimary)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F3, widthDp = 412)
@Composable
private fun ListItemsPreview() {
    BrewBarTheme {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MenuRow(DemoCatalogue.byId("flat-white"), {}, {})
            FeaturedCard(DemoCatalogue.byId("cold-brew"), {}, Modifier.width(180.dp))
        }
    }
}
