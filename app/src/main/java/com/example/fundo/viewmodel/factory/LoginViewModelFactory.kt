package com.example.fundo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.model.UserAuthService
import com.example.fundo.viewmodel.LoginViewModel

class LoginViewModelFactory(var authService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authService) as T
    }
}