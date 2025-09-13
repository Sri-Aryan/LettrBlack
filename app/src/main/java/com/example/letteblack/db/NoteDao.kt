package com.example.letteblack.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun observeNotes(groupId: String): Flow<List<NoteEntity>>

    @Query("""
        UPDATE notes 
        SET title = :title, 
            content = :content, 
            attachmentUrl = :attachmentUrl, 
            updatedAt = :updatedAt
        WHERE noteId = :noteId
    """)
    suspend fun updateFields(
        noteId: String,
        title: String,
        content: String,
        attachmentUrl: String?,
        updatedAt: Long
    )

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteById(noteId: String)

    @Query("SELECT * FROM notes WHERE noteId = :noteId LIMIT 1")
    fun getNoteById(noteId: String): Flow<NoteEntity?>

    @Query("DELETE FROM notes WHERE groupId = :groupId")
    suspend fun deleteByGroupId(groupId: String)

}