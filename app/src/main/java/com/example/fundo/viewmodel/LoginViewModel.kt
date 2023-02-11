package com.example.fundo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.AuthListener
import com.example.fundo.model.User
import com.example.fundo.model.UserAuthService

class LoginViewModel(val userAuthService: UserAuthService): ViewModel() {
    private val _userLoginStatus = MutableLiveData<AuthListener>()
    val userLoginStatus: LiveData<AuthListener> = _userLoginStatus

//    fun loginUser(user: User) {
//        userAuthService.loginUser(user) {
//            _userLoginStatus.value = it
//        }
//    }

    fun loginWithRestApi(email: String, password: String) {
        userAuthService.apiLogin(email, password) {
            _userLoginStatus.value = it
        }
    }
}