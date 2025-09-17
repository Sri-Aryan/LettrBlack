package com.example.letteblack.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_info LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("SELECT * FROM user_info LIMIT 1")
    suspend fun getUserOnce(): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user_info")
    suspend fun clearUser()
}