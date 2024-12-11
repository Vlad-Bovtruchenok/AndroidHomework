package com.example.androidhomework.mvi

import com.example.androidhomework.data.model.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
