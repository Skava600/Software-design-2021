package com.example.tabatatimer.itemCallBack

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.adapter.WorkoutAdapter
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.fragment.LandingFragment
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import com.google.android.material.snackbar.Snackbar

class WorkoutTouchCallback(private var adapter: WorkoutAdapter,
                           private var fragment: Fragment
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var viewModel: WorkoutViewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]


    override fun getSwipeDirs (recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        if ((fragment as LandingFragment).isInitSequence!!)
        {
            return 0
        }

        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val workoutToDelete = adapter.getData()[position]

        if (workoutToDelete.sequence == null) {
            viewModel.delete(workoutToDelete.workout!!)
            //showUndoSnackbar(workoutToDelete.workout!!)
        }
        else{
            viewModel.deleteSequence(workoutToDelete.sequence!!.sequenceOfWorkouts)
            //showUndoSnackbar(workoutToDelete.workout!!)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean { return true }

    private fun showUndoSnackbar(workout: Workout) {
        val snackbar = Snackbar.make(fragment.requireView(), "Workout deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insert(workout)
        }

        snackbar.show()
    }
}