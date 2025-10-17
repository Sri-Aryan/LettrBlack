package com.example.letteblack.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.letteblack.R
import com.example.letteblack.data.remote.Routes
import com.example.letteblack.viewmodel.AuthViewModel
import com.example.letteblack.viewmodel.UserState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Observe the user state
    val userState by authViewModel.userState

    LaunchedEffect(Unit) {
        authViewModel.checkLoginState()
        delay(2000) // splash screen delay
        when (userState) {
            is UserState.Authenticated -> {
                // User is already logged in -> navigate to Home
                navController.navigate(Routes.Home.toString()) {
                    popUpTo(Routes.Splash.toString()) { inclusive = true }
                }
            }
            else -> {
                // User not logged in -> navigate to SignUp/Login
                navController.navigate(Routes.SignUp.toString()) {
                    popUpTo(Routes.Splash.toString()) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.lettrblack_logo),
            contentDescription = "LettrBlack Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                text = "lettr",
                color = Color.Black,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "black",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9D4EDD)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Learn,Grow,Succeed",
            color = Color.Black,
            fontSize = 15.sp
        )
    }
}