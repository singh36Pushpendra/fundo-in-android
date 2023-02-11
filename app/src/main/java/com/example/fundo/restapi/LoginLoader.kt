package com.example.fundo.restapi

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginLoader {
    fun getLoginDone(loginListener: LoginListener, email: String, password: String) {
        FundoClient.getInstance().getMyApi()
            .loginWithRestApi(LoginRequest(email, password, true))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>?,
                    response: Response<LoginResponse>?
                ) {
                    if (response!!.isSuccessful) {
                        response.body().let {
                            loginListener.onLogin(it, true)
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                    loginListener.onLogin(null, false)
                }

            })
    }
}