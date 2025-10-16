package com.example.letteblack.domain.repository

import com.example.letteblack.data.local.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeNotes(groupId: String): Flow<List<NoteEntity>>

    suspend fun addNote(
        groupId: String,
        givenBy: String,
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

    fun getNoteById(noteId: String): Flow<NoteEntity?>
}