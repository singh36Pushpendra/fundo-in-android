package com.example.fundo.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.db.DBHelper
import com.example.fundo.model.Note
import com.example.fundo.model.NoteAdapter
import com.example.fundo.model.NoteAuthService
import com.example.fundo.util.FunDoUtil
import com.example.fundo.viewmodel.HomeViewModel
import com.example.fundo.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private lateinit var fabCreateNote: FloatingActionButton

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var noteRecyclerView: RecyclerView

    private var isLinear: Boolean = true

    private lateinit var imgViewMore: ImageView
    private val unArchiveNotes = mutableListOf<Note>()
    private val archiveNotes = mutableListOf<Note>()
    private lateinit var noteAdapter: NoteAdapter

    private lateinit var progressBar: ProgressBar
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(
                NoteAuthService(
                    DBHelper(
                        requireContext()
                    )
                )
            )
        )[HomeViewModel::class.java]

        with(view) {
            topAppBar = findViewById(R.id.topAppBar)
            drawerLayout = findViewById(R.id.drawerLayout)
            navView = findViewById(R.id.navView)

            noteRecyclerView = findViewById(R.id.noteRecyclerView)
            progressBar = findViewById(R.id.progressBar)
        }


        (activity as AppCompatActivity).setSupportActionBar(topAppBar)
        setHasOptionsMenu(true)

        homeViewModel.getNotes()

        homeViewModel.notesStatus.observe(viewLifecycleOwner) { noteAuthListener ->
            if (isLinear)
                noteRecyclerView.layoutManager = GridLayoutManager(context, 2)
            else
                noteRecyclerView.layoutManager = LinearLayoutManager(context)
            noteAuthListener.notesList.forEach {
                if (it.isArchive == false)
                    unArchiveNotes.add(it)
                else
                    archiveNotes.add(it)
            }
            noteAdapter = NoteAdapter(requireContext(), unArchiveNotes)
            noteRecyclerView.adapter = noteAdapter
        }


        noteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Toast.makeText(requireContext(), "Inside onScrollStateChanged", Toast.LENGTH_SHORT)
                    .show()
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Toast.makeText(requireContext(), "Inside onScrolled", Toast.LENGTH_SHORT).show()
                super.onScrolled(recyclerView, dx, dy)
                var linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                    linearLayoutManager!!.findFirstVisibleItemPosition()
                val visibleItemCount = linearLayoutManager!!.childCount
                val totalItemCount = linearLayoutManager!!.itemCount

                Log.d("firstVisibleItemPosition", "$firstVisibleItemPosition")
                Log.d("visibleItemCount", "$visibleItemCount")
                Log.d("totalItemCount", "$totalItemCount")
                if (isScrolling && firstVisibleItemPosition + visibleItemCount == totalItemCount) {
                    isScrolling = false
                    Toast.makeText(requireContext(), "after fetchMoreNotes", Toast.LENGTH_SHORT).show()

                    fetchMoreNotes()
                }
            }
        })


        if (arguments != null) {
            if (arguments?.get("deleteNoteId") != null) {
                homeViewModel.deleteNote(arguments?.get("deleteNoteId").toString())
                homeViewModel.noteDeletionStatus.observe(viewLifecycleOwner) {
                    if (it.status) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (arguments?.get("archiveNoteId") != null) {
                homeViewModel.archiveNote(arguments?.get("archiveNoteId").toString())
                homeViewModel.noteArchivedStatus.observe(viewLifecycleOwner) {
                    if (it.status) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (arguments?.get("unarchiveNoteId") != null) {
                homeViewModel.unarchiveNote(arguments?.get("unarchiveNoteId").toString())
                homeViewModel.noteUnarchivedStatus.observe(viewLifecycleOwner) {
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
        navView.setNavigationItemSelectedListener {
            // Handle menu item selected
            when (it.itemId) {
                R.id.notes -> {
                    drawerLayout.close()

                    val noteAdapter = NoteAdapter(requireContext(), unArchiveNotes)
                    noteRecyclerView.adapter = noteAdapter
                }
                R.id.archive -> {
                    drawerLayout.close()
                    val noteAdapter = NoteAdapter(requireContext(), archiveNotes, "Unarchive")
                    noteRecyclerView.adapter = noteAdapter
                }
                R.id.signOut -> {
                    Log.d("Sign Out", "Working Fine!")
                    Toast.makeText(context, "It's working!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
            true
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    true
                }

                R.id.recyclerView -> {
                    val recyclerView: MenuItem = menuItem
                    if (isLinear) {
                        recyclerView.setIcon(R.drawable.ic_grid_view)
                        noteRecyclerView.layoutManager = LinearLayoutManager(context)
                        isLinear = false
                    } else {
                        recyclerView.setIcon(R.drawable.ic_linear_view)
                        noteRecyclerView.layoutManager = GridLayoutManager(context, 2)
                        isLinear = true
                    }
                    true
                }
                R.id.account -> {
                    ProfileDialogFragment().show(
                        activity?.supportFragmentManager!!,
                        "Profile Dialog"
                    )
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

        return view
    }

    private fun fetchMoreNotes() {


        Toast.makeText(requireContext(), "Inside fetchMoreNotes", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                Toast.makeText(requireContext(), "Inside run()", Toast.LENGTH_SHORT).show()
                homeViewModel.getNotes()

                noteAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
        }, 5000)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.top_app_bar, menu)
        val search = menu.findItem(R.id.search)
        val searchView: SearchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

}