package com.example.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.AuthListener
import com.example.fundo.model.Note
import com.example.fundo.model.NoteAuthService

class NoteViewModel(val noteAuthService: NoteAuthService): ViewModel() {
    private val _noteStatus = MutableLiveData<AuthListener>()
    val noteStatus: LiveData<AuthListener> = _noteStatus

    fun storeNote(note: Note) {
        noteAuthService.storeNote(note) {
            _noteStatus.value = it
        }
    }

}