package com.example.androidhomework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidhomework.data.model.Note
import com.example.androidhomework.data.model.NoteIntent
import com.example.androidhomework.data.model.NoteState
import com.example.androidhomework.mvi.noteReducer
import com.example.androidhomework.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class NotesViewModel(
    application: Application,
    private val repository: NoteRepository
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(NoteState())
    val state: StateFlow<NoteState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadNotes()
        }
    }

    fun processIntent(intent: NoteIntent) {
        viewModelScope.launch {
            _state.value = noteReducer(_state.value, intent)
            when (intent) {
                is NoteIntent.LoadNotes -> {
                    loadNotes()
                    sortNotesByDate()
                }

                is NoteIntent.AddTextNote -> {
                    repository.saveTextNote(intent.title, intent.message)
                }

                is NoteIntent.AddImageNote -> {
                    repository.saveImageNote(intent.imagePath)
                }

                is NoteIntent.DeleteNote -> {
                    repository.deleteNote(intent.note)
                }
            }
        }
    }

    fun sortNotesByDate() {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()) // Формат для разбора дат заметок
        _state.value = _state.value.copy(notes = _state.value.notes.sortedByDescending { note ->
            when (note) {
                is Note.TextNote -> dateFormat.parse(note.date)!!
                is Note.ImageNote -> dateFormat.parse(note.date)!!
            }
        })
    }

    private fun loadNotes() {
        try {
            val notes = repository.loadNotes()
            _state.value = _state.value.copy(notes = notes, isLoading = false)
            sortNotesByDate()
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message, isLoading = false)
        }
    }
}