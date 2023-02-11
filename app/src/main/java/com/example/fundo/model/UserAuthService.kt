package com.example.fundo.model

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.example.fundo.restapi.Constant
import com.example.fundo.restapi.LoginListener
import com.example.fundo.restapi.LoginLoader
import com.example.fundo.restapi.LoginResponse
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class UserAuthService {

    // Firebase instance.
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore instance.
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Firebase storage.
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()

    private lateinit var user: User
    fun registerUser(user: User, listener: (AuthListener) -> Unit) {
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                storeUser(user)
                listener(AuthListener(true, "Registration successful!"))
            } else {
                listener(AuthListener(false, "Registration failed!"))
            }
        }
    }

    fun loginUser(user: User, listener: (AuthListener) -> Unit) {
        mAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful)
                listener(AuthListener(true, "Login successful!"))
            else
                listener(AuthListener(false, "Login failed!"))
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

    fun getUser(listener: (UserAuthListener) -> Unit) {
        val userId = mAuth.currentUser?.uid.toString()

        db!!.collection("users_info").document(userId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    user = with(it.result) {
                        User(
                            getString("firstName").toString(),
                            getString("lastName").toString(),
                            getString("email").toString(),
                            getString("password").toString(),
                            getString("profilePic").toString()
                        )
                    }

                    listener(UserAuthListener(user!!, true, "Profile exist!"))

                } else {
                    listener(UserAuthListener(user!!, false, "Profile not exist!"))
                    Log.w(ContentValues.TAG, "Error getting document.", it.exception)
                }
            }
    }

    fun uploadProfilePic(selectedImage: Uri, listener: (AuthListener) -> Unit) {
        val reference = storage.reference.child("profile").child(Date().time.toString())
        reference.putFile(selectedImage).addOnSuccessListener {
            listener(AuthListener(true, "Profile pic uploaded!"))
            reference.downloadUrl.addOnCompleteListener {


                saveImgUrl(it.toString())
            }
        }
            .addOnFailureListener {
                listener(AuthListener(false, "Profile pic not uploaded!"))
            }
    }

    private fun saveImgUrl(imgUrl: String) {
        db!!.collection("users_info").document(mAuth.currentUser!!.uid).update("profilePic", imgUrl)
    }

    fun getNotes() {

    }

    fun apiLogin(email: String, password: String, listener: (AuthListener) -> Unit) {
        val loader = LoginLoader()
        loader.getLoginDone(object : LoginListener {
            override fun onLogin(loginResponse: LoginResponse?, status: Boolean) {
                if (status) {
                    if (loginResponse != null) {
                        val constant = Constant.getInstance()
                        constant.setUserId(loginResponse.localId)
                        listener(AuthListener(true, "Login successful with rest api!"))
                    }
                }
                else {
                    listener(AuthListener(true, "Login failed with rest api!"))
                }
            }

        }, email, password)
    }
}