package com.example.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.AuthListener
import com.example.fundo.model.User
import com.example.fundo.model.UserAuthService

class RegisterViewModel(var userAuthService: UserAuthService): ViewModel() {
    private val _userRegisterStatus = MutableLiveData<AuthListener>()
    val userRegisterStatus: LiveData<AuthListener> = _userRegisterStatus

    fun registerUser(user: User) {
        userAuthService.registerUser(user) {
            _userRegisterStatus.value = it
        }
    }
}