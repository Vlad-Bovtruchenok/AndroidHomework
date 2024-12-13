package com.example.androidhomework.mvi


import com.example.androidhomework.data.model.Note
import com.example.androidhomework.data.model.NoteIntent
import com.example.androidhomework.data.model.NoteState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun noteReducer(state: NoteState, intent: NoteIntent): NoteState {
    return when (intent) {
        is NoteIntent.LoadNotes -> {
            state.copy(isLoading = true, error = null)
        }

        is NoteIntent.AddTextNote -> {
            val newNotes = state.notes + Note.TextNote(
                intent.title,
                intent.message,
                getCurrentDate()
            )
            state.copy(notes = newNotes, isLoading = false, error = null)
        }

        is NoteIntent.AddImageNote -> {
            val newNotes = state.notes + Note.ImageNote(intent.imagePath, getCurrentDate())
            state.copy(notes = newNotes, isLoading = false, error = null)
        }

        is NoteIntent.DeleteNote -> {
            val newNotes = state.notes.filter { it != intent.note }
            state.copy(notes = newNotes, isLoading = false, error = null)
        }
    }
}

fun getCurrentDate(): String {
    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
}