package com.example.fundo.model

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.util.FunDoUtil
import com.example.fundo.view.HomeFragment
import com.example.fundo.view.NoteFragment
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(
    val context: Context,
    private val notesList: List<DocumentSnapshot>
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardTitle = itemView.findViewById<TextView>(R.id.tvCardTitle)
        val tvCardNote = itemView.findViewById<TextView>(R.id.tvCardNote)

        val imgViewMore = itemView.findViewById<ImageView>(R.id.imgViewMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentSnapshot = notesList[position]

        holder.tvCardTitle.text = documentSnapshot.get("title").toString()
        holder.tvCardNote.text = documentSnapshot.get("content").toString()
        val noteId = documentSnapshot.get("noteId").toString()

        val imgViewMore = holder.imgViewMore
        imgViewMore.setOnClickListener {
            val popupMenu = PopupMenu(context, imgViewMore)
            popupMenu.menuInflater.inflate(R.menu.note_popup_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.archiveNote -> {}
                    R.id.editNote -> {
                        val noteFragment = NoteFragment()
                        val bundle = Bundle()
                        bundle.putString("noteId", noteId)
                        bundle.putString("title", holder.tvCardTitle.text.toString())
                        bundle.putString("content", holder.tvCardNote.text.toString())
                        noteFragment.arguments = bundle
                        FunDoUtil.replaceFragment((context as AppCompatActivity), R.id.usersFrameLayout, noteFragment)
                    }
                    R.id.deleteNote -> {
                        val bundle = Bundle()
                        bundle.putString("noteId", noteId)
                        val homeFragment = HomeFragment()
                        homeFragment.arguments = bundle
                        FunDoUtil.replaceFragment((context as AppCompatActivity), R.id.usersFrameLayout, homeFragment)
                    }
                    else -> Log.d("More Action", "Menu Item")
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}