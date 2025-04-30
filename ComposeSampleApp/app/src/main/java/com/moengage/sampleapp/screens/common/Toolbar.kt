package com.moengage.sampleapp.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moengage.sampleapp.ui.theme.Purple40

@Composable
fun Toolbar() {
    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            fontSize = 22.sp,
            text = "MoEngage News Sample",
            color = Purple40,
            fontWeight = FontWeight.Bold
        )
    }
}