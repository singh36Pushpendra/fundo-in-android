package com.example.fundo.restapi

interface LoginListener {
    fun onLogin(loginResponse: LoginResponse?, status: Boolean)
}