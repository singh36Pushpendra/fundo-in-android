package com.example.fundo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.model.NoteAuthService
import com.example.fundo.viewmodel.NoteViewModel

class NoteViewModelFactory(val authService: NoteAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(authService) as T
    }
}