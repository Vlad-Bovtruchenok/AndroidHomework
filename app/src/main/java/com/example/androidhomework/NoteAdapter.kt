package com.example.androidhomework

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager


class NoteAdapter(
    val notes: MutableList<Note>,
    val onNoteDelete: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegatesManager = AdapterDelegatesManager<List<Note>>()

    init {
        delegatesManager.addDelegate(TextNoteAdapterDelegate(onNoteDelete))
        delegatesManager.addDelegate(ImageNoteAdapterDelegate(onNoteDelete))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(notes, position, holder)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(notes, position)
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

