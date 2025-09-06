package com.example.letteblack.repositories

import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.db.NoteDao
import com.example.letteblack.db.TaskDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupRepositoryImpl(
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
        val group = GroupEntity(
            groupId = UUID.randomUUID().toString(),
            groupName = groupName,
            description = description,
            createdByUserId = creatorUserId,
            createdByUserName = creatorUserName,
            createdAt = now,
            memberCount = 1
        )

        groupDao.insert(group)

        val map = mapOf(
            "groupId" to group.groupId,
            "groupName" to group.groupName,
            "description" to group.description,
            "createdByUserId" to group.createdByUserId,
            "createdByUserName" to group.createdByUserName,
            "createdAt" to group.createdAt,
            "memberCount" to group.memberCount
        )
        groupsRef.document(group.groupId).set(map).await()
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

        // 2. Remote delete (Firestore)
        val groupDoc = groupsRef.document(groupId)

        // Delete subcollections
        groupDoc.collection("members").get().await().forEach { it.reference.delete().await() }
        groupDoc.collection("tasks").get().await().forEach { it.reference.delete().await() }
        groupDoc.collection("notes").get().await().forEach { it.reference.delete().await() }

        // Finally delete group itself
        groupDoc.delete().await()
    }

    override suspend fun getMembers(groupId: String): List<GroupMemberEntity> {
        return groupMemberDao.observeMembers(groupId) // Flow
            .let { flow ->
                flow.first() // collect once
            }
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
                "joinedAt" to member.joinedAt
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
}