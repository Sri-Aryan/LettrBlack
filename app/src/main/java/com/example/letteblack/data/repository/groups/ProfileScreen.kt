package com.example.letteblack.data.repository.groups

import com.example.letteblack.data.local.dao.GroupDao
import com.example.letteblack.data.local.dao.GroupMemberDao
import com.example.letteblack.data.local.dao.NoteDao
import com.example.letteblack.data.local.dao.TaskDao
import com.example.letteblack.data.local.entities.GroupEntity
import com.example.letteblack.data.local.entities.GroupMemberEntity
import com.example.letteblack.domain.repository.GroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ProfileScreen(
    private val groupDao: GroupDao,
    private val groupMemberDao: GroupMemberDao,
    private val taskDao: TaskDao,
    private val noteDao: NoteDao,
    private val firestore: FirebaseFirestore
) : GroupRepository {

    private val groupsRef get() = firestore.collection("groups")

    override fun observeGroups() = groupDao.observeGroups()

    override suspend fun createGroup(
        groupName: String,
        description: String?,
        creatorUserId: String,
        creatorUserName: String
    ) {
        val now = System.currentTimeMillis()
        val groupId = UUID.randomUUID().toString()

        // Create the group entity
        val group = GroupEntity(
            groupId = groupId,
            groupName = groupName,
            description = description,
            createdByUserId = creatorUserId,
            createdByUserName = creatorUserName,
            createdAt = now,
            memberCount = 1
        )

        // Save locally
        groupDao.insert(group)

        // Save remotely
        val groupMap = mapOf(
            "groupId" to group.groupId,
            "groupName" to group.groupName,
            "description" to group.description,
            "createdByUserId" to group.createdByUserId,
            "createdByUserName" to group.createdByUserName,
            "createdAt" to group.createdAt,
            "memberCount" to group.memberCount
        )
        groupsRef.document(group.groupId).set(groupMap).await()

        // Add creator as first member
        val memberId = UUID.randomUUID().toString()
        val creatorMember = GroupMemberEntity(
            id = memberId,
            groupId = groupId,
            userId = creatorUserId,
            userName = creatorUserName,
            joinedAt = now,
            points = 0
        )

        // Local insert
        groupMemberDao.insert(creatorMember)

        // Firebase
        groupsRef.document(groupId)
            .collection("members")
            .document(memberId)
            .set(
                mapOf(
                    "id" to memberId,
                    "groupId" to groupId,
                    "userId" to creatorUserId,
                    "userName" to creatorUserName,
                    "joinedAt" to now,
                    "points" to 0
                )
            ).await()
    }

    override suspend fun updateGroup(
        groupId: String,
        groupName: String,
        description: String?,
        creatorUserName: String
    ) {
        val group = groupDao.getGroupById(groupId) ?: return
        val updated = group.copy(
            groupName = groupName,
            description = description,
            createdByUserName = creatorUserName
        )

        groupDao.insert(updated)

        val map = mapOf(
            "groupName" to groupName,
            "description" to description,
            "createdByUserName" to creatorUserName
        )
        groupsRef.document(groupId).update(map).await()
    }

    override suspend fun deleteGroup(groupId: String) {
        // 1. Local delete (Room)
        groupDao.deleteGroupById(groupId)
        groupMemberDao.deleteByGroupId(groupId)
        taskDao.deleteByGroupId(groupId)
        noteDao.deleteByGroupId(groupId)

        // 2. Remote delete (Fire store)
        val groupDoc = groupsRef.document(groupId)
        groupDoc.collection("members").get().await().forEach { it.reference.delete().await() }
        groupDoc.collection("tasks").get().await().forEach { it.reference.delete().await() }
        groupDoc.collection("notes").get().await().forEach { it.reference.delete().await() }
        groupDoc.delete().await()
    }

    override suspend fun getMembers(groupId: String): List<GroupMemberEntity> {
        return groupMemberDao.observeMembers(groupId).first()
    }

    override suspend fun updateMembers(groupId: String, members: List<GroupMemberEntity>) {
        // Delete old members locally + remotely
        groupMemberDao.deleteByGroupId(groupId)
        groupsRef.document(groupId).collection("members").get().await().forEach {
            it.reference.delete().await()
        }

        // Insert updated members
        members.forEach { member ->
            groupMemberDao.insert(member)

            val map = mapOf(
                "id" to member.id,
                "groupId" to member.groupId,
                "userId" to member.userId,
                "userName" to member.userName,
                "joinedAt" to member.joinedAt,
                "points" to member.points
            )
            groupsRef.document(groupId)
                .collection("members")
                .document(member.id)
                .set(map)
                .await()
        }

        // Update memberCount in group
        val count = members.size
        groupDao.getGroupById(groupId)?.let { group ->
            val updated = group.copy(memberCount = count)
            groupDao.insert(updated)
            groupsRef.document(groupId).update("memberCount", count).await()
        }
    }

    override fun getGroup(groupId: String) = groupDao.getGroup(groupId)

    override fun getUserGroups(userId: String): Flow<List<GroupEntity>> {
        return groupMemberDao.observeGroupsForUser(userId)
    }
}