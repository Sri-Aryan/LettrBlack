package com.example.letteblack.repositories

import com.example.letteblack.db.GroupEntity
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun observeGroups(): Flow<List<GroupEntity>>
    fun getGroup(groupId: String): Flow<GroupEntity?>
    suspend fun createGroup(name: String, creatorUserId: String, createrUserName: String)
}