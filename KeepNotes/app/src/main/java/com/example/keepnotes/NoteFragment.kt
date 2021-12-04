package com.example.keepnotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.keepnotes.models.Note
import com.example.keepnotes.viewmodel.NoteViewModel
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class NoteFragment : Fragment() {
    private val viewModel : NoteViewModel by activityViewModels()
    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        //viewModel.insert(Note(null, "suka", "pidor\npizda","", Date().time.toString() ))

        val noteAdapter = NoteAdapter()
        viewModel.getAllNotes().observe(
            viewLifecycleOwner,
            {
                    notes -> noteAdapter.setData(notes)
            })
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = noteAdapter
            }
        }
        return view
    }


}