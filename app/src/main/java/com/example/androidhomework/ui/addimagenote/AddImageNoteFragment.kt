package com.example.androidhomework.ui.addimagenote

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.androidhomework.R
import com.example.androidhomework.databinding.FragmentAddImageNoteBinding
import com.example.androidhomework.viewmodel.AddImageNoteViewModel

class AddImageNoteFragment : Fragment() {

    private var _binding: FragmentAddImageNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddImageNoteViewModel
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    selectedImageUri = data.data
                    binding.imagePreview.visibility = View.VISIBLE
                    Glide.with(this).load(selectedImageUri).into(binding.imagePreview)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddImageNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddImageNoteViewModel::class.java)

        binding.chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        binding.addNoteButton.setOnClickListener {
            if (selectedImageUri != null) {
                viewModel.saveImageNote(selectedImageUri.toString())
                findNavController().navigate(R.id.action_global_notesFragment)
            } else {
                Toast.makeText(requireContext(), "Выберите изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}