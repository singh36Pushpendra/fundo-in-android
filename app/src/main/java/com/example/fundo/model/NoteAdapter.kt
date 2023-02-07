package com.example.fundo.model

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.util.FunDoUtil
import com.example.fundo.view.HomeFragment
import com.example.fundo.view.NoteFragment

class NoteAdapter(
    val context: Context,
    private val notesList: MutableList<Note>
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>(), Filterable {

    private val notesListAll: MutableList<Note>

    init {
        notesListAll = ArrayList<Note>(notesList)
    }

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
        val note = notesList[position]

        holder.tvCardTitle.text = note.title
        holder.tvCardNote.text = note.content
        val noteId = note.noteId

        val imgViewMore = holder.imgViewMore
        imgViewMore.setOnClickListener {
            val popupMenu = PopupMenu(context, imgViewMore)
            popupMenu.menuInflater.inflate(R.menu.note_popup_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.archiveNote -> {
                        val bundle = Bundle()

                        if (it.title.toString() == "Archive")
                            bundle.putString("archiveNoteId", noteId)
                        else
                            bundle.putString("unarchiveNoteId", noteId)
                        val homeFragment = HomeFragment()
                        homeFragment.arguments = bundle
                        FunDoUtil.replaceFragment((context as AppCompatActivity), R.id.usersFrameLayout, homeFragment)
                    }
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
                        bundle.putString("deleteNoteId", noteId)
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

    override fun getFilter(): Filter {
        return filteration
    }

    val filteration: Filter = object : Filter() {
        // Runs on a background thread.
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filteredNotes = mutableListOf<Note>()
            if (charSequence.toString().isEmpty())
                filteredNotes.addAll(notesListAll)
            else {
                notesListAll.forEach {
                    val title = it.title
                    if (title.lowercase().contains(charSequence.toString().lowercase()))
                        filteredNotes.add(it)
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredNotes
            return filterResults
        }

        // Runs on a UI thread.
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            notesList.clear()
            notesList.addAll(filterResults!!.values as Collection<Note>)
            notifyDataSetChanged()
        }
    }
}