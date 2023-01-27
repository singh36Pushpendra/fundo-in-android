package com.example.fundo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.model.NoteAuthService
import com.example.fundo.viewmodel.HomeViewModel

class HomeViewModelFactory(val authService: NoteAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(authService) as T
    }
}