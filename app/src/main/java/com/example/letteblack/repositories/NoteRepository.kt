package com.example.letteblack.repositories

import com.example.letteblack.db.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeNotes(groupId: String): Flow<List<NoteEntity>>

    suspend fun addNote(
        groupId: String,
        authorId: String,
        title: String,
        content: String,
        attachmentUrl: String ?
    )

    suspend fun updateNote(
        noteId: String,
        title: String,
        content: String,
        attachmentUrl: String?
    )

    suspend fun deleteNote(noteId: String)
}