package com.example.androidhomework.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidhomework.NoteAdapter
import com.example.androidhomework.mvi.NoteIntent
import com.example.androidhomework.databinding.FragmentNotesBinding
import com.example.androidhomework.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private var viewModel: NotesViewModel? = null
    private var adapter: NoteAdapter? = null

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

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel!!.state.collectLatest { state ->
                adapter = NoteAdapter(state.notes.toMutableList()) { position ->
                    deleteNote(position)
                }
                binding.notesRecyclerView.adapter = adapter
            }
        }
    }

    private fun deleteNote(position: Int) {
        val note = adapter?.notes?.get(position)
        if (note != null) {
            viewModel?.processIntent(NoteIntent.DeleteNote(note))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}