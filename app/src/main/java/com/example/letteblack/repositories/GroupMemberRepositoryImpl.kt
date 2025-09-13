package com.example.letteblack.repositories

import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.GroupMemberEntity
import com.google.firebase.firestore.FieldValue.increment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class GroupMemberRepositoryImpl(
    private val dao: GroupMemberDao,
    private val groupDao: GroupDao,
    private val firestore: FirebaseFirestore
) : GroupMemberRepository {

    private val collection get() = firestore.collection("group_members")

    override fun observeMembers(groupId: String) = dao.observeMembers(groupId)

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

        dao.insert(member)

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

    override suspend fun addPointsToMember(memberId: String, points: Int) {
        // Update Room
        dao.addPoints(memberId, points)

        val docRef = collection.document(memberId)
        docRef.update("points", increment(points.toLong())).await()
    }

    override fun observeMembersByPoints(groupId: String): Flow<List<GroupMemberEntity>> {
        return dao.observeMembersSortedByPoints(groupId)
    }

    override fun getMemberById(memberId: String): Flow<GroupMemberEntity?> {
        return dao.getMemberByIdFlow(memberId)
    }
}