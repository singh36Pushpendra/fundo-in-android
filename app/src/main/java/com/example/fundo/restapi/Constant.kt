package com.example.fundo.restapi

object Constant {
    private lateinit var uid: String
    private var constant: Constant? = null

    fun getInstance(): Constant {
        constant = Constant
        return constant as Constant
    }

    fun setUserId(uid: String) {
        this.uid = uid
    }
}