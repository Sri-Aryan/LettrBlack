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
import com.example.letteblack.db.GroupMemberEntity
import com.example.letteblack.viewmodel.GroupViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.letteblack.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.UUID

@Composable
fun GroupListScreen(
    userId: String,
    onGroupClick: (String) -> Unit,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val groups by viewModel.groups().collectAsState(initial = emptyList())

    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf("") }

    var showCreateDialog by remember { mutableStateOf(false) }
    var newGroupName by remember { mutableStateOf("") }
    var newGroupDescription by remember { mutableStateOf("") }
    var creatorName by remember { mutableStateOf("") }

    // members editing as GroupMemberEntity
    var members by remember { mutableStateOf(listOf<GroupMemberEntity>()) }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Groups", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        groups.forEach { group: GroupEntity ->

            val deleteAction = SwipeAction(
                onSwipe = { viewModel.deleteGroup(group.groupId) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                background = Color.Red
            )

            val editAction = SwipeAction(
                onSwipe = {
                    selectedGroupId = group.groupId
                    newGroupName = group.groupName
                    newGroupDescription = group.description ?: ""
                    creatorName = group.createdByUserName

                    scope.launch {
                        val m = viewModel.getGroupMembers(group.groupId)
                        members = m // full entities
                    }

                    showCreateDialog = true
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_edit_24),
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                background = Color.Blue
            )

            SwipeableActionsBox(
                startActions = listOf(editAction),
                endActions = listOf(deleteAction)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onGroupClick(group.groupId) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                            IconButton(onClick = {
                                selectedGroupId = group.groupId
                                showJoinDialog = true
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Join")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            selectedGroupId = null
            newGroupName = ""
            newGroupDescription = ""
            creatorName = ""
            members = listOf()
            showCreateDialog = true
        }) {
            Text("➕ Create New Group")
        }
    }

    // Join Group dialog
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
                TextButton(onClick = {
                    if (userName.isNotBlank()) {
                        scope.launch {
                            viewModel.joinGroup(
                                selectedGroupId!!,
                                userId,
                                userName
                            )
                        }
                        userName = ""
                        showJoinDialog = false
                    }
                }) { Text("Join") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showJoinDialog = false
                    userName = ""
                }) { Text("Cancel") }
            }
        )
    }

    // Create/Update dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text(if (selectedGroupId == null) "New Group" else "Edit Group") },
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

                    Spacer(Modifier.height(12.dp))
                    Text("Group Members", style = MaterialTheme.typography.titleMedium)

                    members.forEachIndexed { index, member ->
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = member.userName,
                                onValueChange = { newName ->
                                    members = members.toMutableList().apply {
                                        this[index] = member.copy(userName = newName)
                                    }
                                },
                                label = { Text("Member ${index + 1}") },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                members = members.toMutableList().apply { removeAt(index) }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_delete_24),
                                    contentDescription = "Remove"
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        members = members + GroupMemberEntity(
                            id = UUID.randomUUID().toString(),
                            groupId = selectedGroupId ?: "temp",
                            userId = UUID.randomUUID().toString(),
                            userName = "",
                            joinedAt = System.currentTimeMillis()
                        )
                    }) {
                        Text("➕ Add Member")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newGroupName.isNotBlank() && creatorName.isNotBlank()) {
                        scope.launch {
                            if (selectedGroupId == null) {
                                viewModel.createGroup(
                                    newGroupName,
                                    newGroupDescription,
                                    userId,
                                    creatorName
                                )
                                // members added after creation
                                // (optional: auto insert)
                            } else {
                                viewModel.updateGroupWithMembers(
                                    selectedGroupId!!,
                                    newGroupName,
                                    newGroupDescription,
                                    creatorName,
                                    members
                                )
                            }
                            newGroupName = ""
                            newGroupDescription = ""
                            creatorName = ""
                            members = emptyList()
                            selectedGroupId = null
                            showCreateDialog = false
                        }
                    }
                }) {
                    Text(if (selectedGroupId == null) "Create" else "Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            }
        )
    }
}