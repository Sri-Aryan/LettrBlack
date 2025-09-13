package com.example.letteblack.repositories

import com.example.letteblack.db.GroupEntity
import com.example.letteblack.db.GroupMemberEntity
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun observeGroups(): Flow<List<GroupEntity>>
    fun getGroup(groupId: String): Flow<GroupEntity?>

    suspend fun createGroup(
        groupName: String,
        description: String?,
        creatorUserId: String,
        creatorUserName: String
    )

    suspend fun updateGroup(
        groupId: String,
        groupName: String,
        description: String?,
        creatorUserName: String
    )

    suspend fun deleteGroup(groupId: String)

    suspend fun getMembers(groupId: String): List<GroupMemberEntity>

    suspend fun updateMembers(groupId: String, members: List<GroupMemberEntity>)
}