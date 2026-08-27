package com.moengage.sampleapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * The handoff calls dark mode out as deferred pending design-owner sign-off, so the app
 * commits to the single light palette rather than inventing dark tokens.
 */
private val BrewColorScheme = lightColorScheme(
    primary = BrewColors.Primary,
    onPrimary = BrewColors.OnDarkPrimary,
    primaryContainer = BrewColors.PrimaryLightTint,
    onPrimaryContainer = BrewColors.TextPrimary,
    secondary = BrewColors.PrimaryDarkSurface,
    onSecondary = BrewColors.OnDarkPrimary,
    background = BrewColors.PageBackground,
    onBackground = BrewColors.TextPrimary,
    surface = BrewColors.Surface,
    onSurface = BrewColors.TextPrimary,
    surfaceVariant = BrewColors.NeutralFill,
    onSurfaceVariant = BrewColors.TextSecondary,
    outline = BrewColors.BorderDefault,
    outlineVariant = BrewColors.BorderSubtle,
    error = BrewColors.UnreadBadge,
)

@Composable
fun BrewBarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BrewColorScheme,
        typography = BrewTypography,
        shapes = BrewMaterialShapes,
        content = content,
    )
}
