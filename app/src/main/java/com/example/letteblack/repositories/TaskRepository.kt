package com.example.letteblack.repositories

import com.example.letteblack.db.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(groupId: String): Flow<List<TaskEntity>>
    suspend fun assignTask(
        groupId: String,
        assignerId: String,
        assigneeId: String,
        assigneeName: String,
        title: String,
        description: String,
        pointsRewarded: Int,
        dueDate: Long?
    )
    suspend fun updateStatus(taskId: String, status: String)
    suspend fun updateTask(
        taskId: String,
        title: String,
        description: String,
        dueDate: Long?,
        pointsRewarded: Int,
        assigneeId: String
    )
    suspend fun deleteTask(taskId: String)
    fun getTaskById(taskId: String): Flow<TaskEntity?>

    suspend fun getTaskByIdOnce(taskId: String): TaskEntity?
}