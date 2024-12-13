package com.example.androidhomework.di

import com.example.androidhomework.data.repository.NoteRepository
import com.example.androidhomework.viewmodel.AddImageNoteViewModel
import com.example.androidhomework.viewmodel.AddNotesViewModel
import com.example.androidhomework.viewmodel.NotesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { androidApplication().getSharedPreferences("notes", android.content.Context.MODE_PRIVATE) }
    single { NoteRepository(get()) }
    factory { NotesViewModel(androidApplication(), get()) }
    factory { AddNotesViewModel(androidApplication(), get()) }
    factory { AddImageNoteViewModel(androidApplication(), get()) }
}