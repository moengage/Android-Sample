package com.moengage.sampleapp.screens.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moengage.sampleapp.screens.home.MainScreen
import com.moengage.sampleapp.screens.onboarding.OnboardScreen

@Composable
fun NavigationStack(activity: ComponentActivity, startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Route.OnBoarding.route) {
            OnboardScreen(activity)
        }

        composable(route = Route.Home.route) {
            MainScreen()
        }
    }
}