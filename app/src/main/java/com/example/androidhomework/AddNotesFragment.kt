package com.example.androidhomework

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import java.util.Locale

class AddNotesFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.noteTitleText)
        messageEditText = view.findViewById(R.id.noteMessageText)
        addButton = view.findViewById(R.id.addNoteButton)

        addButton.setOnClickListener {
            handleAddNoteClick()
        }
    }

    private fun handleAddNoteClick() {
        val title = titleEditText.text.toString()
        val message = messageEditText.text.toString()

        if (validateInput(title, message)) {
            val note = createNote(title, message)
            saveNote(note)
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(title: String, message: String): Boolean {
        return title.isNotBlank() && message.isNotBlank()
    }

    private fun createNote(title: String, message: String): Note.TextNote {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return Note.TextNote(title, message, date)
    }

    private fun saveNote(note: Note.TextNote) {
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val noteId = generateUniqueId()
        editor.putString("note_title_$noteId", note.title)
        editor.putString("note_message_$noteId", note.message)
        editor.putString("note_date_$noteId", note.date)
        editor.apply()
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}