package com.example.letteblack.repositories

import com.example.letteblack.db.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

interface GroupMemberRepository {
    fun observeMembers(groupId: String): Flow<List<GroupMemberEntity>>
    suspend fun joinGroup(groupId: String, userId: String, userName: String)
}