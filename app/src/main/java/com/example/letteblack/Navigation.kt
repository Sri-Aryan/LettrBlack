package com.example.letteblack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.HomeScreen
import com.example.letteblack.screens.LoginScreen
import com.example.letteblack.screens.SignUpScreen
import com.example.letteblack.screens.SplashScreen

@Composable
fun Navigation(viewModel: ViewModel, modifier: Modifier){
    val navController = rememberNavController()
    NavHost(navController, Routes.Splash.toString()) {
        composable(Routes.Splash.toString()) {
            SplashScreen(modifier,navController)
        }
        composable(Routes.Login.toString()) {
            LoginScreen(navController,modifier,viewModel)
        }
        composable(Routes.SignUp.toString()) {
            SignUpScreen(navController,modifier,viewModel)
        }
        composable(Routes.Home.toString()) {
            HomeScreen(navController,modifier,viewModel)
        }
    }}