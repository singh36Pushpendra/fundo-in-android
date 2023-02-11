package com.example.fundo.restapi

data class LoginResponse(val idToken: String, val email: String, val refreshToken: String, val expiresIn: String, val localId: String, val registered: Boolean)