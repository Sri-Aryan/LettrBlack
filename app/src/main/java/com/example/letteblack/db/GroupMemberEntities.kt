package com.example.letteblack.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_members")
data class GroupMemberEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val userId: String,
    val userName: String,
    val joinedAt: Long
)