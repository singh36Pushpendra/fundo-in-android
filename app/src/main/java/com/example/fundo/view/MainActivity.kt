package com.example.fundo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fundo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.usersFrameLayout, LoginFragment()).commit()
    }
}