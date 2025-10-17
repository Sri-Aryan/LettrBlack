package com.example.letteblack.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.letteblack.data.remote.Routes
import com.example.letteblack.presentation.screen.auth.LoginScreen
import com.example.letteblack.presentation.screen.auth.SignUpScreen
import com.example.letteblack.presentation.screen.home.HomeScreen
import com.example.letteblack.presentation.screen.onboarding.OnBoardingViewModel
import com.example.letteblack.presentation.screen.onboarding.OnboardingScreen
import com.example.letteblack.presentation.screen.puzzle.PuzzleCategoryScreen
import com.example.letteblack.presentation.screen.puzzle.PuzzlePlayScreen
import com.example.letteblack.presentation.screen.settings.ChangePasswordScreen
import com.example.letteblack.presentation.screen.settings.PremiumContentScreen
import com.example.letteblack.presentation.screen.settings.PrivacyScreen
import com.example.letteblack.presentation.screen.settings.ReportScreen
import com.example.letteblack.presentation.screen.settings.SettingsScreen
import com.example.letteblack.presentation.screen.splash.SplashScreen
import com.example.letteblack.screens.ProfileScreen
import com.example.letteblack.screens.settings.AccountScreen
import com.example.letteblack.viewmodel.AuthViewModel

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
        composable(Routes.OnBoarding.toString()) {
            val viewModel: OnBoardingViewModel = hiltViewModel()
            OnboardingScreen(navController, viewModel)
        }
        composable(Routes.Home.toString()) {

            val authViewModel: AuthViewModel = hiltViewModel()
            HomeScreen(navController, authViewModel)

        }
        composable("settings") {
            val authViewModel: AuthViewModel = hiltViewModel()
            SettingsScreen(navController, authViewModel)
        }
        composable(
            route = "profile/{userId}/{userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: "User Name"

            ProfileScreen(
                navController = navController,
                userId = userId,
                userName = userName
            )
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
            AccountScreen(navController = navController)
        }

        composable("privacy") {
            PrivacyScreen(navController)
        }
        composable("premium") {
            PremiumContentScreen(navController)
        }
        composable("report") {
            ReportScreen(navController)
        }

        composable("change_password") {
            ChangePasswordScreen(navController)
        }
    }
}