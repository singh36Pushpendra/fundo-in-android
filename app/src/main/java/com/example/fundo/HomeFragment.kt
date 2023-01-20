package com.example.fundo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.fundo.util.FunDoUtil
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private lateinit var fabCreateNote: FloatingActionButton

    private lateinit var topAppBar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        with(view) {
            topAppBar = findViewById(R.id.topAppBar)
            drawerLayout = findViewById(R.id.drawerLayout)
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
        return view
    }
}