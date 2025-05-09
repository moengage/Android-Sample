package com.moengage.sampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.moengage.sampleapp.screens.navigation.NavigationStack
import com.moengage.sampleapp.screens.navigation.Route
import com.moengage.sampleapp.ui.theme.MoEngageSampleAppTheme
import com.moengage.sampleapp.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    private val onboardingViewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition({
                onboardingViewModel.keepSplashVisible.value
            })
        }

        setContent {
            MoEngageSampleAppTheme {
                NavigationStack(
                    this,
                    if (onboardingViewModel.isOnBoardingCompleted.value) {
                        Route.Home.route
                    } else {
                        Route.OnBoarding.route
                    }
                )
            }
        }
    }
}