package com.example.fundo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fundo.util.FunDoUtil
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteFragment : Fragment() {

    private lateinit var noteTopAppBar: MaterialToolbar
    private lateinit var fabSaveNote: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        with(view) {

            noteTopAppBar = findViewById(R.id.noteTopAppBar)
            fabSaveNote = findViewById(R.id.fabSaveNote)
        }

        noteTopAppBar.setNavigationOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, HomeFragment())
        }

        fabSaveNote.setOnClickListener {

        }
        return view
    }
}