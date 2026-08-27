package com.moengage.sampleapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors
import com.moengage.sampleapp.ui.theme.BrewShapes
import com.moengage.sampleapp.ui.theme.BrewType
import com.moengage.sampleapp.ui.theme.Sizes

/** Height 52, radius 12, `#06A6B7` fill, white 16/medium label. */
@Composable
fun PrimaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.buttonHeight),
        shape = BrewShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = BrewColors.Primary,
            contentColor = BrewColors.OnDarkPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Text(label, style = BrewType.cardTitle)
    }
}

/** Same metrics as [PrimaryButton] but white fill with a 1px `#D9DFED` outline. */
@Composable
fun SecondaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.buttonHeight),
        shape = BrewShapes.button,
        border = BorderStroke(1.dp, BrewColors.BorderDefault),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = BrewColors.Surface,
            contentColor = BrewColors.TextPrimary,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Text(label, style = BrewType.cardTitle)
    }
}

/** Transparent, `#485771` label. */
@Composable
fun QuietTextButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Sizes.touchTarget),
        shape = BrewShapes.button,
    ) {
        Text(label, style = BrewType.cardTitle, color = BrewColors.TextSecondary)
    }
}

/** The two-button footer used on Order status. */
@Composable
fun FooterButtonRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F3)
@Composable
private fun ButtonsPreview() {
    BrewBarTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PrimaryButton("Get started", {})
            SecondaryButton("Continue with Google", {})
            QuietTextButton("Not now", {})
        }
    }
}
