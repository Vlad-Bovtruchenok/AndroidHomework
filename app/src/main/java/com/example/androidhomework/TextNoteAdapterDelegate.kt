package com.example.androidhomework

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhomework.data.model.Note
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class TextNoteAdapterDelegate(private val onNoteDelete: (Int) -> Unit) :
    AdapterDelegate<List<Note>>() {

    override fun isForViewType(items: List<Note>, position: Int): Boolean {
        return items[position] is Note.TextNote
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_note_item, parent, false)
        return TextNoteViewHolder(view, onNoteDelete)
    }

    override fun onBindViewHolder(
        items: List<Note>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val note = items[position] as Note.TextNote
        val viewHolder = holder as TextNoteViewHolder
        viewHolder.bind(note, items)
    }

    inner class TextNoteViewHolder(itemView: View, onNoteDelete: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitleTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.noteMessageTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.noteDateTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val shareButton: ImageButton = itemView.findViewById(R.id.shareButton)

        init {
            deleteButton.setOnClickListener {
                onNoteDelete(adapterPosition)
            }

            itemView.setOnClickListener {
                if (messageTextView.maxLines == 3) {
                    messageTextView.maxLines = Integer.MAX_VALUE
                } else {
                    messageTextView.maxLines = 3
                }
            }
        }

        fun bind(note: Note.TextNote, items: List<Note>) {
            titleTextView.text = note.title
            messageTextView.text = note.message
            dateTextView.text = note.date


            shareButton.setOnClickListener {
                val noteToShare = items[adapterPosition] as Note.TextNote
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${noteToShare.title}\n${noteToShare.message}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                itemView.context.startActivity(shareIntent)
            }
        }
    }
}