package com.example.androidhomework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddImageNoteFragment : Fragment() {

    private lateinit var chooseImageButton: ImageButton
    private lateinit var imagePreview: ImageView
    private lateinit var saveButton: Button
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    selectedImageUri = data.data
                    imagePreview.visibility = View.VISIBLE
                    Glide.with(this).load(selectedImageUri).into(imagePreview)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_image_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chooseImageButton = view.findViewById(R.id.chooseImageButton)
        imagePreview = view.findViewById(R.id.imagePreview)
        saveButton = view.findViewById(R.id.addNoteButton)

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            if (selectedImageUri != null) {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                saveImageNote(selectedImageUri.toString(), date)
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Выберите изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageNote(imagePath: String, date: String) {
        val sharedPreferences = requireContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val noteId = generateUniqueId()
        editor.putString("image_note_path_$noteId", imagePath)
        editor.putString("image_note_date_$noteId", date)
        editor.apply()

        Toast.makeText(requireContext(), "Заметка с изображением сохранена", Toast.LENGTH_SHORT)
            .show()
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }
}