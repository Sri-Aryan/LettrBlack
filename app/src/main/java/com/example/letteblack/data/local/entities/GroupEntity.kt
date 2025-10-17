package com.example.letteblack.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val groupId: String,
    val groupName: String,
    val description: String?,
    val createdByUserId: String,
    val createdByUserName: String,
    val createdAt: Long,
    val memberCount: Int
)