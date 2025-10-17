package com.example.letteblack.domain.repository

import com.example.letteblack.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: UserEntity)

    fun observeUser(): Flow<UserEntity?>

    suspend fun getUserOnce(): UserEntity?

    suspend fun clearUser()

    suspend fun updateUser(user: UserEntity)

}