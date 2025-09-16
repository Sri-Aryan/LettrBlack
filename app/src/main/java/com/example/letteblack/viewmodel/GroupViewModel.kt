package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.repositories.GroupMemberRepository
import com.example.letteblack.repositories.GroupRepository
import com.example.letteblack.repositories.NoteRepository
import com.example.letteblack.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepo: GroupRepository,
    private val memberRepo: GroupMemberRepository
) : ViewModel() {

    fun groups() = groupRepo.observeGroups()

    fun members(groupId: String) = memberRepo.observeMembers(groupId)

    fun createGroup(name: String, description: String?, creatorId: String, creatorName: String) {
        viewModelScope.launch {
            groupRepo.createGroup(name, description, creatorId, creatorName)
        }
    }

    fun getGroup(groupId: String) = groupRepo.getGroup(groupId)

    fun joinGroup(groupId: String, userId: String, userName: String) {
        viewModelScope.launch {
            memberRepo.joinGroup(groupId, userId, userName)
        }
    }

    fun updateGroupWithMembers(
        groupId: String,
        groupName: String,
        description: String?,
        creatorName: String,
        members: List<GroupMemberEntity>
    ) {
        viewModelScope.launch {
            groupRepo.updateGroup(groupId, groupName, description, creatorName)
            groupRepo.updateMembers(groupId, members)
        }
    }

    suspend fun getGroupMembers(groupId: String): List<GroupMemberEntity> {
        return groupRepo.getMembers(groupId)
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            groupRepo.deleteGroup(groupId)
        }
    }

    fun userGroups(userId: String): Flow<List<GroupEntity>> {
        return groupRepo.getUserGroups(userId)
    }
}