package com.example.fundo.view

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.R
import com.example.fundo.model.User
import com.example.fundo.model.UserAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.RegisterViewModel
import com.example.fundo.viewmodel.factory.RegisterViewModelFactory

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var alreadyHaveAccount: TextView
    
    private lateinit var progressDialog: ProgressDialog

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(RegisterViewModel::class.java)
        with(view) {
            etFirstName = findViewById(R.id.etFirstName)
            etLastName = findViewById(R.id.etLastName)
            etEmail = findViewById(R.id.etEmail)
            etPassword = findViewById(R.id.etPassword)
            etConfirmPassword = findViewById(R.id.etConfirmPassword)
            btnRegister = findViewById(R.id.btnRegister)
            alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount)
        }

        progressDialog = ProgressDialog(view.context)

        btnRegister.setOnClickListener {

            registerUser()
        }

        alreadyHaveAccount.setOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, LoginFragment())
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun registerUser() {
        val firstName: String = etFirstName.text.toString()
        val lastName: String = etLastName.text.toString()
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        val confirmPassword: String = etConfirmPassword.text.toString()

        with(FunDoUtil) {
            if (!email.matches(Regex(EMAIL_PATTERN))) {
                etEmail.error = "Enter Correct Email!"
            } else if (password.isEmpty() || password.length < 8) {
                etPassword.error = "Enter Proper Password!"
            } else if (password != confirmPassword) {
                etConfirmPassword.error = "Password Not matched in both fields!"
            } else {
                progressDialog.setMessage("Please wait while Registration...")
                progressDialog.setTitle("Registration")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val user = User(firstName, lastName, email, password, "")
                registerViewModel.registerUser(user)
                registerViewModel.userRegisterStatus.observe(viewLifecycleOwner) {
                    progressDialog.dismiss()
                    if (it.status) {
                        replaceFragment(activity, R.id.usersFrameLayout, HomeFragment())
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}