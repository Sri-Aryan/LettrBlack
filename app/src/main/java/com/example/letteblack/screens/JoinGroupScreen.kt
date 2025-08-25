package com.example.letteblack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.viewmodel.GroupViewModel

@Composable
fun JoinGroupScreen(
    groupId: String,
    userId: String,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val members by viewModel.members(groupId).collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Group: $groupId", style = MaterialTheme.typography.titleLarge)

        Button(onClick = { viewModel.joinGroup(groupId, userId) }) {
            Text("Join Group")
        }

        Divider()

        Text("Members:", style = MaterialTheme.typography.titleMedium)
        members.forEach { member ->
            Text("• ${member.userId} (joined ${member.joinedAt})")
        }
    }
}