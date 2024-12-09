package com.example.androidhomework.data.model

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