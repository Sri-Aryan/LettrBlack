package com.example.letteblack.repositories

import com.example.letteblack.db.NoteDao
import com.example.letteblack.db.NoteEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val firestore: FirebaseFirestore
) : NoteRepository {

    private val collection get() = firestore.collection("notes")

    override fun observeNotes(groupId: String) = dao.observeNotes(groupId)

    override suspend fun addNote(groupId: String, authorId: String, title: String, content: String) {
        val now = System.currentTimeMillis()
        val note = NoteEntity(
            noteId = UUID.randomUUID().toString(),
            groupId = groupId,
            authorId = authorId,
            title = title,
            content = content,
            createdAt = now,
            updatedAt = now
        )

        // 1) Local insert
        dao.insert(note)

        // 2) Remote write
        val map = mapOf(
            "noteId" to note.noteId,
            "groupId" to note.groupId,
            "authorId" to note.authorId,
            "title" to note.title,
            "content" to note.content,
            "createdAt" to note.createdAt,
            "updatedAt" to note.updatedAt
        )
        collection.document(note.noteId).set(map).await()
    }
}