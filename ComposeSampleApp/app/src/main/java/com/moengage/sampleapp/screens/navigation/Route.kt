package com.moengage.sampleapp.screens.navigation


sealed class Route(val route: String) {

    object OnBoarding : Route("OnBoarding")

    object Home : Route("Home")
}