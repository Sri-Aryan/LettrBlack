package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.db.LeaderboardMember
import com.example.letteblack.db.TaskEntity
import com.example.letteblack.repositories.GroupMemberRepository
import com.example.letteblack.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    private val memberRepo: GroupMemberRepository
) : ViewModel() {

    // --- Tasks ---
    fun observeTasks(groupId: String): Flow<List<TaskEntity>> =
        repo.observeTasks(groupId)

    fun getTaskById(taskId: String): Flow<TaskEntity?> =
        repo.getTaskById(taskId)

    fun assignTask(
        groupId: String,
        assignerId: String,
        assigneeId: String,
        assigneeName: String,
        title: String,
        description: String,
        pointsRewarded: Int,
        dueDate: Long?
    ) {
        viewModelScope.launch {
            repo.assignTask(groupId, assignerId, assigneeId, assigneeName, title, description, pointsRewarded, dueDate)
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

    /**
     * Update task status.
     * If marking complete, automatically adds points to the assignee.
     */
    fun updateStatus(taskId: String, newStatus: String) {
        viewModelScope.launch {
            val task = repo.getTaskByIdOnce(taskId)
            if (task != null) {
                repo.updateStatus(taskId, newStatus)
                // Award points automatically if task completed
                if (newStatus == "complete") {
                    memberRepo.addPointsToMember(task.groupId,task.assigneeId, task.pointsRewarded)
                }
            }
        }
    }

    // --- Members ---
    fun members(groupId: String): Flow<List<GroupMemberEntity>> =
        memberRepo.observeMembers(groupId)

    fun observeLeaderboard(groupId: String): Flow<List<LeaderboardMember>> {
        return memberRepo.observeMembersByPoints(groupId).map { members ->
            println("DEBUG >>> Raw members = $members")
            members.map { member ->
                LeaderboardMember(
                    userId = member.userId,
                    userName = member.userName,
                    points = member.points
                )
            }
        }
    }

}