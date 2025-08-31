package com.example.letteblack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letteblack.db.TaskEntity
import com.example.letteblack.viewmodel.TaskViewModel
import java.util.*

@Composable
fun TaskDetailScreen(
    task: TaskEntity,
    viewModel: TaskViewModel,
    onEdit: (TaskEntity) -> Unit,
    onDeleted: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ---- Actions ----
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onEdit(task) }) {
                Text("Edit")
            }
            OutlinedButton(onClick = { showDeleteDialog = true }) {
                Text("Delete")
            }
        }

        // ---- Task Info ----
        Text(task.title, style = MaterialTheme.typography.titleLarge)
        Text(
            "Assigned to ${task.assigneeId} • Status: ${task.status}",
            style = MaterialTheme.typography.labelMedium
        )
        Text("Reward: ${task.pointsRewarded} points", style = MaterialTheme.typography.labelSmall)

        task.dueDate?.let {
            Text("Due: ${Date(it)}", style = MaterialTheme.typography.labelSmall)
        }

        Divider()

        Text(task.description, style = MaterialTheme.typography.bodyMedium)
    }

    // ---- Delete Confirmation Dialog ----
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete task?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteTask(task.taskId)
                    onDeleted()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}