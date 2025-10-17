package com.example.letteblack.data.repository.user

import com.example.letteblack.data.local.dao.UserDao
import com.example.letteblack.data.local.entities.UserEntity
import com.example.letteblack.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override fun observeUser(): Flow<UserEntity?> = userDao.getUser()

    override suspend fun getUserOnce(): UserEntity? = userDao.getUserOnce()

    override suspend fun clearUser() {
        userDao.clearUser()
    }

    override suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}