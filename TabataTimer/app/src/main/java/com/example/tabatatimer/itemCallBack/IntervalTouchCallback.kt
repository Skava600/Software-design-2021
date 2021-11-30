package com.example.tabatatimer.itemCallBack

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapter.IntervalAdapter
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.viewmodels.IntervalViewModel
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_interval_list.*

class IntervalTouchCallback(private var cardAdapter: IntervalAdapter,
                            private var fragment: Fragment,
                            private var workout: Workout
    ) : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT) {
        private var viewModel = ViewModelProvider(fragment)[IntervalViewModel::class.java]
        private var workoutViewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]
        private var wasSwiped = false

        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            val adapter = recyclerView.adapter as IntervalAdapter
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            adapter.moveItem(from, to)
            adapter.notifyItemMoved(from, to)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val intervalToDelete = cardAdapter.getIntervals()[position]

            processTimeChanges(intervalToDelete, false)

            viewModel.delete(intervalToDelete)
            showUndoSnackbar(intervalToDelete)
            wasSwiped = true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            if (!wasSwiped) {
                persistIndexUpdates()
            }
            wasSwiped = false
        }

        private fun showUndoSnackbar(interval: Interval) {
            val snackbar = Snackbar.make(fragment.requireView(), "Interval deleted", Snackbar.LENGTH_LONG)
            snackbar.setAction("Undo") {
                viewModel.insert(interval)
                cardAdapter.insertItem(interval.index, interval)
                persistIndexUpdates()

                processTimeChanges(interval, true)
            }

            snackbar.show()
        }

        private fun persistIndexUpdates() {
            val intervals = cardAdapter.getIntervals()
            intervals.forEachIndexed { index, interval ->
                interval.index = index
                viewModel.update(interval)
            }
        }

        private fun processTimeChanges(interval: Interval, isBeingAdded: Boolean) {

            if(interval.time != null) {
                // Update the time in the database
                if(isBeingAdded) {
                    workout.length += interval.time!!
                } else {
                    workout.length -= interval.time!!
                }
            }

            workoutViewModel.update(workout)

            fragment.intervalViewTotalTime.text = Interval.getIntervalDuration(workout.length)
        }
    }