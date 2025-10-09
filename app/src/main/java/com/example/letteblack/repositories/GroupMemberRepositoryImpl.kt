package com.example.letteblack.repositories

import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.GroupMemberEntity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupMemberRepositoryImpl(
    private val groupMemberDao: GroupMemberDao,
    private val groupDao: GroupDao,
    private val firestore: FirebaseFirestore
) : GroupMemberRepository {

    private val collection get() = firestore.collection("group_members")

    override fun observeMembers(groupId: String) = groupMemberDao.observeMembers(groupId)

    override suspend fun joinGroup(
        groupId: String,
        userId: String,
        userName: String
    ) {
        val now = System.currentTimeMillis()
        val member = GroupMemberEntity(
            id = UUID.randomUUID().toString(),
            groupId = groupId,
            userId = userId,
            userName = userName,
            joinedAt = now
        )

        groupMemberDao.insert(member)

        val group = groupDao.getGroupById(groupId)
        if (group != null) {
            val updated = group.copy(memberCount = group.memberCount + 1)
            groupDao.insert(updated) // since it's REPLACE, it updates
        }

        val map = mapOf(
            "id" to member.id,
            "groupId" to member.groupId,
            "userId" to member.userId,
            "userName" to member.userName,
            "joinedAt" to member.joinedAt
        )
        collection.document(member.id).set(map).await()
    }

    override fun observeMembersByPoints(groupId: String): Flow<List<GroupMemberEntity>> {
        return groupMemberDao.observeMembersSortedByPoints(groupId)
    }

    override fun getMemberById(memberId: String): Flow<GroupMemberEntity?> {
        return groupMemberDao.getMemberByIdFlow(memberId)
    }

    override suspend fun addPointsToMember(groupId: String, userId: String, points: Int) {
        // Update Room
        groupMemberDao.addPoints(groupId, userId, points)

        // Update Firebase
        val snapshot = collection
            .whereEqualTo("groupId", groupId)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            val docId = snapshot.documents.first().id
            collection.document(docId)
                .update("points", FieldValue.increment(points.toLong()))
                .await()
        }
    }
}