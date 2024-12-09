package com.example.androidhomework.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidhomework.NoteAdapter
import com.example.androidhomework.data.model.Note
import com.example.androidhomework.databinding.FragmentNotesBinding
import com.example.androidhomework.viewmodel.NotesViewModel

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NotesViewModel
    private var adapter: NoteAdapter? = null
    private var notes: MutableList<Note> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        adapter = NoteAdapter(notes) { position ->
            deleteNote(position)
        }

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = adapter

        viewModel.allNotes.observe(viewLifecycleOwner) { newNotes ->
            notes.clear()
            notes.addAll(newNotes)
            adapter?.notifyDataSetChanged()
        }


    }

    private fun deleteNote(position: Int) {
        val note = notes[position]
        viewModel.deleteNote(note)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}