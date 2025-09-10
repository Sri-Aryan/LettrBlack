package com.example.letteblack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.TaskEntity
import com.example.letteblack.repositories.TaskRepository
import com.example.letteblack.streak.StreakManager
import com.example.letteblack.streak.StudyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repo: TaskRepository
) : ViewModel() {

    // ================== Task Management ==================
    fun observeTasks(groupId: String): Flow<List<TaskEntity>> =
        repo.observeTasks(groupId)

    fun assignTask(
        groupId: String,
        assignerId: String,
        assigneeId: String,
        title: String,
        description: String,
        pointsRewarded: Int,
        dueDate: Long?
    ) {
        viewModelScope.launch {
            repo.assignTask(groupId, assignerId, assigneeId, title, description, pointsRewarded, dueDate)
        }
    }

    fun updateStatus(taskId: String, status: String) {
        viewModelScope.launch {
            repo.updateStatus(taskId, status)
        }
    }

    fun updateTask(
        taskId: String,
        title: String,
        description: String,
        dueDate: Long?,
        pointsRewarded: Int
    ) {
        viewModelScope.launch {
            repo.updateTask(taskId, title, description, dueDate, pointsRewarded)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repo.deleteTask(taskId)
        }
    }

    fun getTaskById(taskId: String): Flow<TaskEntity?> {
        return repo.getTaskById(taskId)
    }

    // ================== Streak & Milestones ==================
    fun logStudySession(context: Context, onResult: (StudyResult) -> Unit) {
        viewModelScope.launch {
            val result = StreakManager.recordStudySession(context)
            onResult(result)
        }
    }
}
