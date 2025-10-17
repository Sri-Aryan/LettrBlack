package com.example.letteblack.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val taskId: String,
    val groupId: String,
    val assignerId: String,     // who assigned
    val assigneeId: String,     // to whom
    val title: String,
    val description: String,
    val dueDate: Long?,
    val pointsRewarded: Int,
    val assigneeName: String,
    val status: String = "pending",  // pending / completed
    val createdAt: Long,
    val updatedAt: Long
)