package com.example.letteblack.data.remote

sealed class Routes(route: String){
    object Splash: Routes("Splash")
    object Login: Routes("login")
    object SignUp: Routes("sign")
    object Home: Routes("Home")

    object Profile : Routes("profile")
    object PuzzleCategory : Routes("puzzle")
    object OnBoarding: Routes("onboarding")
}