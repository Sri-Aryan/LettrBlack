package com.example.letteblack.presentation.screen.home

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.letteblack.core.navigation.BottomNavigationBar
import com.example.letteblack.core.navigation.NavGraph
import com.example.letteblack.data.remote.Routes
import com.example.letteblack.viewmodel.AuthViewModel
import com.example.letteblack.viewmodel.UserState

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // Redirect if unauthenticated
    LaunchedEffect(authViewModel.userState.value) {
        if (authViewModel.userState.value is UserState.Unauthenticated)
            navController.navigate(Routes.Login.toString())
    }

    // Observe user info
    val userInfo by authViewModel.userRepository.observeUser().collectAsState(initial = null)

    // Notifications permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            Log.d("Notifications", if (isGranted) "Granted" else "Denied")
        }

        LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    }

    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(innerNavController) }
    ) { innerPadding ->
        NavGraph(
            innerNavController = innerNavController,
            rootNavController = navController,
            authViewModel = authViewModel,
            userInfo = userInfo,
            innerPadding = innerPadding
        )
    }
}