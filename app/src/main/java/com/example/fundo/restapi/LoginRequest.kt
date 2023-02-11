package com.example.fundo.restapi

data class LoginRequest(val email: String, val password: String, val returnSecureToken: Boolean)