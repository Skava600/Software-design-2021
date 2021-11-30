package com.example.tabatatimer.itemCallBack

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapter.SequenceCrossRefAdapter
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import com.google.android.material.snackbar.Snackbar

class WorkoutRefsTouchCallback(private var cardAdapter: SequenceCrossRefAdapter,
private var fragment: Fragment
) : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT) {
    private var viewModel = ViewModelProvider(fragment).get(WorkoutViewModel::class.java)
    private var wasSwiped = false

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        val adapter = recyclerView.adapter as SequenceCrossRefAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        adapter.moveItem(from, to)
        adapter.notifyItemMoved(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val seqCrossRefToDelete = cardAdapter.getSequenceCrossRefs()[position]

        viewModel.deleteSequenceCrossRef(seqCrossRefToDelete)
        showUndoSnackbar(seqCrossRefToDelete)
        wasSwiped = true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (!wasSwiped) {
            persistIndexUpdates()
        }
        wasSwiped = false
    }

    private fun showUndoSnackbar(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef) {
        val snackbar = Snackbar.make(fragment.requireView(), "Interval deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insertSequenceCrossRef(sequenceWorkoutCrossRef)
            cardAdapter.insertItem(sequenceWorkoutCrossRef.workoutIndex, sequenceWorkoutCrossRef)
            persistIndexUpdates()

        }

        snackbar.show()
    }

    private fun persistIndexUpdates() {
        val crossRefs = cardAdapter.getSequenceCrossRefs()
        crossRefs.forEachIndexed { index, ref ->
            ref.workoutIndex = index
            viewModel.updateSequenceCrossRef(ref)
        }
    }
}