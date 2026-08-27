package com.moengage.sampleapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Radii ramp: 8 · 10 · 12 · 14 · 22 · 28 · pill. */
object BrewShapes {
    val chip = RoundedCornerShape(8.dp)
    val input = RoundedCornerShape(10.dp)
    val button = RoundedCornerShape(12.dp)
    val card = RoundedCornerShape(14.dp)
    val notification = RoundedCornerShape(22.dp)
    val osDialog = RoundedCornerShape(28.dp)
    val pill = RoundedCornerShape(percent = 50)
    val track = RoundedCornerShape(3.dp)
    val checkbox = RoundedCornerShape(5.dp)
    val iconTile = RoundedCornerShape(9.dp)
}

internal val BrewMaterialShapes = Shapes(
    extraSmall = BrewShapes.chip,
    small = BrewShapes.input,
    medium = BrewShapes.button,
    large = BrewShapes.card,
    extraLarge = BrewShapes.osDialog,
)
