package com.example.fundo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.model.UserAuthService
import com.example.fundo.viewmodel.RegisterViewModel

class RegisterViewModelFactory(var authService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(authService) as T
    }
}