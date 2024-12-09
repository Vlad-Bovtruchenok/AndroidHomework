package com.example.androidhomework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidhomework.data.model.Note
import com.example.androidhomework.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository = NoteRepository(application)
    val allNotes: LiveData<List<Note>> = repository.allNotes
    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}