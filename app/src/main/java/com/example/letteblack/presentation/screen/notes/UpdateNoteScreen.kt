package com.example.letteblack.presentation.screen.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.data.local.entities.NoteEntity
import com.example.letteblack.viewmodel.NotesViewModel

@Composable
fun UpdateNoteScreen(
    note: NoteEntity,
    viewModel: NotesViewModel = hiltViewModel(),
    onNoteUpdated: () -> Unit
) {
    // Prefill with existing note values
    var title by remember(note.noteId) { mutableStateOf(note.title) }
    var content by remember(note.noteId) { mutableStateOf(note.content) }
    var attachmentUrl by remember(note.noteId) { mutableStateOf(note.attachmentUrl ?: "") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Edit Note", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content (Markdown/Text)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 6
        )

        OutlinedTextField(
            value = attachmentUrl,
            onValueChange = { attachmentUrl = it },
            label = { Text("Attachment URL (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.updateNote(
                    noteId = note.noteId,
                    title = title,
                    content = content,
                    attachmentUrl = attachmentUrl.ifBlank { null }
                )
                onNoteUpdated()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Note")
        }
    }
}