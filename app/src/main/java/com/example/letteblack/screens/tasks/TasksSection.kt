package com.example.letteblack.screens.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.viewmodel.TaskViewModel

@Composable
fun TasksSection(
    groupId: String,
    viewModel: TaskViewModel = hiltViewModel(),
    onTaskClick: (String) -> Unit   // pass clicked taskId
) {
    val tasks by viewModel.observeTasks(groupId).collectAsState(emptyList())

    if (tasks.isEmpty()) {
        Text("No tasks yet.")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tasks.forEach { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTaskClick(task.taskId) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(task.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )
                        Text(
                            "Assigned to ${task.assigneeId} • Status: ${task.status}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}