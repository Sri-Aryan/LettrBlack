package com.example.letteblack

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.HomeScreen
import com.example.letteblack.screens.LoginScreen
import com.example.letteblack.screens.ProfileScreen
import com.example.letteblack.screens.PuzzleCategoryScreen
import com.example.letteblack.screens.PuzzlePlayScreen
import com.example.letteblack.screens.PuzzleScreen
import com.example.letteblack.screens.SettingsScreen
import com.example.letteblack.screens.SignUpScreen
import com.example.letteblack.screens.SplashScreen
import com.example.letteblack.screens.onboarding.OnBoardingViewModel
import com.example.letteblack.screens.onboarding.OnboardingScreen
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
            LoginScreen(navController,modifier,authViewModel)
        }
        composable(Routes.SignUp.toString()) {
            val authViewModel: AuthViewModel = hiltViewModel()
            SignUpScreen(navController,modifier,authViewModel)
        }
        composable(Routes.OnBoarding.toString()) {
            val viewModel: OnBoardingViewModel = hiltViewModel()
            OnboardingScreen(navController,viewModel)
        }
        composable(Routes.Home.toString()) {

            val authViewModel: AuthViewModel = hiltViewModel()
            HomeScreen(navController,modifier,authViewModel)

        }
        composable(Routes.Profile.toString()) {
            ProfileScreen(navController)
        }

        // Settings route using Routes object
        composable("settings") {

            val authViewModel: AuthViewModel = viewModel()
            SettingsScreen(navController, authViewModel)
        }

        composable(
            route = "puzzlePlay/{puzzleName}/{puzzleImage}/{puzzleDescription}",
            arguments = listOf(
                navArgument("puzzleName") { type = NavType.StringType },
                navArgument("puzzleImage") { type = NavType.IntType },
                navArgument("puzzleDescription") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val puzzleName = backStackEntry.arguments?.getString("puzzleName")
            val puzzleImage = backStackEntry.arguments?.getInt("puzzleImage")
            val puzzleDescription = backStackEntry.arguments?.getString("puzzleDescription")

            PuzzlePlayScreen(
                puzzleImage = puzzleImage ?: 0,
                puzzleTitle = puzzleName ?: "Unknown",
                puzzleDescription = puzzleDescription ?: "No description",
                onPlayClick = {},
                modifier = modifier
            )
        }


        composable(Routes.PuzzleCategory.toString()) {
            PuzzleCategoryScreen(modifier = modifier)

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