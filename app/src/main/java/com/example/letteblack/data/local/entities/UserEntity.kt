package com.example.letteblack.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val notificationEnabled : Boolean= true,
    val soundEnabled: Boolean = true
)