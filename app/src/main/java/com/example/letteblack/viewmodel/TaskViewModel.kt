package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.db.TaskEntity
import com.example.letteblack.repositories.GroupMemberRepository
import com.example.letteblack.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    private val memberRepo: GroupMemberRepository
) : ViewModel() {

    fun observeTasks(groupId: String): Flow<List<TaskEntity>> =
        repo.observeTasks(groupId)

    fun members(groupId: String): Flow<List<GroupMemberEntity>> =
        memberRepo.observeMembers(groupId)

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
        pointsRewarded: Int,
        assigneeId: String
    ) {
        viewModelScope.launch {
            repo.updateTask(taskId, title, description, dueDate, pointsRewarded, assigneeId)
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
}