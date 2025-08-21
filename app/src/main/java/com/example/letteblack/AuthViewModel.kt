package com.example.letteblack

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.letteblack.data.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel: ViewModel(){

    private val _userState = mutableStateOf<UserState?>(null)
    val userState: State<UserState?> = _userState
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    fun onNameChange(value: String){
        name=value
    }
    fun onEmailChange(value: String){
        email=value
    }
    fun onPasswordChange(value: String){
        password=value
    }
    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    fun login(email: String,password: String){
        _userState.value= UserState.Loading
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                _userState.value= UserState.Authenticated
            }else{
                _userState.value= UserState.Error(it.exception?.localizedMessage?: "Error 404")
            }
        }
    }
    fun sign(name: String,email: String,password: String){
        _userState.value= UserState.Loading
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            Utils.uid()?.let {id->
                val user = UserDetails(id,name,email)
                firestore.collection("users").document(id)
                    .set(user).addOnSuccessListener {
                        _userState.value= UserState.Authenticated
                        Log.d("Firestore", "User details saved successfully")
                    }
                    .addOnFailureListener { e ->
                        _userState.value= UserState.Error(it.exception?.localizedMessage?: "Error 404")
                        Log.e("Firestore", "Error saving user", e)
                    }
            }
        }
    }
    fun signOut(){
        auth.signOut()
        _userState.value= UserState.Unauthenticated
    }
}


sealed class UserState{
    object Loading: UserState()
    object Authenticated: UserState()
    object Unauthenticated: UserState()
    data class Error(val error: String): UserState()
}