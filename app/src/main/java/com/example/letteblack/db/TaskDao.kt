package com.example.letteblack.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun observeTasks(groupId: String): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET status = :status, updatedAt = :updatedAt WHERE taskId = :taskId")
    suspend fun updateStatus(taskId: String, status: String, updatedAt: Long)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun delete(taskId: String)

    @Query("""
        UPDATE tasks 
        SET title = :title, description = :description, dueDate = :dueDate, 
            pointsRewarded = :pointsRewarded, updatedAt = :updatedAt
        WHERE taskId = :taskId
    """)
    suspend fun updateTask(
        taskId: String,
        title: String,
        description: String,
        dueDate: Long?,
        pointsRewarded: Int,
        updatedAt: Long
    )
}