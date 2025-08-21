package com.example.letteblack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.HomeScreen
import com.example.letteblack.screens.LoginScreen
import com.example.letteblack.screens.SignUpScreen
import com.example.letteblack.screens.SplashScreen

@Composable
fun Navigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    NavHost(navController, Routes.Splash.toString()) {
        composable(Routes.Splash.toString()) {
            SplashScreen(modifier,navController)
        }
        composable(Routes.Login.toString()) {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(navController,modifier,authViewModel)
        }
        composable(Routes.SignUp.toString()) {
            val authViewModel: AuthViewModel = viewModel()
            SignUpScreen(navController,modifier,authViewModel)
        }
        composable(Routes.Home.toString()) {
            val authViewModel: AuthViewModel = viewModel()
            HomeScreen(navController,modifier,authViewModel)
        }
    }}