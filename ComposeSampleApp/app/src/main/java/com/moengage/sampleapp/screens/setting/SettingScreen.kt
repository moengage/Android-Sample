package com.moengage.sampleapp.screens.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moengage.sampleapp.state.UiState
import com.moengage.sampleapp.ui.theme.MoEngageSampleAppTheme
import com.moengage.sampleapp.viewmodel.GoogleSigningViewModel


@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    signingViewModel: GoogleSigningViewModel = hiltViewModel()
) {
    when (signingViewModel.loginState.value) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        is UiState.Failure -> {
            Toast.makeText(LocalContext.current, "Failed Signing, Try Again", Toast.LENGTH_SHORT)
                .show()
            GoogleSigningScreen()
        }

        is UiState.Success -> {
            ProfileScreen(modifier)
        }

        else -> {
            GoogleSigningScreen()
        }
    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    device = Devices.PIXEL_4A
)
@Composable
fun SettingScreenPreview() {
    MoEngageSampleAppTheme {
        SettingScreen()
    }
}