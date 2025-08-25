package com.example.letteblack.repositories

import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.GroupMemberEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GroupMemberRepositoryImpl(
    private val dao: GroupMemberDao,
    private val firestore: FirebaseFirestore
) : GroupMemberRepository {

    private val collection get() = firestore.collection("group_members")

    override fun observeMembers(groupId: String) = dao.observeMembers(groupId)

    override suspend fun joinGroup(groupId: String, userId: String, userName: String) {
        val now = System.currentTimeMillis()
        val id = "${groupId}_$userId"

        val member = GroupMemberEntity(
            id = id,
            groupId = groupId,
            userId = userId,
            userName = userName,
            joinedAt = now
        )

        dao.insert(member)

        val map = mapOf(
            "id" to member.id,
            "groupId" to member.groupId,
            "userId" to member.userId,
            "userName" to member.userName,
            "joinedAt" to member.joinedAt
        )
        collection.document(member.id).set(map).await()
    }
}