package com.example.letteblack

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.data.UserDetails
import com.example.letteblack.db.UserEntity
import com.example.letteblack.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {
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
                user?.let { u ->
                    firestore.collection("users").document(u.uid).get()
                        .addOnSuccessListener { doc ->
                            val userDetails = doc.toObject(UserDetails::class.java)
                            userDetails?.let { details ->
                                viewModelScope.launch {
                                    userRepository.saveUser(
                                        UserEntity(details.uid, details.name, details.email)
                                    )
                                }
                            }
                            _userState.value = UserState.Authenticated(u)
                        }
                }
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

                    // Save in Firestore
                    firestore.collection("users").document(u.uid)
                        .set(userDetails)
                        .addOnSuccessListener {
                            viewModelScope.launch {
                                // Save in Room
                                userRepository.saveUser(
                                    UserEntity(u.uid, name, email)
                                )
                            }
                            _userState.value = UserState.Authenticated(u)
                            onSuccess(u.uid)
                        }
                        .addOnFailureListener { e ->
                            _userState.value =
                                UserState.Error(e.localizedMessage ?: "Error saving user")
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
        viewModelScope.launch {
            userRepository.clearUser()
        }
        _userState.value = UserState.Unauthenticated
    }
}


sealed class UserState {
    object Loading : UserState()
    data class Authenticated(val user: FirebaseUser) : UserState()
    object Unauthenticated : UserState()
    data class Error(val error: String) : UserState()
}