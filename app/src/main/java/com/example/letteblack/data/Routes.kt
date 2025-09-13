package com.example.letteblack.data

sealed class Routes(route: String){
    object Splash: Routes("Splash")
    object Login: Routes("login")
    object SignUp: Routes("sign")
    object Home: Routes("Home")

    object Profile : Routes("profile")

    object Account : Routes("account")

// Lets get started
    object Settings : Routes("settings")

    object OnBoarding: Routes("onboarding")
}