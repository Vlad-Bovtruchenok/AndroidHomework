package com.example.androidhomework.data.model

data class NoteState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
