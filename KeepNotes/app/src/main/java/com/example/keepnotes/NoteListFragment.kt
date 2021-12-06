package com.example.keepnotes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.keepnotes.models.Note
import com.example.keepnotes.touchcallback.NoteTouchCallBack
import com.example.keepnotes.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class NoteListFragment : Fragment(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private val viewModel : NoteViewModel by activityViewModels()
    private var columnCount = 2
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var menu:Menu

    private var choosedNotes:MutableList<Note> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        setHasOptionsMenu(true)

        noteAdapter = NoteAdapter(this, this)
        viewModel.getAllNotes().observe(
            viewLifecycleOwner,
            {
                    notes -> noteAdapter.setData(notes)
            })
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = noteAdapter
            }

        view.findViewById<FloatingActionButton>(R.id.add_note).setOnClickListener {
            findNavController().navigate(
                NoteListFragmentDirections.actionFragmentNoteListToNoteFragment(noteAdapter.itemCount, false)
            )
        }

        ItemTouchHelper(
            NoteTouchCallBack(
                noteAdapter,
                this
            )
        ).attachToRecyclerView(recyclerView)
        return view
    }

    override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, viewId: Long) {
        if (choosedNotes.isNotEmpty()) {
            val note = noteAdapter.getNotes()[position]
            val border: Drawable?
            if (choosedNotes.find { it.noteId == note.noteId } != null) {
                border = ContextCompat.getDrawable(context!!, R.drawable.border)
                choosedNotes.removeIf { it.noteId == note.noteId }
                if (choosedNotes.isEmpty()){
                    changeVisibilityBarMenuItems()
                }
            } else {
                border = ContextCompat.getDrawable(context!!, R.drawable.border_choosed)
                choosedNotes.add(note)
            }

            view!!.findViewById<LinearLayout>(R.id.note_item)
                .background = border
        }
    }

    override fun onItemLongClick(p0: AdapterView<*>?, view: View?, position: Int, viewId: Long): Boolean {
        if (choosedNotes.isEmpty())
        {
            changeVisibilityBarMenuItems()

            val note = noteAdapter.getNotes()[position]
            choosedNotes.add(note)
            view!!.findViewById<LinearLayout>(R.id.note_item)
                .background =  ContextCompat.getDrawable(context!!, R.drawable.border_choosed)
            return true
        }
        else{
            return false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_list_app_bar, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun changeVisibilityBarMenuItems()
    {
        val actionDelete = menu.findItem(R.id.action_delete)
        actionDelete.isVisible = !actionDelete.isVisible

        val actionPalette = menu.findItem(R.id.action_palette)
        actionPalette.isVisible = !actionPalette.isVisible
    }


}