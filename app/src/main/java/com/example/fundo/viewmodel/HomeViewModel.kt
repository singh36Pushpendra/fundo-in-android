package com.example.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.AuthListener
import com.example.fundo.model.NoteAuthListener
import com.example.fundo.model.NoteAuthService

class HomeViewModel(val noteAuthService: NoteAuthService): ViewModel() {
    private val _notesStatus = MutableLiveData<NoteAuthListener>()
    val notesStatus: LiveData<NoteAuthListener> = _notesStatus

    private val _noteDeletionStatus = MutableLiveData<AuthListener>()
    val noteDeletionStatus: LiveData<AuthListener> = _noteDeletionStatus

    private val _noteArchivedStatus = MutableLiveData<AuthListener>()
    val noteArchivedStatus: LiveData<AuthListener> = _noteArchivedStatus

    private val _noteUnarchivedStatus = MutableLiveData<AuthListener>()
    val noteUnarchivedStatus: LiveData<AuthListener> = _noteUnarchivedStatus

    fun getNotes() {
        noteAuthService.getNotes() {
            _notesStatus.value = it
        }
    }

    fun deleteNote(noteId: String) {
        noteAuthService.deleteNote(noteId) {
            _noteDeletionStatus.value = it
        }
    }

    fun archiveNote(noteId: String) {
        noteAuthService.archiveNote(noteId) {
            _noteArchivedStatus.value = it
        }
    }

    fun unarchiveNote(noteId: String) {
        noteAuthService.unarchiveNote(noteId) {
            _noteUnarchivedStatus.value = it
        }
    }
}