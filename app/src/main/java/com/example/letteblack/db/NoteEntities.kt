package com.example.letteblack.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val noteId: String,
    val groupId: String,
    val authorId: String,
    val title: String,
    val content: String,
    val attachmentUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)