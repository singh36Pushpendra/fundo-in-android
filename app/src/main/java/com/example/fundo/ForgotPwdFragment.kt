package com.example.fundo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundo.util.FunDoUtil
import com.google.firebase.auth.FirebaseAuth

class ForgotPwdFragment : Fragment(R.layout.fragment_forgot_pwd) {

    private lateinit var btnSendMail: Button
    private lateinit var etUserEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_pwd, container, false)

        view.findViewById<TextView>(R.id.backToLogin).setOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, LoginFragment())
        }

        auth = FirebaseAuth.getInstance()

        btnSendMail = view.findViewById(R.id.btnSendMail)
        etUserEmail = view.findViewById(R.id.etUserEmail)

        btnSendMail.setOnClickListener {
            validateEmail()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun validateEmail() {
        email = etUserEmail.text.toString()
        if (!email.matches(Regex(FunDoUtil.EMAIL_PATTERN)))
            etUserEmail.setError("Please provide proper email!")
        else
            forgotPwd()
    }

    private fun forgotPwd() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Check your mail!", Toast.LENGTH_LONG).show()
                FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, LoginFragment())
                activity?.finish()
            }
            else {
                Toast.makeText(context, "${it.exception}", Toast.LENGTH_LONG).show()
            }
        }
    }
}