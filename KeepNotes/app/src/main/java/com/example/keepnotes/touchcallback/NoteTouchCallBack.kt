package com.example.keepnotes.touchcallback

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.NoteAdapter
import com.example.keepnotes.models.Note
import com.example.keepnotes.viewmodel.NoteViewModel

class NoteTouchCallBack(private var noteAdapter: NoteAdapter,
                        private var fragment: Fragment): ItemTouchHelper.Callback()
{
    private var viewModel = ViewModelProvider(fragment)[NoteViewModel::class.java]
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = UP or DOWN or LEFT or RIGHT
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        noteAdapter.onRowMoved(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        persistIndexUpdates()
    }

    private fun persistIndexUpdates() {
        val notes = noteAdapter.getNotes()
        notes.forEachIndexed{index, note ->
            note.index = index
            viewModel.update(note)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    interface ItemTouchHelperContract {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
    }

}