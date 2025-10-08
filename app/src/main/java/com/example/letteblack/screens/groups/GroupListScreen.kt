package com.example.letteblack.screens.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.viewmodel.GroupViewModel
import kotlinx.coroutines.launch

@Composable
fun GroupListScreen(
    userId: String,
    onGroupClick: (String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val groups by viewModel.groups().collectAsState(initial = emptyList())

    // state for joining existing group
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf("") }

    // state for creating new group
    var showCreateDialog by remember { mutableStateOf(false) }
    var newGroupName by remember { mutableStateOf("") }
    var newGroupDescription by remember { mutableStateOf("") }
    var creatorName by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Groups", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        groups.forEach { group: GroupEntity ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onGroupClick(group.groupId) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(group.groupName, style = MaterialTheme.typography.titleMedium)

                    if (!group.description.isNullOrBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            group.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "👤 ${group.createdByUserName} • ${group.memberCount} members",
                            style = MaterialTheme.typography.labelSmall
                        )
                        IconButton(
                            onClick = {
                                selectedGroupId = group.groupId
                                showJoinDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Join"
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { showCreateDialog = true }) {
            Text("➕ Create New Group")
        }
    }

    // Dialog for joining existing group
    if (showJoinDialog && selectedGroupId != null) {
        val selectedGroup = groups.find { it.groupId == selectedGroupId }
        AlertDialog(
            onDismissRequest = {
                showJoinDialog = false
                userName = ""
            },
            title = { Text("Join Group") },
            text = {
                Column {
                    // Prefilled Group Name (read only)
                    OutlinedTextField(
                        value = selectedGroup?.groupName ?: "",
                        onValueChange = {},
                        label = { Text("Group Name") },
                        enabled = false
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Enter your name") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (userName.isNotBlank()) {
                            val nameToSave = userName
                            scope.launch {
                                viewModel.joinGroup(selectedGroupId!!, userId, nameToSave)
                            }
                            // reset AFTER launch
                            userName = ""
                            showJoinDialog = false
                        }
                    }
                ) {
                    Text("Join")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showJoinDialog = false
                        userName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog for creating new group
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New Group") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text("Group Name") }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newGroupDescription,
                        onValueChange = { newGroupDescription = it },
                        label = { Text("Description") }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = creatorName,
                        onValueChange = { creatorName = it },
                        label = { Text("Your Name") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newGroupName.isNotBlank() && creatorName.isNotBlank()) {
                            scope.launch {
                                viewModel.createGroup(
                                    newGroupName,
                                    newGroupDescription,
                                    userId,
                                    creatorName
                                )
                                // reset AFTER insertion
                                newGroupName = ""
                                newGroupDescription = ""
                                creatorName = ""
                                showCreateDialog = false
                            }
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}