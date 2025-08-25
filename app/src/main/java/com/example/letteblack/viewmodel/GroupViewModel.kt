package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.repositories.GroupMemberRepository
import com.example.letteblack.repositories.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepo: GroupRepository,
    private val memberRepo: GroupMemberRepository
) : ViewModel() {

    fun groups() = groupRepo.observeGroups()

    fun members(groupId: String) = memberRepo.observeMembers(groupId)

    fun createGroup(name: String, creatorId: String, creatorName: String) {
        viewModelScope.launch {
            groupRepo.createGroup(name, creatorId, creatorName)
        }
    }

    fun getGroup(groupId: String) = groupRepo.getGroup(groupId)

    fun joinGroup(groupId: String, userId: String, userName: String) {
        viewModelScope.launch {
            memberRepo.joinGroup(groupId, userId, userName)
        }
    }
}