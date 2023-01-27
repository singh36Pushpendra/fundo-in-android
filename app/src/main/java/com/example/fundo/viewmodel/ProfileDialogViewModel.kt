package com.example.fundo.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundo.model.AuthListener
import com.example.fundo.model.User
import com.example.fundo.model.UserAuthListener
import com.example.fundo.model.UserAuthService

class ProfileDialogViewModel(val authService: UserAuthService): ViewModel() {
    private val _userProfileStatus = MutableLiveData<UserAuthListener>()
    val userProfileStatus: LiveData<UserAuthListener> = _userProfileStatus

    private val _userProfilePicStatus = MutableLiveData<AuthListener>()
    val userProfilePicStatus: LiveData<AuthListener> = _userProfilePicStatus

    fun getUser() {
        authService.getUser {
            _userProfileStatus.value = it
        }
    }

    fun uploadProfilePic(selectedImg: Uri) {
        authService.uploadProfilePic(selectedImg) {
            _userProfilePicStatus.value = it
        }
    }
}