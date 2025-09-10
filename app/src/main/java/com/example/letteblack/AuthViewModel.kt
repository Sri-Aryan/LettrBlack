package com.example.letteblack

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.letteblack.data.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {

    private val _userState = mutableStateOf<UserState?>(null)
    val userState: State<UserState?> = _userState
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    fun onNameChange(value: String) {
        name = value
    }

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    private val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    fun login(email: String, password: String) {
        _userState.value = UserState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                user?.let { u -> _userState.value = UserState.Authenticated(u) }
            } else {
                _userState.value =
                    UserState.Error(it.exception?.localizedMessage ?: "Login failed")
            }
        }
    }

    fun sign(
        name: String,
        email: String,
        password: String,
        onSuccess: (String) -> Unit = {}
    ) {
        _userState.value = UserState.Loading
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let { u ->
                    val userDetails = UserDetails(u.uid, name, email)
                    firestore.collection("users").document(u.uid)
                        .set(userDetails)
                        .addOnSuccessListener {
                            _userState.value = UserState.Authenticated(u)
                            Log.d("Firestore", "User details saved successfully")
                            onSuccess(u.uid)
                        }
                        .addOnFailureListener { e ->
                            _userState.value =
                                UserState.Error(e.localizedMessage ?: "Error saving user")
                            Log.e("Firestore", "Error saving user", e)
                        }
                }
            } else {
                _userState.value =
                    UserState.Error(task.exception?.localizedMessage ?: "Signup failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _userState.value = UserState.Unauthenticated
    }
}


sealed class UserState {
    object Loading : UserState()
    data class Authenticated(val user: FirebaseUser) : UserState()
    object Unauthenticated : UserState()
    data class Error(val error: String) : UserState()
}