package com.example.androidhomework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.UUID
import java.util.Locale

class AddImageNoteActivity : AppCompatActivity() {

    private lateinit var chooseImageButton: ImageButton
    private lateinit var imagePreview: ImageView
    private lateinit var saveButton: Button
    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_notes)

        chooseImageButton = findViewById(R.id.chooseImageButton)
        imagePreview = findViewById(R.id.imagePreview)
        saveButton = findViewById(R.id.addNoteButton)

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            if (selectedImageUri != null) {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date())
                saveImageNote(selectedImageUri.toString(), date)
                finish()
            } else {
                Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imagePreview.visibility = View.VISIBLE
            Glide.with(this).load(selectedImageUri).into(imagePreview)
        }
    }

    private fun saveImageNote(imagePath: String, date: String) {
        val sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val noteId = generateUniqueId()
        editor.putString("image_note_path_$noteId", imagePath)
        editor.putString("image_note_date_$noteId", date)
        editor.apply()

        Toast.makeText(this, "Заметка с изображением сохранена", Toast.LENGTH_SHORT).show()
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}