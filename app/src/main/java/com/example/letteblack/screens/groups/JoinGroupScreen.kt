package com.example.letteblack.screens.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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

    var selectedTab by remember { mutableStateOf(0) }

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

            // Tab Content
            // Tab Content
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
    }
}