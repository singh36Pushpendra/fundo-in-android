package com.example.fundo.model

import com.google.firebase.firestore.DocumentSnapshot

data class NoteAuthListener(var notesList: List<Note>, var status: Boolean, var message: String) {
}