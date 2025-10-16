package com.example.letteblack.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.data.local.entities.UserEntity
import com.example.letteblack.domain.repository.UserRepository
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
                val updatedUser = it.copy(avatarUrl = newAvatarUri)
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
        // timestamp to avoid conflicts
        val timestamp = System.currentTimeMillis()
        val avatarRef = storageRef.child("avatars/$uid/$timestamp.jpg")

        Log.d("UserViewModel", "Uploading to: avatars/$uid/$timestamp.jpg")

        avatarRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("UserViewModel", "Upload successful")

                // download URL after successful upload
                avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val downloadUrl = downloadUri.toString()
                    Log.d("UserViewModel", "Download URL: $downloadUrl")

                    // Save to Room and Firestore
                    saveAvatarUrl(uid, downloadUrl)
                    onSuccess(downloadUrl)
                }.addOnFailureListener { exception ->
                    Log.e("UserViewModel", "Error getting download URL", exception)
                    onError(exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserViewModel", "Upload failed", exception)
                onError(exception)
            }
    }

    private fun updateAvatarInFirestore(uid: String, avatarUrl: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid)
            .update("avatarUrl", avatarUrl)
            .addOnSuccessListener {}
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    fun saveAvatarUrlToFirestore(uid: String, avatarUrl: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid)
            .update("avatarUrl", avatarUrl)
            .addOnSuccessListener {  }
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


    // Load avatar from Firestore on app start
    fun loadAvatarFromFirestore(uid: String) {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val avatarUrl = document.getString("avatarUrl")
                        if (!avatarUrl.isNullOrEmpty()) {
                            viewModelScope.launch {
                                val currentUser = repo.getUserOnce()
                                currentUser?.let {
                                    repo.updateUser(it.copy(avatarUrl = avatarUrl))
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("UserViewModel", "Error loading avatar from Firestore", e)
                }
        }
    }

    private fun saveAvatarUrl(uid: String, avatarUrl: String) {
        viewModelScope.launch {
            // Saving to Room
            val currentUser = repo.getUserOnce()
            currentUser?.let {
                repo.updateUser(it.copy(avatarUrl = avatarUrl))
            }

            val db = Firebase.firestore
            db.collection("users").document(uid)
                .set(mapOf("avatarUrl" to avatarUrl), com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("UserViewModel", "Avatar URL saved to Firestore")
                }
                .addOnFailureListener { e ->
                    Log.e("UserViewModel", "Error saving to Firestore", e)
                }
        }
    }
}