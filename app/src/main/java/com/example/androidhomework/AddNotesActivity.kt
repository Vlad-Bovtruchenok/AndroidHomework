package com.example.androidhomework

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddNotesActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        titleEditText = findViewById(R.id.noteTitleText)
        messageEditText = findViewById(R.id.noteMessageText)
        addButton = findViewById(R.id.addNoteButton)

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
            finish()
        } else {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
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
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
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