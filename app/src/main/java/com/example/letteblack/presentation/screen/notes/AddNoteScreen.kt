package com.example.letteblack.presentation.screen.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.letteblack.data.local.entities.GroupMemberEntity
import com.example.letteblack.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    groupId: String,
    userId: String,
    viewModel: NotesViewModel = hiltViewModel(),
    onNoteSaved: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var attachmentUrl by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<GroupMemberEntity?>(null) }

    val members by viewModel.members(groupId).collectAsState(initial = emptyList())


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add New Note", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content (Markdown/Text)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 6
        )

        OutlinedTextField(
            value = attachmentUrl,
            onValueChange = { attachmentUrl = it },
            label = { Text("Attachment URL (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedMember?.userName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Given By") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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

        Button(
            onClick = {
                if (selectedMember != null) {
                    viewModel.addNote(
                        groupId = groupId,
                        title = title,
                        content = content,
                        attachmentUrl = attachmentUrl.ifBlank { null },
                        givenBy = selectedMember!!.userName
                    )
                    onNoteSaved()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedMember != null
        ) {
            Text("Save Note")
        }
    }
}