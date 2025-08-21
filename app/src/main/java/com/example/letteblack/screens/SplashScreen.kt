package com.example.letteblack.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.letteblack.R
import com.example.letteblack.data.Routes
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(modifier: Modifier, navController: NavHostController) {
    LaunchedEffect(Unit){
        delay(2000)
        navController.navigate(Routes.SignUp.toString()){
            popUpTo(Routes.Splash.toString()){inclusive=true}
        }
    }
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(painter = painterResource(id = R.drawable.lettrblack),"LettrBlack",
            modifier = Modifier.size(200.dp))
    }
}