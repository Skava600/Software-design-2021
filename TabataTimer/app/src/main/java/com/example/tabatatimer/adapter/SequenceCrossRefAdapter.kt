package com.example.tabatatimer.adapter

import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.database.AppDatabase
import com.example.database.WorkoutRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.data.WorkoutWithIntervals
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import kotlinx.android.synthetic.main.workout_cardview.view.*

class SequenceCrossRefAdapter():  RecyclerView.Adapter<SequenceCrossRefAdapter.WorkoutViewHolder>() {
    private var sequenceRefs: MutableList<SequenceWorkoutCrossRef>? = null
    private var workoutWithIntervals: MutableList<WorkoutWithIntervals>? = arrayListOf()
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null

    constructor(fragment: Fragment) : this() {
        this.fragment = fragment
        this.viewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]

    }

    fun setSequenceRefs(sequenceRefs: MutableList<SequenceWorkoutCrossRef>)
    {
        this.sequenceRefs = sequenceRefs.sortedBy {
            it.workoutIndex
        }.toMutableList()

        notifyDataSetChanged()
    }

    class WorkoutViewHolder(
        private val workoutView: View,
        private val fragment: Fragment
    ):
        RecyclerView.ViewHolder(workoutView){

        fun bind(position: Int, workout: Workout) {
            val color = workout.color
            workoutView.workout_cardview.setCardBackgroundColor(color)

            workoutView.popUpMenuButton.setBackgroundColor(color)

            val workoutName = workoutView.workoutNameText
            val workoutTime = workoutView.intervalTime

            workoutName.text = workout.name
            workoutTime.text = Interval.getIntervalDuration(workout.length) + " minutes"
            workoutView.popUpMenuButton.visibility = View.GONE
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(
            R.layout.workout_cardview,
            parent,
            false
        )

        return WorkoutViewHolder(view, fragment!!)

    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {

        val appDatabase = AppDatabase.getInstance(fragment!!.context)
        val workoutRepository = WorkoutRepository(appDatabase.workoutDao())
        workoutRepository.getWorkoutById(sequenceRefs!![position].workoutId).observe(
            fragment!!.viewLifecycleOwner,
            {
                holder.bind(position, it.workout)
                workoutWithIntervals!!.add(it)
            })

    }

    override fun getItemCount(): Int {
        return sequenceRefs?.size?: 0
    }

    fun moveItem(from: Int, to: Int) {
        val temp = sequenceRefs!![from]
        sequenceRefs?.removeAt(from)
        sequenceRefs?.add(to, temp)
    }

    fun getSequenceCrossRefs() : List<SequenceWorkoutCrossRef> = this.sequenceRefs!!

    fun getWorkoutsWithIntervals() : List<WorkoutWithIntervals> = this.workoutWithIntervals!!

    fun insertItem(at: Int, sequenceWorkoutCrossRef: SequenceWorkoutCrossRef) {
        if (sequenceRefs != null && sequenceRefs!!.isEmpty()) {
            sequenceRefs?.add(sequenceWorkoutCrossRef)
        } else {
            sequenceRefs?.add(at, sequenceWorkoutCrossRef)
        }
        notifyDataSetChanged()
    }
}