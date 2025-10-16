package com.example.letteblack.domain.repository

import com.example.letteblack.data.local.entities.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

interface GroupMemberRepository {
    fun observeMembers(groupId: String): Flow<List<GroupMemberEntity>>

    fun observeMembersByPoints(groupId: String): Flow<List<GroupMemberEntity>>

    suspend fun joinGroup(groupId: String, userId: String, userName: String): Boolean

    suspend fun addPointsToMember(groupId: String, userId: String, points: Int)

    fun getMemberById(memberId: String): Flow<GroupMemberEntity?>
}