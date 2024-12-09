package com.example.androidhomework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteAdapter(
    val notes: MutableList<Note>,
    val onNoteDelete: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitleTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.noteMessageTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.noteDateTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.titleTextView.text = currentNote.title
        holder.messageTextView.text = notes[position].message
        holder.dateTextView.text = currentNote.date
        holder.deleteButton.setOnClickListener {
            onNoteDelete(position)
        }
        holder.itemView.setOnClickListener {
            if (holder.messageTextView.maxLines == 3) {
                holder.messageTextView.maxLines = Integer.MAX_VALUE
            } else {
                holder.messageTextView.maxLines = 3
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

