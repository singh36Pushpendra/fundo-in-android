package com.example.fundo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.R
import com.example.fundo.model.Note
import com.example.fundo.model.NoteAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.NoteViewModel
import com.example.fundo.viewmodel.factory.NoteViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteFragment : Fragment() {

    private lateinit var noteTopAppBar: MaterialToolbar
    private lateinit var fabSaveNote: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel

    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteAuthService()))[NoteViewModel::class.java]
        with(view) {

            noteTopAppBar = findViewById(R.id.noteTopAppBar)
            fabSaveNote = findViewById(R.id.fabSaveNote)

            etTitle = findViewById(R.id.etTitle)
            etNote = findViewById(R.id.etNote)
        }

        val noteTitle = arguments?.get("title")
        val noteContent = arguments?.get("content")

        if (noteTitle != null)
            etTitle.setText(noteTitle.toString())
        if (noteContent != null)
            etNote.setText(noteContent.toString())

        noteTopAppBar.setNavigationOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, HomeFragment())
        }

        fabSaveNote.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etNote.text.toString()

            val note: Note
            val anyNoteId = arguments?.get("noteId")
            if (anyNoteId == null) {
                note = Note("", title, content)
            }
            else {
                note = Note(anyNoteId.toString(), title, content)
            }

            noteViewModel.storeNote(note)
            noteViewModel.noteStatus.observe(viewLifecycleOwner) {
                if (it.status) {
                    FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, HomeFragment())
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}