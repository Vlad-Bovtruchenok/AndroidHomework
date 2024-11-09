package com.example.androidhomework

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class AddNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        val titleEditText = findViewById<EditText>(R.id.noteTitleText)
        val messageEditText = findViewById<EditText>(R.id.noteMessageText)
        val addButton = findViewById<Button>(R.id.addNoteButton)

        addButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val message = messageEditText.text.toString()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            saveNote(title, message, date)
            finish()
        }
    }

    private fun saveNote(title: String, message: String, date: String) {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val noteId = generateUniqueId()
        editor.putString("note_title_$noteId", title)
        editor.putString("note_message_$noteId", message)
        editor.putString("note_date_$noteId", date)
        editor.apply()
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}