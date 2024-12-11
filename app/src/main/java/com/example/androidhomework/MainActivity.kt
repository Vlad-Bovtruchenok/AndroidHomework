package com.example.androidhomework

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.notesListFragment -> {
                    if (navController.currentDestination?.id != R.id.notesFragment) {
                        navController.navigate(R.id.action_global_notesFragment)
                    }
                    true
                }

                R.id.addNoteFragment -> {
                    if (navController.currentDestination?.id != R.id.addNotesFragment) {
                        navController.navigate(R.id.action_global_addNotesFragment)
                    }
                    true
                }

                R.id.addImageNoteFragment -> {
                    if (navController.currentDestination?.id != R.id.addImageNoteFragment) {
                        navController.navigate(R.id.action_global_addImageNoteFragment)
                    }
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.notesFragment, R.id.addNotesFragment, R.id.addImageNoteFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }

                else -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }
}