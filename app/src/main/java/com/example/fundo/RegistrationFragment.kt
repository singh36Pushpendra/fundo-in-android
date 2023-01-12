package com.example.fundo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundo.util.FunDoUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var progressDialog: ProgressDialog

    // Firebase instances.
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        with(view) {
            etFirstName = findViewById(R.id.etFirstName)
            etLastName = findViewById(R.id.etLastName)
            etEmail = findViewById(R.id.etEmail)
            etPassword = findViewById(R.id.etPassword)
            etConfirmPassword = findViewById(R.id.etConfirmPassword)
            btnRegister = findViewById(R.id.btnRegister)
        }

        progressDialog = ProgressDialog(view.context)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!

        btnRegister.setOnClickListener {

            validateContacts()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun validateContacts() {
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

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        sendUserToNextPage()
                        Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "${it.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun sendUserToNextPage() {
        fragmentManager?.run {
            beginTransaction().replace(R.id.usersFrameLayout, HomeFragment()).commit()
        }
    }

}