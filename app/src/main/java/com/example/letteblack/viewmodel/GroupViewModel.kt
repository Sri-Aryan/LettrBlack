package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.repositories.GroupMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repo: GroupMemberRepository
) : ViewModel() {

    fun members(groupId: String): StateFlow<List<GroupMemberEntity>> =
        repo.observeMembers(groupId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun joinGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            repo.joinGroup(groupId, userId)
        }
    }
}