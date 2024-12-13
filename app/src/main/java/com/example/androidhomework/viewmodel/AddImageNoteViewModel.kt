package com.example.androidhomework.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidhomework.data.repository.NoteRepository
import kotlinx.coroutines.launch

class AddImageNoteViewModel(
    application: Application,
    private val repository: NoteRepository
) : AndroidViewModel(application) {

    fun saveImageNote(imagePath: String) = viewModelScope.launch {
        repository.saveImageNote(imagePath)

    }
}