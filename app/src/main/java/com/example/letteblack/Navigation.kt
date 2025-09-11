package com.example.letteblack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.HomeScreen
import com.example.letteblack.screens.LoginScreen
import com.example.letteblack.screens.ProfileScreen
import com.example.letteblack.screens.SettingsScreen
import com.example.letteblack.screens.SignUpScreen
import com.example.letteblack.screens.SplashScreen
import com.example.letteblack.screens.settings.AccountScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, Routes.Splash.toString()) {
        composable(Routes.Splash.toString()) {
            SplashScreen(modifier, navController)
        }
        composable(Routes.Login.toString()) {
            val authViewModel: AuthViewModel = hiltViewModel()
            LoginScreen(navController, modifier, authViewModel)
        }
        composable(Routes.SignUp.toString()) {
            val authViewModel: AuthViewModel = hiltViewModel()
            SignUpScreen(navController, modifier, authViewModel)
        }
        composable(Routes.Home.toString()) {
            val authViewModel: AuthViewModel = hiltViewModel()
            HomeScreen(navController, modifier, authViewModel)
        }
        composable(Routes.Profile.toString()) {
            ProfileScreen(navController)
        }
        composable("settings") {
            val authViewModel: AuthViewModel = hiltViewModel()
            SettingsScreen(navController, authViewModel)
        }

        composable(Routes.Account.toString()) {
            AccountScreen(
                navController = navController,
                onDeleteAccount = { /* hook into authViewModel.deleteUser() */ },
                onChangeAvatar = { /* open avatar picker */ }
            )
        }
    }
}