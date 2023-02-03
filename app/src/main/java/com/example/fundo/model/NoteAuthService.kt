package com.example.fundo.model

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.fundo.db.DBHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class NoteAuthService(val db: DBHelper) {

    // Firebase instance.
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore instance.
    private var fStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun storeNote(note: Note, listener: (AuthListener) -> Unit) {
        val userId = mAuth.currentUser?.uid.toString()
        val collectionReference =
            fStore!!.collection("users_info").document(userId).collection("notes_info")
        val documentReference: DocumentReference
        var noteAlreadyExists: Boolean

        if (note.noteId == null) {
            noteAlreadyExists = false
            documentReference = collectionReference.document()
            note.noteId = documentReference.id
            Log.d("Created", "Note saved in sqlite!")
        } else {
            noteAlreadyExists = true
            documentReference = collectionReference.document(note.noteId!!)
        }

        if (checkForInternet(db.context))
            documentReference.set(note).addOnCompleteListener {
                if (it.isSuccessful) {
                    db.saveNote(userId, note, noteAlreadyExists)
                    listener(AuthListener(true, "Note saved!"))
                } else {
                    listener(AuthListener(false, "Note not saved!"))
                }
            }
        else
            db.saveNote(userId, note, noteAlreadyExists)

    }

    fun getNotes(listener: (NoteAuthListener) -> Unit) {
        fStore.collection("users_info").document(mAuth.currentUser!!.uid).collection("notes_info")
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
        val userId = mAuth.currentUser!!.uid
        fStore.collection("users_info").document(userId).collection("notes_info")
            .document(noteId).delete().addOnSuccessListener {
                if (db.deleteNote(userId, noteId))
                    Log.d("Deletion", "Note deleted successfully!")
                else
                    Log.d("Deletion", "Note not deleted!")
                listener(AuthListener(true, "Note deleted!"))
            }
            .addOnFailureListener {
                listener(AuthListener(false, "Note not deleted!"))
            }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun archiveNote(noteId: String, listener: (AuthListener) -> Unit) {
        val userId = mAuth.currentUser!!.uid
        fStore.collection("users_info").document(userId).collection("notes_info")
            .document(noteId).update("archive", true).addOnSuccessListener {
                if (db.archiveNote(userId, noteId))
                    Log.d("Archive", "Note archived successfully!")
                else
                    Log.d("Archive", "Note not archived!")
                listener(AuthListener(true, "Note archived!"))
            }
            .addOnFailureListener {
                listener(AuthListener(false, "Note not archived!"))
            }
    }
}