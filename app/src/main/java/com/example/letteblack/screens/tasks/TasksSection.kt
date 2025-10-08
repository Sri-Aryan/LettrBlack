package com.example.letteblack.screens.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.viewmodel.TaskViewModel

@Composable
fun TasksSection(
    groupId: String,
    viewModel: TaskViewModel = hiltViewModel(),
    onTaskClick: (String) -> Unit
) {
    val tasks by viewModel.observeTasks(groupId).collectAsState(emptyList())
    val members by viewModel.members(groupId).collectAsState(emptyList())

    if (tasks.isEmpty()) {
        Text("No tasks yet.")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tasks.forEach { task ->
                val assigneeName = members.find { it.userId == task.assigneeId }?.userName ?: "Unknown"
                var isChecked by remember { mutableStateOf(task.status == "complete") }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTaskClick(task.taskId) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(task.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                task.description,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )
                            Text(
                                text = "Assigned to ${task.assigneeName} • Status: ${task.status}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Checkbox(
                            checked = task.status == "complete",
                            onCheckedChange = { checked ->
                                viewModel.updateStatus(
                                    taskId = task.taskId,
                                    newStatus = if (checked) "complete" else "incomplete"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}