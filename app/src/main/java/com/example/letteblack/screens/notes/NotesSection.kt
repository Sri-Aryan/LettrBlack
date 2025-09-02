package com.example.letteblack.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letteblack.db.NoteEntity
import com.example.letteblack.viewmodel.NotesViewModel
import java.util.Date

@Composable
fun NotesSection(
    groupId: String,
    viewModel: NotesViewModel,
    onNoteClick: (NoteEntity) -> Unit
) {
    val notes by viewModel.notes(groupId).collectAsState(emptyList())

    if (notes.isEmpty()) {
        Text("No notes yet.")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            notes.forEach { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNoteClick(note) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(note.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )
                        Text(
                            "By ${note.givenBy} • ${Date(note.createdAt)}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}