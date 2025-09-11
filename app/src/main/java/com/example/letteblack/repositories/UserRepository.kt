package com.example.letteblack.repositories

import com.example.letteblack.db.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: UserEntity)
    fun observeUser(): Flow<UserEntity?>
    suspend fun getUserOnce(): UserEntity?
    suspend fun clearUser()
}