package com.example.fundo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fundo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.deleteDatabase("FUNDO_NOTES")
        this.deleteDatabase("FUNDO")
        supportFragmentManager.beginTransaction().add(R.id.usersFrameLayout, LoginFragment()).commit()
    }
}