package com.example.androidhomework.data.model

sealed class NoteIntent {
    data object LoadNotes : NoteIntent()
    data class AddTextNote(val title: String, val message: String) : NoteIntent()
    data class AddImageNote(val imagePath: String) : NoteIntent()
    data class DeleteNote(val note: Note) : NoteIntent()
}
