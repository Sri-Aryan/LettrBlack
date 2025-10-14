package com.example.letteblack.screens.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.screens.notes.NotesSection
import com.example.letteblack.screens.tasks.TasksSection
import com.example.letteblack.viewmodel.GroupViewModel
import com.example.letteblack.viewmodel.NotesViewModel
import com.example.letteblack.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JoinGroupScreen(
    groupId: String,
    userId: String,
    navController: NavHostController,
    onAddNoteClick: (String, String) -> Unit,
    onAddTaskClick: (String, String) -> Unit,
    groupViewModel: GroupViewModel = hiltViewModel(),
    notesViewModel: NotesViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val group by groupViewModel.getGroup(groupId).collectAsState(initial = null)
    val members by groupViewModel.members(groupId).collectAsState(emptyList())

    var selectedTab by remember { mutableIntStateOf(0) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun formatDate(time: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        onAddNoteClick(groupId, userId)
                    } else {
                        onAddTaskClick(groupId, userId)
                    }
                }
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group Info
            if (group == null) {
                Text("Loading group...", style = MaterialTheme.typography.titleLarge)
            } else {
                Text(group!!.groupName, style = MaterialTheme.typography.titleLarge)

                if (!group!!.description.isNullOrBlank()) {
                    Text(group!!.description!!, style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = "👤 ${group!!.createdByUserName} • ${group!!.memberCount} members",
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "Created at: ${formatDate(group!!.createdAt)}",
                    style = MaterialTheme.typography.labelSmall
                )

                // 🔹 Update & Delete buttons
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { showUpdateDialog = true }) {
                        Text("Update Group")
                    }
                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Group")
                    }
                }
            }

            Text("Members", style = MaterialTheme.typography.titleMedium)

            if (members.isEmpty()) {
                Text("No members yet", style = MaterialTheme.typography.bodySmall)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    members.forEach { member ->
                        Text("👤 ${member.userName}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Divider()

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Notes") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Tasks") }
                )
            }

            when (selectedTab) {
                0 -> NotesSection(
                    groupId = groupId,
                    viewModel = notesViewModel,
                    onNoteClick = { note ->
                        navController.navigate("group/$groupId/noteDetail/${note.noteId}")
                    }
                )
                1 -> TasksSection(
                    groupId = groupId,
                    viewModel = taskViewModel,
                    onTaskClick = { taskId ->
                        navController.navigate("group/$groupId/taskDetail/$taskId")
                    }
                )
            }
        }

        // 🔹 Update Group Dialog
        if (showUpdateDialog && group != null) {
            UpdateGroupDialog(
                group = group!!,
                members = members,
                onDismiss = { showUpdateDialog = false },
                onConfirm = { name, desc, creatorName, updatedMembers ->
                    groupViewModel.updateGroupWithMembers(
                        groupId,
                        name,
                        desc,
                        creatorName,
                        updatedMembers
                    )
                    showUpdateDialog = false
                }
            )
        }

        // 🔹 Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            groupViewModel.deleteGroup(groupId)
                            showDeleteDialog = false
                            navController.popBackStack() // go back after delete
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete Group") },
                text = { Text("Are you sure you want to delete this group and all its members, tasks, and notes?") }
            )
        }
    }
}

@Composable
fun UpdateGroupDialog(
    group: GroupEntity,
    members: List<GroupMemberEntity>,
    onDismiss: () -> Unit,
    onConfirm: (String, String?, String, List<GroupMemberEntity>) -> Unit
) {
    var name by remember { mutableStateOf(group.groupName) }
    var description by remember { mutableStateOf(group.description ?: "") }
    val creatorName = group.createdByUserName
    val updatedMembers = remember { members.toMutableStateList() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Group") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Group Name") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )

                OutlinedTextField(
                    value = creatorName,
                    onValueChange = {},
                    label = { Text("Creator Name") },
                    enabled = false,
                    readOnly = true
                )

                Text("Members", style = MaterialTheme.typography.titleSmall)
                updatedMembers.forEachIndexed { index, member ->
                    OutlinedTextField(
                        value = member.userName,
                        onValueChange = { updatedMembers[index] = member.copy(userName = it) },
                        label = { Text("Member ${index + 1}") }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, description, creatorName, updatedMembers) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}