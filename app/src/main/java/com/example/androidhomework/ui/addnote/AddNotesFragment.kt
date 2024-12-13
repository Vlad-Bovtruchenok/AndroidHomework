package com.example.androidhomework.ui.addnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidhomework.R
import com.example.androidhomework.databinding.FragmentAddNotesBinding
import com.example.androidhomework.viewmodel.AddNotesViewModel
import org.koin.android.ext.android.inject

class AddNotesFragment : Fragment() {

    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddNotesViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNoteButton.setOnClickListener {
            val title = binding.noteTitleText.text.toString()
            val message = binding.noteMessageText.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty()) {
                viewModel.saveNote(title, message)
                findNavController().navigate(R.id.action_global_notesFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}