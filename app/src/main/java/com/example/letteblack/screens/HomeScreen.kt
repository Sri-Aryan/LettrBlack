package com.example.letteblack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.letteblack.UserState
import com.example.letteblack.Utils
import com.example.letteblack.ViewModel
import com.example.letteblack.data.Routes
import com.example.letteblack.data.UserDetails
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier, viewModel: ViewModel){
    LaunchedEffect(viewModel.userState.value){
        when(viewModel.userState.value){
            is UserState.Unauthenticated -> navController.navigate(Routes.Login.toString())
            else -> null
        }
    }
    var user: UserDetails by remember { mutableStateOf(UserDetails()) }
    LaunchedEffect(Unit){
        Utils.uid()?.let{
            Firebase.firestore.collection("users")
                .document(it).get().addOnCompleteListener {listener->
                    if(listener.isSuccessful){
                        user= listener.result.toObject(UserDetails()::class.java) as UserDetails
                    }
                }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Welcome To HomeScreen ${user.name}",
            fontSize = (24.sp),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier= Modifier.height(8.dp))
    Button(onClick = {
        viewModel.signOut()
    },
        modifier = Modifier.size(200.dp,50.dp)){
        Text("Sign Out")
    }
    }
}