package com.example.letteblack.screens.groups

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.db.GroupEntity
import com.example.letteblack.viewmodel.GroupViewModel
import com.example.letteblack.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun GroupListScreen(
    userId: String,
    onGroupClick: (String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val groups by viewModel.groups().collectAsState(initial = emptyList())

    // Dialog states
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<String?>(null) }

    var showCreateDialog by remember { mutableStateOf(false) }
    var newGroupName by remember { mutableStateOf("") }
    var newGroupDescription by remember { mutableStateOf("") }

    // Current user info
    var currentUserName by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    // Fetch name from local Room (AuthViewModel)
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser { user ->
            user?.let {
                currentUserName = it.name
            }
        }
    }

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
                        Text(group.description, style = MaterialTheme.typography.bodySmall)
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
                            Icon(Icons.Default.Add, contentDescription = "Join")
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

    // Join Group Dialog
    if (showJoinDialog && selectedGroupId != null) {
        val selectedGroup = groups.find { it.groupId == selectedGroupId }
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Join Group") },
            text = {
                Column {
                    OutlinedTextField(
                        value = selectedGroup?.groupName ?: "",
                        onValueChange = {},
                        label = { Text("Group Name") },
                        enabled = false
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = currentUserName,
                        onValueChange = {},
                        label = { Text("Your Name") },
                        enabled = false // not editable
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.joinGroup(selectedGroupId!!, userId, currentUserName) {
                            Toast.makeText(context, "You are already a member of this group", Toast.LENGTH_SHORT).show()
                        }
                        showJoinDialog = false
                    }
                ) {
                    Text("Join")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Create Group Dialog
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
                        value = currentUserName,
                        onValueChange = {},
                        label = { Text("Created By") },
                        enabled = false // readonly field
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newGroupName.isNotBlank()) {
                            scope.launch {
                                viewModel.createGroup(
                                    newGroupName,
                                    newGroupDescription,
                                    userId,
                                    currentUserName
                                )
                                newGroupName = ""
                                newGroupDescription = ""
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