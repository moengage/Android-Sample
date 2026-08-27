package com.moengage.sampleapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * The design system's "moe" family is not shipped with this sample, so the ramp is expressed
 * on the platform default (Roboto). Sizes/weights/line-heights are the handoff values.
 */
private val Brand = FontFamily.Default

object BrewType {
    /** 38/bold, line-height 1.05 — the splash wordmark. */
    val display = TextStyle(
        fontFamily = Brand,
        fontSize = 38.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold,
    )

    /** 26/bold — screen titles. */
    val screenTitle = TextStyle(Brand, fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold)

    /** 24/bold — push opt-in headline. */
    val screenTitleSmall = TextStyle(Brand, fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold)

    /** 22/bold — section header on a hero, item title, order-placed title. */
    val heroHeader = TextStyle(Brand, fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold)

    /** 30/bold — the big self-handled cards metric. */
    val metric = TextStyle(Brand, fontSize = 30.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold)

    /** 20/bold — greeting name, item price. */
    val titleBold = TextStyle(Brand, fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold)

    /** 18/bold — profile name, payment amount. */
    val titleBoldSmall = TextStyle(Brand, fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)

    /** 16/medium — app bar, card titles, section headers, "To pay". */
    val cardTitle = TextStyle(Brand, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium)

    /** 16/bold — bill total. */
    val cardTitleBold = TextStyle(Brand, fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold)

    /** 15/medium — progress-step title. */
    val subtitleMedium = TextStyle(Brand, fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)

    /** 15/bold — list-row price. */
    val subtitleBold = TextStyle(Brand, fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)

    /** 15/regular — value-prop copy. */
    val subtitle = TextStyle(Brand, fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.Normal)

    /** 14/medium — item names, list titles. */
    val bodyMedium = TextStyle(Brand, fontSize = 14.sp, lineHeight = 19.sp, fontWeight = FontWeight.Medium)

    /** 14/bold — featured price. */
    val bodyBold = TextStyle(Brand, fontSize = 14.sp, lineHeight = 19.sp, fontWeight = FontWeight.Bold)

    /** 14/regular — body copy, checkbox labels. */
    val body = TextStyle(Brand, fontSize = 14.sp, lineHeight = 19.sp, fontWeight = FontWeight.Normal)

    /** 13/regular — supporting copy. Line-height 1.35 where noted. */
    val support = TextStyle(Brand, fontSize = 13.sp, lineHeight = 17.6.sp, fontWeight = FontWeight.Normal)

    /** 13/medium — inline links ("Reorder", "View"). */
    val supportMedium = TextStyle(Brand, fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)

    /** 12/regular — meta, hints, timestamps. */
    val caption = TextStyle(Brand, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal)

    /** 12/medium — field labels, "Change", "Full menu". */
    val captionMedium = TextStyle(Brand, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)

    /** 11/regular — micro copy under a title. */
    val micro = TextStyle(Brand, fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.Normal)

    /** 11/medium — nav labels, pills, badge. */
    val microMedium = TextStyle(Brand, fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.Medium)

    /** 11/medium uppercase, tracking 0.6–0.8 — inbox group headers. */
    val label = TextStyle(
        fontFamily = Brand,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.07.em,
    )

    /** 17/medium centred — OTP digits. */
    val otpDigit = TextStyle(Brand, fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium)

    /** 19/bold — avatar initials. */
    val initials = TextStyle(Brand, fontSize = 19.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)

    val link = supportMedium.copy(textDecoration = TextDecoration.None)
}

private fun TextStyle(
    family: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit,
    lineHeight: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight,
) = TextStyle(fontFamily = family, fontSize = fontSize, lineHeight = lineHeight, fontWeight = fontWeight)

internal val BrewTypography = Typography(
    displayLarge = BrewType.display,
    headlineLarge = BrewType.screenTitle,
    headlineMedium = BrewType.heroHeader,
    titleLarge = BrewType.titleBold,
    titleMedium = BrewType.cardTitle,
    titleSmall = BrewType.subtitleMedium,
    bodyLarge = BrewType.body,
    bodyMedium = BrewType.support,
    bodySmall = BrewType.caption,
    labelLarge = BrewType.bodyMedium,
    labelMedium = BrewType.captionMedium,
    labelSmall = BrewType.microMedium,
)
