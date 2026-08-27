package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * The house card: white, radius 14, a 1px `#ECEFF6` border and no shadow.
 * Elevation in this design is carried entirely by the border.
 */
@Composable
fun BrewCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = BrewShapes.card,
    background: Color = BrewColors.Surface,
    borderColor: Color = BrewColors.BorderSubtle,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(1.dp, borderColor, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding),
        content = content,
    )
}

/** A rounded square holding a single icon — the recurring "tile" in this design. */
@Composable
fun IconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = Sizes.iconTileMedium,
    shape: RoundedCornerShape = BrewShapes.input,
    background: Color = BrewColors.PrimaryLightTint,
    tint: Color = BrewColors.Primary,
    iconSize: Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

/** Section header: 16/medium title on the left, an optional 12/regular link on the right. */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = BrewType.cardTitle, color = BrewColors.TextPrimary)
        if (actionLabel != null) {
            Text(
                actionLabel,
                style = BrewType.captionMedium,
                color = BrewColors.Link,
                modifier = Modifier
                    .then(if (onAction != null) Modifier.clickable(onClick = onAction) else Modifier)
                    .padding(start = Space.x8, top = Space.x4, bottom = Space.x4),
            )
        }
    }
}

/** A label/value row inside a divided card (Taste profile, bill, items). */
@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelStyle: androidx.compose.ui.text.TextStyle = BrewType.body,
    labelColor: Color = BrewColors.TextSecondary,
    valueStyle: androidx.compose.ui.text.TextStyle = BrewType.bodyMedium,
    valueColor: Color = BrewColors.TextPrimary,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = labelStyle, color = labelColor)
        Text(value, style = valueStyle, color = valueColor)
    }
}

@Composable
fun ThinDivider(modifier: Modifier = Modifier, color: Color = BrewColors.BorderSubtle) {
    HorizontalDivider(modifier = modifier, thickness = 1.dp, color = color)
}

/** Image helper that always crops rather than distorts. */
@Composable
fun CroppedImage(resId: Int, modifier: Modifier = Modifier, contentDescription: String? = null) {
    Image(
        painter = painterResource(resId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

/** Full-width strip used to fill a card slot with art. */
@Composable
fun ImageBanner(resId: Int, height: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(BrewColors.NeutralFill),
    ) {
        CroppedImage(resId, Modifier.fillMaxSize())
    }
}

/** Bottom action bar with the spec'd 1px top border. */
@Composable
fun FooterBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = Sizes.screenPadding,
        end = Sizes.screenPadding,
        top = Space.x14,
        bottom = Space.x18,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth().background(BrewColors.Surface)) {
        ThinDivider()
        Column(modifier = Modifier.padding(contentPadding), content = content)
    }
}

/** Row scope helper for the "left content / right content" pattern. */
@Composable
fun SpaceBetweenRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = verticalAlignment,
        content = content,
    )
}
