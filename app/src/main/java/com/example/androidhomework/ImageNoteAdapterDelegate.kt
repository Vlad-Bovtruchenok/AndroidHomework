package com.example.androidhomework

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.bumptech.glide.Glide
import com.example.androidhomework.data.model.Note

class ImageNoteAdapterDelegate(private val onNoteDelete: (Int) -> Unit) :
    AdapterDelegate<List<Note>>() {

    override fun isForViewType(items: List<Note>, position: Int): Boolean {
        return items[position] is Note.ImageNote
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_image_note_item, parent, false)
        return ImageNoteViewHolder(view)
    }

    override fun onBindViewHolder(
        items: List<Note>,
        position: Int,
        holder: RecyclerView.ViewHolder, payloads: MutableList<Any>
    ) {
        val note = items[position] as Note.ImageNote
        val viewHolder = holder as ImageNoteViewHolder
        viewHolder.bind(note)
    }

    inner class ImageNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteImageView: ImageView = itemView.findViewById(R.id.noteImageView)
        val noteDateTextView: TextView = itemView.findViewById(R.id.noteDateTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val shareButton: ImageButton = itemView.findViewById(R.id.shareButton)

        fun bind(note: Note.ImageNote) {
            Glide.with(itemView.context)
                .load(note.imagePath)
                .into(noteImageView)
            noteDateTextView.text = note.date

            deleteButton.setOnClickListener {
                onNoteDelete(adapterPosition)
            }

            shareButton.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, note.imagePath)
                    type = "image/jpeg"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                itemView.context.startActivity(shareIntent)
            }
        }
    }
}