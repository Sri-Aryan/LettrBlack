package com.example.letteblack.repositories

import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupRepositoryImpl(
    private val dao: GroupDao,
    private val firestore: FirebaseFirestore
) : GroupRepository {

    private val groupsRef get() = firestore.collection("groups")

    override fun observeGroups() = dao.observeGroups()

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

        dao.insert(group)

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

    override fun getGroup(groupId: String) = dao.getGroup(groupId)
}