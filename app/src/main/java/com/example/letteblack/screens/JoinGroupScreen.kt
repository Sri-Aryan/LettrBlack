package com.example.letteblack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.viewmodel.GroupViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JoinGroupScreen(
    groupId: String,
    userId: String,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val group by viewModel.getGroup(groupId).collectAsState(initial = null)
    val members by viewModel.members(groupId).collectAsState(emptyList())

    // Helper for date formatting
    fun formatDate(time: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(time))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (group == null) {
            Text("Loading group...", style = MaterialTheme.typography.titleLarge)
        } else {
            Text(
                text = group!!.groupName,
                style = MaterialTheme.typography.titleLarge
            )

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
        }

        Divider()

        Text("Members:", style = MaterialTheme.typography.titleMedium)

        if (members.isEmpty()) {
            Text("No members yet.")
        } else {
            members.forEach { member ->
                Text("• ${member.userName} (joined ${formatDate(member.joinedAt)})")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Open Add Note dialog/screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Add Note")
        }
    }
}