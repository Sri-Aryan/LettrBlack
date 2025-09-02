package com.example.letteblack.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letteblack.viewmodel.TaskViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    groupId: String,
    assignerId: String,
    assigneeId: String,
    viewModel: TaskViewModel,
    onTaskSaved: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }

    // Date picker
    var showDatePicker by remember { mutableStateOf(false) }
    var dueDateMillis by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = points,
            onValueChange = { points = it },
            label = { Text("Reward Points") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = dueDateMillis?.let { SimpleDateFormat("dd MMM yyyy").format(it) }
                    ?: "Pick Due Date"
            )
        }

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    viewModel.assignTask(
                        groupId = groupId,
                        assignerId = assignerId,
                        assigneeId = assigneeId,
                        title = title.trim(),
                        description = description.trim(),
                        pointsRewarded = points.toIntOrNull() ?: 0,
                        dueDate = dueDateMillis
                    )
                    onTaskSaved()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Task")
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dueDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}