package com.example.letteblack.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letteblack.db.NoteEntity
import com.example.letteblack.viewmodel.NotesViewModel
import java.util.Date

@Composable
fun NoteDetailScreen(
    note: NoteEntity,
    viewModel: NotesViewModel,
    onEdit: (NoteEntity) -> Unit, // navigate to UpdateNoteScreen
    onDeleted: () -> Unit         // e.g., navController.popBackStack()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onEdit(note) }) {
                Text("Edit")
            }

            OutlinedButton(onClick = { showDeleteDialog = true }) {
                Text("Delete")
            }
        }

        Text(note.title, style = MaterialTheme.typography.titleLarge)
        Text(
            "By ${note.authorId} • ${Date(note.createdAt)}",
            style = MaterialTheme.typography.labelMedium
        )

        Divider()

        Text(note.content, style = MaterialTheme.typography.bodyMedium)

        note.attachmentUrl?.let {
            Divider()
            Text("Attachment: $it", style = MaterialTheme.typography.labelSmall)
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete note?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteNote(note.noteId)
                    onDeleted()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}