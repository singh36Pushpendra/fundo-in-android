package com.example.fundo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var createNewAccount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        createNewAccount = view.findViewById(R.id.createNewAccount)
        goToRegistrationPage()

        // Inflate the layout for this fragment
        return view
    }

    private fun goToRegistrationPage() {
        createNewAccount.setOnClickListener {

            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.usersFrameLayout, RegistrationFragment()).commit()
            }
        }
    }

}