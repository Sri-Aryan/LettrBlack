package com.example.letteblack.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val avatarUri: String? = null,
    val notificationEbnabled : Boolean= true
)