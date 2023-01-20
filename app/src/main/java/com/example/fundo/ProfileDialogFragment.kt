package com.example.fundo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.fundo.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileDialogFragment : DialogFragment() {

    private lateinit var fStore: FirebaseFirestore
    private var fAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnOk: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_dialog, container, false)

        fStore = FirebaseFirestore.getInstance()
        val userId = fAuth.currentUser?.uid.toString()

        with(view) {
            tvName = findViewById(R.id.tvName)
            tvEmail = findViewById(R.id.tvEmail)
            btnOk = findViewById(R.id.btnOk)
        }

        btnOk.setOnClickListener {
            dismiss()
        }

        fStore.collection("users_info").document(userId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var firstName = it.result.getString("firstName").toString()
                    Log.d("first name", "$firstName")
                    val user = with(it.result) {
                        User(
                            getString("firstName").toString(),
                            getString("lastName").toString(),
                            getString("email").toString(),
                            getString("password").toString()
                        )
                    }

                    tvName.text = "Name: ${user.firstName} ${user.lastName}"
                    tvEmail.text = "Email: ${user.email}"

                } else {
                    Log.w(TAG, "Error getting documents.", it.exception)
                }
            }
        return view
    }
}