package com.example.fundo.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.fundo.R
import com.example.fundo.RegistrationFragment

class FunDoUtil {
    companion object {
        const val FIRST_NAME_PATTERN = "^[A-Z][a-z]{2,}$"
        const val LAST_NAME_PATTERN = FIRST_NAME_PATTERN
        const val EMAIL_PATTERN =
            "^[a-zA-Z]+([.+-]?[a-zA-Z0-9]{1,})*[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.][a-zA-Z]{2,3})?[,]?$"
        const val PASSWORD_PATTERN =
            "^(?=.{8,})(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*[@#$%^&()+-_][a-zA-Z0-9]*$"

        fun replaceFragment(activity: FragmentActivity?, containerId: Int, fragment: Fragment) {
            activity?.run {
                supportFragmentManager.beginTransaction()
                    .replace(containerId, fragment).addToBackStack(null).commit()
            }
        }
    }
}