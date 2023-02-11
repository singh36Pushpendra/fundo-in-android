package com.example.fundo.restapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("./accounts:signInWithCustomToken?key=AIzaSyDTpKD8HTdu63j2iqSN211z1nO8d_VjEO8")
    fun loginWithRestApi(@Body request: LoginRequest): Call<LoginResponse>
}