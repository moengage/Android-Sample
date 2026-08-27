package com.moengage.sampleapp.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.data.DemoUser
import com.moengage.sampleapp.ui.components.BackTile
import com.moengage.sampleapp.ui.components.PrimaryButton
import com.moengage.sampleapp.ui.components.SecondaryButton
import com.moengage.sampleapp.ui.components.ThinDivider
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes
import com.moengage.sampleapp.ui.theme.Space

/**
 * Screen 2 — phone + OTP.
 *
 * MoEngage moment: on "Verify & continue" (or the Google button) the app calls
 * `setUniqueId` / `setMobileNumber` / `setUserName` / `setBirthDate` through
 * [com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper.onLoginSucceeded].
 *
 * The field values are fixed demo copy — this sample has no auth backend.
 */
@Composable
fun LoginScreen(onBack: () -> Unit, onVerified: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrewColors.Surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Sizes.screenPadding, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(Space.x22),
    ) {
        BackTile(onBack)

        Column(verticalArrangement = Arrangement.spacedBy(Space.x8)) {
            Text("Sign in for stars", style = BrewType.screenTitle, color = BrewColors.TextPrimary)
            Text(
                "Every ₹100 earns a star. Ten stars, one free drink.",
                style = BrewType.body,
                color = BrewColors.TextSecondary,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Space.x14)) {
            FieldGroup(label = "Mobile number") {
                ReadOnlyField(DemoUser.PHONE)
            }
            FieldGroup(label = "OTP") {
                OtpRow(DemoUser.OTP)
            }
        }

        PrimaryButton("Verify & continue", onVerified)

        Row(verticalAlignment = Alignment.CenterVertically) {
            ThinDivider(Modifier.weight(1f), color = BrewColors.BorderDefault)
            Text(
                "or",
                style = BrewType.caption,
                color = BrewColors.TextTertiary,
                modifier = Modifier.padding(horizontal = Space.x12),
            )
            ThinDivider(Modifier.weight(1f), color = BrewColors.BorderDefault)
        }

        SecondaryButton("Continue with Google", onVerified)

        Text(
            "Signing in links this device to your Brew Bar identity so orders, stars and " +
                "notifications follow you.",
            style = BrewType.micro,
            color = BrewColors.TextTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun FieldGroup(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.x8)) {
        Text(label, style = BrewType.captionMedium, color = BrewColors.TextSecondary)
        content()
    }
}

@Composable
private fun ReadOnlyField(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Sizes.inputHeight)
            .clip(BrewShapes.input)
            .border(1.dp, BrewColors.BorderDefault, BrewShapes.input)
            .padding(horizontal = Space.x14),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(value, style = BrewType.body, color = BrewColors.TextPrimary)
    }
}

/** Four equal 48 dp boxes, gap 10, digits 17/medium centred. */
@Composable
private fun OtpRow(otp: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(Space.x10)) {
        otp.take(4).forEach { digit ->
            Box(
                modifier = Modifier
                    .size(Sizes.inputHeight)
                    .clip(BrewShapes.input)
                    .border(1.dp, BrewColors.BorderDefault, BrewShapes.input),
                contentAlignment = Alignment.Center,
            ) {
                Text(digit.toString(), style = BrewType.otpDigit, color = BrewColors.TextPrimary)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
private fun LoginPreview() {
    BrewBarTheme { LoginScreen(onBack = {}, onVerified = {}) }
}
