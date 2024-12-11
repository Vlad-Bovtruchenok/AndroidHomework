package com.example.androidhomework.mvi

import com.example.androidhomework.data.model.Note

sealed class NoteIntent {
    data object LoadNotes : NoteIntent()
    data class AddTextNote(val title: String, val message: String) : NoteIntent()
    data class AddImageNote(val imagePath: String) : NoteIntent()
    data class DeleteNote(val note: Note) : NoteIntent()
}
