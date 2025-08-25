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
    val group by viewModel.getGroup(groupId).collectAsState(initial = null)
    val members by viewModel.members(groupId).collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Group: ${group?.name ?: "..."}",  // show name, not UUID
            style = MaterialTheme.typography.titleLarge
        )

        Divider()

        Text("Members:", style = MaterialTheme.typography.titleMedium)
        members.forEach { member ->
            Text("• ${member.userName} (joined ${member.joinedAt})") // show username
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /*TODO*/ }) {
            Text(text = "Add Note")
        }
    }
}