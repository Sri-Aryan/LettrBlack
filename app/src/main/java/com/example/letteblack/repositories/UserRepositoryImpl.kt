package com.example.letteblack.repositories

import com.example.letteblack.db.UserDao
import com.example.letteblack.db.UserEntity
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
}