package com.example.letteblack.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupMemberDao {

    @Query("SELECT * FROM group_members WHERE groupId = :groupId")
    fun observeMembers(groupId: String): Flow<List<GroupMemberEntity>>

    @Query("SELECT * FROM group_members WHERE groupId = :groupId ORDER BY points DESC")
    fun observeMembersSortedByPoints(groupId: String): Flow<List<GroupMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: GroupMemberEntity)

    @Query("UPDATE group_members SET points = points + :points WHERE id = :memberId")
    suspend fun addPoints(memberId: String, points: Int)

    @Query("SELECT * FROM group_members WHERE id = :memberId LIMIT 1")
    fun getMemberByIdFlow(memberId: String): Flow<GroupMemberEntity?>

    @Query("DELETE FROM group_members WHERE groupId = :groupId")
    suspend fun deleteByGroupId(groupId: String)

    @Query("""
        SELECT g.* FROM groups g 
        INNER JOIN group_members m ON g.groupId = m.groupId 
        WHERE m.userId = :userId
        """)
    fun observeGroupsForUser(userId: String): Flow<List<GroupEntity>>
}