package com.example.fundo.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fundo.R
import com.example.fundo.model.UserAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.ProfileDialogViewModel
import com.example.fundo.viewmodel.factory.ProfileDialogViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ProfileDialogFragment : DialogFragment() {

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnSignOut: Button

    private lateinit var profileDialogViewModel: ProfileDialogViewModel
    private lateinit var imgViewUser: CircleImageView
    private lateinit var imgViewClose: ImageView

    private val SELECT_PICTURE = 200
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_dialog, container, false)

        with(view) {
            tvName = findViewById(R.id.tvName)
            tvEmail = findViewById(R.id.tvEmail)
            btnSignOut = findViewById(R.id.btnSignOut)

            imgViewUser = findViewById(R.id.imgViewUser)
            imgViewClose = findViewById(R.id.imgViewClose)
        }

        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            dismiss()
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, LoginFragment())
        }

        imgViewClose.setOnClickListener {
            dismiss()
        }
        profileDialogViewModel = ViewModelProvider(
            this,
            ProfileDialogViewModelFactory(UserAuthService())
        )[ProfileDialogViewModel::class.java]

        profileDialogViewModel.getUser()

        profileDialogViewModel.userProfileStatus.observe(viewLifecycleOwner) {
            if (it.status) {
                val user = it.user
                tvName.text = user.firstName
                tvEmail.text = user.email

                Log.d("Profile Pic", "${user.profilePic}")
                Glide.with(this).load(user.profilePic).into(imgViewUser)

                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        imgViewUser.setOnClickListener {
            imageChooser()
        }
        return view
    }

    private fun imageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Chooser"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImgUri = data?.data
                if (null != selectedImgUri) {
                    imgViewUser.setImageURI(selectedImgUri)
                    profileDialogViewModel.uploadProfilePic(selectedImgUri)
                    profileDialogViewModel.userProfilePicStatus.observe(viewLifecycleOwner) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}