package com.example.fundo.util

class FunDoUtil {
    companion object {

        const val FIRST_NAME_PATTERN = "^[A-Z][a-z]{2,}$"
        const val LAST_NAME_PATTERN = FIRST_NAME_PATTERN
        const val EMAIL_PATTERN =
            "^[a-zA-Z]+([.+-]?[a-zA-Z0-9]{1,})*[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.][a-zA-Z]{2,3})?[,]?$"
        const val PASSWORD_PATTERN =
            "^(?=.{8,})(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*[@#$%^&()+-_][a-zA-Z0-9]*$"
    }
}