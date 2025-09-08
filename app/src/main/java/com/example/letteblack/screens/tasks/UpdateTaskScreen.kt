package com.example.letteblack.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.db.TaskEntity
import com.example.letteblack.viewmodel.TaskViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTaskScreen(
    task: TaskEntity,
    groupId: String,
    viewModel: TaskViewModel,
    onTaskUpdated: () -> Unit,
    onCancel: () -> Unit
) {
    var title by remember(task.taskId) { mutableStateOf(task.title) }
    var description by remember(task.taskId) { mutableStateOf(task.description) }
    var assigneeId by remember(task.taskId) { mutableStateOf(task.assigneeId) }
    var points by remember(task.taskId) { mutableStateOf(task.pointsRewarded.toString()) }

    // Members dropdown
    var expanded by remember { mutableStateOf(false) }
    val members by viewModel.members(groupId).collectAsState(initial = emptyList())
    var selectedMember by remember(task.taskId) {
        mutableStateOf<GroupMemberEntity?>(null)
    }

    // Initialize selected member from task.assigneeId
    LaunchedEffect(members, task.assigneeId) {
        if (selectedMember == null) {
            selectedMember = members.find { it.userId == task.assigneeId }
        }
    }

    // Date picker
    var showDatePicker by remember { mutableStateOf(false) }
    var dueDateMillis by remember(task.taskId) { mutableStateOf(task.dueDate) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Update Task", style = MaterialTheme.typography.titleLarge)

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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedMember?.userName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Assigned To") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member.userName) },
                        onClick = {
                            selectedMember = member
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = points,
            onValueChange = { points = it },
            label = { Text("Points Rewarded") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = dueDateMillis?.let { SimpleDateFormat("dd MMM yyyy").format(it) }
                    ?: "Pick Due Date"
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.updateTask(
                        taskId = task.taskId,
                        title = title,
                        description = description,
                        dueDate = dueDateMillis,
                        pointsRewarded = points.toIntOrNull() ?: 0,
                        assigneeId = selectedMember!!.userId
                    )
                    onTaskUpdated()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDateMillis)
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