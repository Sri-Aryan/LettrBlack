package com.example.letteblack.repositories

import com.example.letteblack.db.GroupDao
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.db.GroupMemberDao
import com.example.letteblack.db.GroupMemberEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class GroupRepositoryImpl(
    private val dao: GroupDao,
    private val memberDao: GroupMemberDao,
    private val firestore: FirebaseFirestore
) : GroupRepository {

    private val groupsRef get() = firestore.collection("groups")
    private val membersRef get() = firestore.collection("group_members")

    override fun observeGroups() = dao.observeGroups()

    override suspend fun createGroup(name: String, creatorUserId: String, creatorUserName: String) {
        val groupId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        val group = GroupEntity(id = groupId, name = name, createdAt = now)
        dao.insert(group)

        // auto add creator as first member, with name
        val member = GroupMemberEntity(
            id = "${groupId}_$creatorUserId",
            groupId = groupId,
            userId = creatorUserId,
            userName = creatorUserName,   // include name
            joinedAt = now
        )
        memberDao.insert(member)

        groupsRef.document(group.id).set(group).await()
        membersRef.document(member.id).set(member).await()
    }

    override fun getGroup(groupId: String) = dao.getGroup(groupId)
}