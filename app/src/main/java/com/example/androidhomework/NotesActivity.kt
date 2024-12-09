package com.example.androidhomework

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NotesActivity : AppCompatActivity() {

    private lateinit var addButton: FloatingActionButton
    private lateinit var addImageButton: FloatingActionButton
    private val notes = mutableListOf<Note>()
    private var adapter: NoteAdapter? = null
    private val noteIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        displayLogin()
        setupAddNoteButton()
        loadNotes()
    }

    private fun displayLogin() {
        val login = intent.getStringExtra("LOGIN")
        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        loginTextView.text = login
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(notes) { position ->
            deleteNote(position)
        }
        recyclerView.adapter = adapter
    }

    private fun setupAddNoteButton() {
        addImageButton = findViewById<FloatingActionButton>(R.id.AddFloatingActionButtonImage)
        addButton = findViewById<FloatingActionButton>(R.id.AddFloatingActionButton)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddNotesActivity::class.java))
        }
        addImageButton.setOnClickListener {
            startActivity(Intent(this, AddImageNoteActivity::class.java))
        }
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val allNotes = mutableMapOf<String, Note>()
        noteIds.clear()

        for (key in sharedPreferences.all.keys) {
            if (key.startsWith("note_title_")) {
                val noteId = key.substring("note_title_".length)
                val title = sharedPreferences.getString(key, "")!!
                val message = sharedPreferences.getString("note_message_$noteId", "")!!
                val date = sharedPreferences.getString("note_date_$noteId", "")!!
                allNotes[noteId] = Note.TextNote(title, message, date)
                noteIds.add(noteId)
            } else if (key.startsWith("image_note_path_")) {
                val noteId = key.substring("image_note_path_".length)
                val imagePath = sharedPreferences.getString(key, "")!!
                val date = sharedPreferences.getString("image_note_date_$noteId", "")!!
                allNotes[noteId] = Note.ImageNote(imagePath, date)
                noteIds.add(noteId)
            }
        }

        notes.clear()
        notes.addAll(allNotes.values)
        adapter?.notifyDataSetChanged()
    }

    private fun removeTextNote(editor: SharedPreferences.Editor, noteId: String) {
        editor.remove("note_title_$noteId")
        editor.remove("note_message_$noteId")
        editor.remove("note_date_$noteId")
    }

    private fun removeImageNote(editor: SharedPreferences.Editor, noteId: String) {
        editor.remove("image_note_path_$noteId")
        editor.remove("image_note_date_$noteId")
    }

    private fun deleteNote(position: Int) {
        val noteId = noteIds[position]
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val note = notes[position]
        when (note) {
            is Note.TextNote -> removeTextNote(editor, noteId)
            is Note.ImageNote -> removeImageNote(editor, noteId)
        }

        editor.apply()

        notes.removeAt(position)
        noteIds.removeAt(position)
        adapter?.notifyItemRemoved(position)

        Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }
}