package com.moengage.sampleapp.screens.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moengage.sampleapp.viewmodel.GoogleSigningViewModel
import kotlinx.coroutines.launch

@Composable
fun GoogleSigningScreen(viewModel: GoogleSigningViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
            onClick = {
                coroutineScope.launch {
                    viewModel.login()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Gmail"
            )
            Text(
                text = "Sign in with Google",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}