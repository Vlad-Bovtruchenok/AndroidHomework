package com.example.androidhomework.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidhomework.data.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class NoteRepository(application: Application) {

    private val sharedPreferences =
        application.getSharedPreferences("notes", android.content.Context.MODE_PRIVATE)
    private val _allNotes = MutableLiveData<List<Note>>()
    val allNotes: LiveData<List<Note>> = _allNotes

    init {
        _allNotes.value = loadNotes()
    }

    private fun loadNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val allEntries = sharedPreferences.all

        for ((key, value) in allEntries) {
            if (key.startsWith("note_title_")) {
                val noteId = key.substring("note_title_".length)
                val title = sharedPreferences.getString(key, "")!!
                val message = sharedPreferences.getString("note_message_$noteId", "")!!
                val date = sharedPreferences.getString("note_date_$noteId", "")!!
                notes.add(Note.TextNote(title, message, date))
            } else if (key.startsWith("image_note_path_")) {
                val noteId = key.substring("image_note_path_".length)
                val imagePath = sharedPreferences.getString(key, "")!!
                val date = sharedPreferences.getString("image_note_date_$noteId", "")!!
                notes.add(Note.ImageNote(imagePath, date))
            }
        }

        return notes
    }


    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            when (note) {
                is Note.TextNote -> {
                    val noteId =
                        sharedPreferences.all.entries.find { it.value == note.title }?.key?.substring(
                            "note_title_".length
                        )
                    if (noteId != null) {
                        editor.remove("note_title_$noteId")
                        editor.remove("note_message_$noteId")
                        editor.remove("note_date_$noteId")
                    }
                }

                is Note.ImageNote -> {
                    val noteId =
                        sharedPreferences.all.entries.find { it.value == note.imagePath }?.key?.substring(
                            "image_note_path_".length
                        )
                    if (noteId != null) {
                        editor.remove("image_note_path_$noteId")
                        editor.remove("image_note_date_$noteId")
                    }
                }
            }
            editor.apply()
            _allNotes.postValue(loadNotes())
        }
    }

    suspend fun saveTextNote(title: String, message: String) {
        withContext(Dispatchers.IO) {
            val noteId = generateUniqueId()
            val editor = sharedPreferences.edit()
            editor.putString("note_title_$noteId", title)
            editor.putString("note_message_$noteId", message)
            editor.putString(
                "note_date_$noteId",
                SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()).format(Date())
            )
            editor.apply()
            _allNotes.postValue(loadNotes())
        }
    }

    suspend fun saveImageNote(imagePath: String) {
        withContext(Dispatchers.IO) {
            val noteId = generateUniqueId()
            val editor = sharedPreferences.edit()
            editor.putString("image_note_path_$noteId", imagePath)
            editor.putString(
                "image_note_date_$noteId",
                SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()).format(Date())
            )
            editor.apply()
            _allNotes.postValue(loadNotes())
        }
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}