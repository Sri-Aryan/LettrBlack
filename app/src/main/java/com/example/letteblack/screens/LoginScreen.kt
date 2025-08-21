package com.example.letteblack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.letteblack.R
import com.example.letteblack.UserState
import com.example.letteblack.Utils
import com.example.letteblack.AuthViewModel
import com.example.letteblack.data.Routes

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    LaunchedEffect(authViewModel.userState.value){
        when(authViewModel.userState.value){
            is UserState.Authenticated -> navController.navigate(Routes.Home.toString())
            is UserState.Error -> Utils.showToast(context,authViewModel.userState.value.toString())
            else -> null
        }
    }
    var eyeOpener by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Welcome To Login Screen",
                fontSize = (24.sp),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = authViewModel.email,
                onValueChange = {authViewModel.onEmailChange(it)},
                placeholder = {Text("Enter your email")},
                label = {Text("Email")},
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 16.dp),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = authViewModel.password,
                onValueChange = {authViewModel.onPasswordChange(it)},
                placeholder = {Text("Enter your password")},
                label = {Text("Password")},
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 16.dp),
                shape = CircleShape,
                visualTransformation = if(!eyeOpener) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = {eyeOpener = !eyeOpener},
                        modifier = Modifier.size(50.dp))
                    {
                        Icon(painterResource(R.drawable.eye),null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(40.dp))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if(authViewModel.email.isNotEmpty() && authViewModel.password.isNotEmpty()){
                    authViewModel.login(authViewModel.email,authViewModel.password)
                        authViewModel.email=""
                    authViewModel.password=""
                }else{
                    Utils.showToast(context,"enter the email and password")
                }
            },
                enabled = authViewModel.userState.value!= UserState.Loading,
                modifier = Modifier.size(200.dp,50.dp)){
                Text("Login")
            }
            Spacer(modifier = Modifier.height(0.dp))
            TextButton(onClick = {
                navController.navigate(Routes.SignUp.toString())
            }){
                Text("Don't have any account, SignUp")
            }
        }
    }
