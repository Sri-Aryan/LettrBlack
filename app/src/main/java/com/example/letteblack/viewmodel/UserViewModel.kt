package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.UserEntity
import com.example.letteblack.repositories.UserRepository
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
}