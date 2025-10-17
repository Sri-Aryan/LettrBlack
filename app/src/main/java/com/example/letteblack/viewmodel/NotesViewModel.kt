package com.example.letteblack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letteblack.data.local.entities.GroupMemberEntity
import com.example.letteblack.data.local.entities.NoteEntity
import com.example.letteblack.domain.repository.GroupMemberRepository
import com.example.letteblack.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repo: NoteRepository,
    private val memberRepo: GroupMemberRepository
) : ViewModel() {

    fun notes(groupId: String): Flow<List<NoteEntity>> =
        repo.observeNotes(groupId)

    fun members(groupId: String): Flow<List<GroupMemberEntity>> =
        memberRepo.observeMembers(groupId)

    fun addNote(
        groupId: String,
        givenBy: String,
        title: String,
        content: String,
        attachmentUrl: String?,
    ) {
        viewModelScope.launch {
            repo.addNote(groupId, givenBy, title, content, attachmentUrl)
        }
    }

    fun updateNote(
        noteId: String,
        title: String,
        content: String,
        attachmentUrl: String?
    ) = viewModelScope.launch {
        repo.updateNote(noteId, title, content, attachmentUrl)
    }

    fun deleteNote(noteId: String) = viewModelScope.launch {
        repo.deleteNote(noteId)
    }

    fun getNoteById(noteId: String): Flow<NoteEntity?> {
        return repo.getNoteById(noteId)
    }

}