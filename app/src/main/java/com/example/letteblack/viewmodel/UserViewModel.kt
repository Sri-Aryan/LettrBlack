package com.example.letteblack.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.UserEntity
import com.example.letteblack.repositories.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    val user: StateFlow<UserEntity?> = repo.observeUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            repo.saveUser(user)
        }
    }

    fun clearUser() {
        viewModelScope.launch {
            repo.clearUser()
        }
    }

    fun setAvatar(newAvatarUri: String) {
        viewModelScope.launch {
            val currentUser = repo.getUserOnce()
            currentUser?.let {
                val updatedUser = it.copy(avatarUri = newAvatarUri)
                repo.updateUser(updatedUser) // Just local save(Room)
                updateAvatarInFirestore(it.uid, newAvatarUri)
            }
        }
    }

    fun uploadAvatarToFirebase(
        uid: String,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val storageRef = Firebase.storage.reference
        val avatarRef = storageRef.child("avatars/$uid.jpg")

        avatarRef.putFile(imageUri)
            .addOnSuccessListener {
                avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    private fun updateAvatarInFirestore(uid: String, avatarUrl: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid)
            .update("avatarUrl", avatarUrl)
            .addOnSuccessListener { /* synced successfully */ }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    fun saveAvatarUrlToFirestore(uid: String, avatarUrl: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid)
            .update("avatarUrl", avatarUrl)
            .addOnSuccessListener { /* success */ }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    fun setNotificationEnabled(enabled : Boolean){
        viewModelScope.launch {
            val currentUser = repo.getUserOnce()
            currentUser?.let {
                repo.updateUser(it.copy(notificationEnabled = enabled))
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean){
        viewModelScope.launch {
            val currentUser = repo.getUserOnce()
            currentUser?.let {
                repo.updateUser(it.copy(soundEnabled = enabled))
            }
        }
    }
}