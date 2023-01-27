package com.example.fundo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.fundo.model.UserAuthService
import com.example.fundo.viewmodel.ProfileDialogViewModel

class ProfileDialogViewModelFactory(val authService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ProfileDialogViewModel(authService) as T
    }
}