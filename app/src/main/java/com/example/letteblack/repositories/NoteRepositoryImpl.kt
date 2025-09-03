package com.example.letteblack.repositories

import com.example.letteblack.db.NoteDao
import com.example.letteblack.db.NoteEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.*

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val firestore: FirebaseFirestore
) : NoteRepository {

    private val collection get() = firestore.collection("notes")

    override fun observeNotes(groupId: String) = dao.observeNotes(groupId)

    override suspend fun addNote(
        groupId: String,
        givenBy: String,
        title: String,
        content: String,
        attachmentUrl: String?
    ) {
        val now = System.currentTimeMillis()
        val note = NoteEntity(
            noteId = UUID.randomUUID().toString(),
            groupId = groupId,
            givenBy = givenBy,
            title = title,
            content = content,
            attachmentUrl = attachmentUrl,
            createdAt = now,
            updatedAt = now
        )

        dao.insert(note)

        val map = mapOf(
            "noteId" to note.noteId,
            "groupId" to note.groupId,
            "givenBy" to note.givenBy,
            "title" to note.title,
            "content" to note.content,
            "attachmentUrl" to note.attachmentUrl,
            "createdAt" to note.createdAt,
            "updatedAt" to note.updatedAt
        )
        collection.document(note.noteId).set(map).await()
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        content: String,
        attachmentUrl: String?
    ) {
        val now = System.currentTimeMillis()

        // 1) Local update (instant UI)
        dao.updateFields(
            noteId = noteId,
            title = title,
            content = content,
            attachmentUrl = attachmentUrl,
            updatedAt = now
        )

        // 2) Remote merge (only provided fields)
        val map = mapOf(
            "title" to title,
            "content" to content,
            "attachmentUrl" to attachmentUrl,
            "updatedAt" to now
        )
        collection.document(noteId).set(map, SetOptions.merge()).await()
    }

    override suspend fun deleteNote(noteId: String) {
        // 1) Local delete (instant UI)
        dao.deleteById(noteId)

        // 2) Remote delete
        collection.document(noteId).delete().await()
    }

    override fun getNoteById(noteId: String): Flow<NoteEntity?> {
        return dao.getNoteById(noteId)
    }
}