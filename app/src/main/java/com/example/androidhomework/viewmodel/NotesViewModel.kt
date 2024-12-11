package com.example.androidhomework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidhomework.mvi.NoteIntent
import com.example.androidhomework.mvi.NoteState
import com.example.androidhomework.mvi.noteReducer
import com.example.androidhomework.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository = NoteRepository(application)
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

    private fun loadNotes() {
        try {
            val notes = repository.loadNotes()
            _state.value = _state.value.copy(notes = notes, isLoading = false)
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message, isLoading = false)
        }
    }
}