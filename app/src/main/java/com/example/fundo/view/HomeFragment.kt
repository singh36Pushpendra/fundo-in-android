package com.example.fundo.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.db.DBHelper
import com.example.fundo.model.NoteAdapter
import com.example.fundo.model.NoteAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.HomeViewModel
import com.example.fundo.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.function.Predicate

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private lateinit var fabCreateNote: FloatingActionButton

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var noteRecyclerView: RecyclerView

    private var flag: Boolean = true

    private lateinit var imgViewMore: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(NoteAuthService(DBHelper(
            requireContext()
        ))))[HomeViewModel::class.java]

        with(view) {
            topAppBar = findViewById(R.id.topAppBar)
            drawerLayout = findViewById(R.id.drawerLayout)
            navView = findViewById(R.id.navView)

            noteRecyclerView = findViewById(R.id.noteRecyclerView)
        }

        homeViewModel.getNotes()

        homeViewModel.notesStatus.observe(viewLifecycleOwner) { noteAuthListener ->
            noteRecyclerView.layoutManager = GridLayoutManager(context, 2)
            val unArchiveNotes = noteAuthListener.notesList.filter {
                it.get("archive") == false
            }
            val noteAdapter = NoteAdapter(requireContext(), unArchiveNotes)
            noteRecyclerView.adapter = noteAdapter
        }

        if (arguments != null) {
            val noteId = arguments?.get("noteId").toString()
            if (arguments?.get("deleteNote") != null) {
                homeViewModel.deleteNote(noteId)
                homeViewModel.noteDeletionStatus.observe(viewLifecycleOwner) {
                    if (it.status) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (arguments?.get("archiveNote") != null) {
                homeViewModel.archiveNote(noteId)
                homeViewModel.noteArchivedStatus.observe(viewLifecycleOwner) {
                    if (it.status) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    true
                }
                R.id.recyclerView -> {
                    val recyclerView: MenuItem = menuItem
                    if (flag) {
                        recyclerView.setIcon(R.drawable.ic_grid_view)
                        noteRecyclerView.layoutManager = LinearLayoutManager(context)
                        flag = false
                    }
                    else {
                        recyclerView.setIcon(R.drawable.ic_linear_view)
                        noteRecyclerView.layoutManager = GridLayoutManager(context, 2)
                        flag = true
                    }
                    true
                }
                R.id.account -> {
                    ProfileDialogFragment().show(activity?.supportFragmentManager!!, "Profile Dialog")
                    true
                }
                else -> false
            }
        }

        auth = FirebaseAuth.getInstance()

        fabCreateNote = view.findViewById(R.id.fabCreateNote)
        fabCreateNote.setOnClickListener {
            FunDoUtil.replaceFragment(activity, R.id.usersFrameLayout, NoteFragment())
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(activity, drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            // Handle menu item selected
            it.isChecked = true
            drawerLayout.close()
            if (it.itemId == R.id.signOut) {
                Log.d("Sign Out", "Working Fine!")
                Toast.makeText(context, "It's working!", Toast.LENGTH_SHORT).show()
            }
            true
        }
        return view
    }

    @SuppressLint("LongLogTag")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut) {
            Log.d("Sign Out OnOptionsItemSelected", "Working Fine!")
            Toast.makeText(context, "It's working!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}