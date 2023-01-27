package com.example.fundo.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class NoteAuthService {

    // Firebase instance.
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore instance.
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun storeNote(note: Note, listener: (AuthListener) -> Unit) {
        val userId = mAuth.currentUser?.uid.toString()
        val collectionReference = db!!.collection("users_info").document(userId).collection("notes_info")
        val documentReference: DocumentReference
        if (note.noteId == "") {
            documentReference = collectionReference.document()
            note.noteId = documentReference.id
        }
        else {
            documentReference = collectionReference.document(note.noteId)
        }

        documentReference.set(note).addOnCompleteListener {
            if (it.isSuccessful) {
                listener(AuthListener(true, "Note saved!"))
            } else {
                listener(AuthListener(false, "Note not saved!"))
            }
        }
    }

    fun getNotes(listener: (NoteAuthListener) -> Unit) {
        db.collection("users_info").document(mAuth.currentUser!!.uid).collection("notes_info")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(NoteAuthListener(it.result.documents, true, "Notes fetched!"))
                    for (document in it.result) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    listener(NoteAuthListener(it.result.documents, false, "Notes not fetched!"))
                    Log.w(TAG, "Error getting documents.", it.exception)
                }
            }
    }

    fun deleteNote(noteId: String, listener: (AuthListener) -> Unit) {
        db.collection("users_info").document(mAuth.currentUser!!.uid).collection("notes_info")
            .document(noteId).delete().addOnSuccessListener {
                listener(AuthListener(true, "Note deleted!"))
            }
            .addOnFailureListener {
                listener(AuthListener(false, "Note not deleted!"))
            }
    }
}