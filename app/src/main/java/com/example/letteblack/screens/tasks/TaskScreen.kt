package com.example.letteblack.screens.tasks

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
    val members by viewModel.members(task.groupId).collectAsState(initial = emptyList())
    val assigneeName = members.find { it.userId == task.assigneeId }?.userName ?: "Unknown"

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onEdit(task) }) {
                Text("Edit")
            }
            OutlinedButton(onClick = { showDeleteDialog = true }) {
                Text("Delete")
            }
        }

        Text(task.title, style = MaterialTheme.typography.titleLarge)
        Text("Assigned to $assigneeName • Status: ${task.status}")
        Text("Reward: ${task.pointsRewarded} points")

        task.dueDate?.let { Text("Due: ${Date(it)}") }

        Divider()

        Text(task.description)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete task?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
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