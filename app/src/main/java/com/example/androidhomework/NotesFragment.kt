package com.example.androidhomework

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class NotesFragment : Fragment() {

    private val notes = mutableListOf<Note>()
    private var adapter: NoteAdapter? = null
    private val noteIds = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayLogin(view)
        setupRecyclerView(view)
        loadNotes()

    }

    private fun displayLogin(view: View) {
        val login = arguments?.getString("login")
        val loginTextView = view.findViewById<TextView>(R.id.loginTextView)
        loginTextView.text = login
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NoteAdapter(notes) { position ->
            deleteNote(position)
        }
        recyclerView.adapter = adapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadNotes() {
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
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
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
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

        Toast.makeText(requireContext(), "Заметка удалена", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }
}