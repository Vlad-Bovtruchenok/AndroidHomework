package com.example.androidhomework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidhomework.data.repository.NoteRepository
import kotlinx.coroutines.launch

class AddNotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository = NoteRepository(application)

    fun saveNote(title: String, message: String) = viewModelScope.launch {
        repository.saveTextNote(title, message)
    }
}