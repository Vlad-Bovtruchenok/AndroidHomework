package com.example.androidhomework

sealed class Note {
    data class TextNote(
        val title: String,
        val message: String,
        val date: String
    ) : Note()

    data class ImageNote(
        val imagePath: String,
        val date: String
    ) : Note()
}