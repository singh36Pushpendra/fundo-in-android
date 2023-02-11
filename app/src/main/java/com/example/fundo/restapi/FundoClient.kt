package com.example.fundo.restapi

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASEURL = "https://identitytoolkit.googleapis.com/v1/"
object FundoClient {
    lateinit var myApi: LoginApi
    private var instance: FundoClient? = null
    private lateinit var client: OkHttpClient

    init {
        val httpClient = OkHttpClient.Builder()
        client = httpClient.connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40,TimeUnit.SECONDS).writeTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    fun getInstance(): FundoClient {
        if (instance == null) {
            instance = FundoClient
        }
        return instance as FundoClient
    }

    @JvmName("getMyApi1")
    fun getMyApi(): LoginApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(client).build()
        myApi = retrofit.create(LoginApi::class.java)
        return myApi
    }
}