package com.example.fundo.view

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.R
import com.example.fundo.model.User
import com.example.fundo.model.UserAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.LoginViewModel
import com.example.fundo.viewmodel.factory.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    // Firebase instances.
    private lateinit var mAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    private lateinit var createNewAccount: TextView

    private lateinit var imgViewGoogle: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var loginViewModel: LoginViewModel

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount = task.result
            account?.let {
                updateUI(account)
            }
        } else {
            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val homeFragment = HomeFragment()

                FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, homeFragment)
            }
            else
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())).get(LoginViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        createNewAccount = view.findViewById(R.id.createNewAccount)

        createNewAccount.setOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, RegistrationFragment())
        }

        with(view) {

            etUsername = findViewById(R.id.etUser)
            etPassword = findViewById(R.id.etPass)
            btnLogin = findViewById(R.id.btnLogin)
            imgViewGoogle = findViewById(R.id.imgViewGoogle)
        }

        progressDialog = ProgressDialog(view.context)

        btnLogin.setOnClickListener {

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            validateUserLogin(username, password)
        }

        imgViewGoogle.setOnClickListener {
            signInGoogle()
        }

        view.findViewById<TextView>(R.id.forgotPassword).setOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, ForgotPwdFragment())
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    fun validateUserLogin(username: String, password: String): Boolean {

        var flag = false

        with(FunDoUtil) {
            if (!username.matches(Regex(EMAIL_PATTERN))) {
                etUsername.error = "Enter Correct Email!"
            } else if (password.isEmpty() || password.length < 8) {
                etPassword.error = "Enter Proper Password!"
            } else {

                with(progressDialog) {
                    setMessage("Please wait while Login...")
                    setTitle("Login")
                    setCanceledOnTouchOutside(false)
                    show()
                }

                val user = User("", "", username, password, "")
                loginViewModel.loginUser(user)

                loginViewModel.userLoginStatus.observe(viewLifecycleOwner) {
                    progressDialog.dismiss()
                    if (it.status) {
                        val homeFragment = HomeFragment()
                        replaceFragment(activity, R.id.usersFrameLayout, homeFragment)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return flag
    }

}