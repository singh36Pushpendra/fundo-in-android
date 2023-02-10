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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NoteAuthService(val db: DBHelper) {

    private var flag: Boolean = true

    // Firebase instance.
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore instance.
    private var fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val limit = 13

    private lateinit var lastVisible: DocumentSnapshot
    var isLastItemReached = false
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
        val usersRef = fStore.collection("users_info")
        val notesRef =
            usersRef.document(FirebaseAuth.getInstance().currentUser!!.uid).collection("notes_info")

        var query: Query
        if (flag) {

            query =
                notesRef.orderBy("title", Query.Direction.ASCENDING).limit(limit.toLong())
            flag = false
        } else {

            query =
                notesRef.orderBy("title", Query.Direction.ASCENDING).startAfter(lastVisible)
                    .limit(limit.toLong())
        }

        if (!isLastItemReached) {
        query.get()
            .addOnCompleteListener { documents ->
                val noteSets = mutableListOf<Note>()
                if (documents.isSuccessful) {
                    for (document in documents.result) {
                        val noteId = document["noteId"].toString()
                        val title = document["title"].toString()
                        val content = document["content"].toString()
                        var isArchive = document["archive"] as Boolean

                        val note = Note(noteId, title, content, isArchive)
                        noteSets.add(note)
                    }
                    listener(NoteAuthListener(noteSets, true, "Notes fetched!"))

                    lastVisible =
                        documents.result.documents[documents.result.size() - 1]
                    if (documents.result.size() < limit) {
                        isLastItemReached = true
                    }
                } else {
                    listener(NoteAuthListener(emptyList(), false, "Notes not fetched!"))
                    Log.w(TAG, "Error getting documents.", documents.exception)
                }
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

    fun unarchiveNote(noteId: String, listener: (AuthListener) -> Unit) {
        val userId = mAuth.currentUser!!.uid
        fStore.collection("users_info").document(userId).collection("notes_info")
            .document(noteId).update("archive", false).addOnSuccessListener {
                if (db.unarchiveNote(userId, noteId))
                    Log.d("Unarchive", "Note unarchived successfully!")
                else
                    Log.d("Unarchive", "Note not unarchived!")
                listener(AuthListener(true, "Note unarchived!"))
            }
            .addOnFailureListener {
                listener(AuthListener(false, "Note not unarchived!"))
            }
    }
}