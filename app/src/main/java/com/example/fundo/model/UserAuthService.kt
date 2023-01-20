package com.example.fundo.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserAuthService {

    // Firebase instance.
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore instance.
    private var db: FirebaseFirestore? = FirebaseFirestore.getInstance()

    fun registerUser(user: User, listener: (AuthListener) -> Unit) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                storeUser(user)
                listener (AuthListener(true, "Registration successful!"))
            } else {
                listener (AuthListener(false, "Registration failed!"))
            }
        }
    }

    fun loginUser(user: User, listener: (AuthListener) -> Unit) {
        mAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful)
                listener (AuthListener(true, "Login successful!"))
            else
                listener (AuthListener(false, "Login failed!"))
        }
    }

    private fun storeUser(user: User) {
        val userId = mAuth.currentUser?.uid.toString()
        val userMap = HashMap<String, String>()
        userMap.put("firstName", user.firstName)
        userMap.put("lastName", user.lastName)
        userMap.put("email", user.email)
        userMap.put("password", user.password)

        // Add a new document with a generated ID
        db!!.collection("users_info").document(userId)
            .set(userMap)
            .addOnSuccessListener(OnSuccessListener {

            })
    }
}