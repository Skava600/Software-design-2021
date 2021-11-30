package com.example.tabatatimer.adapter

import android.view.*
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.database.AppDatabase
import com.example.database.WorkoutRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.fragment.LandingFragmentDirections
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import kotlinx.android.synthetic.main.workout_cardview.view.*

class SequenceCrossRefAdapter():  RecyclerView.Adapter<SequenceCrossRefAdapter.WorkoutViewHolder>() {
    private var sequenceRefs: MutableList<SequenceWorkoutCrossRef>? = null
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null
    private var onItemClickListener: AdapterView.OnItemClickListener? = null

    constructor(fragment: Fragment, onItemClickListener: AdapterView.OnItemClickListener?) : this() {
        this.fragment = fragment
        this.onItemClickListener = onItemClickListener
        this.viewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]

    }

    fun setData(sequenceRefs: MutableList<SequenceWorkoutCrossRef>)
    {
        this.sequenceRefs = sequenceRefs.sortedBy {
            it.workoutIndex
        }.toMutableList()

        notifyDataSetChanged()
    }

    class WorkoutViewHolder(
        private val workoutView: View,
        private val onItemClickListener: AdapterView.OnItemClickListener,
        private val fragment: Fragment
    ):
        RecyclerView.ViewHolder(workoutView), View.OnClickListener {
        init {
            workoutView.setOnClickListener(this)
        }

        fun bind(position: Int, workout: Workout) {
            val color = workout.color
            workoutView.workout_cardview.setCardBackgroundColor(color)

            workoutView.popUpMenuButton.setBackgroundColor(color)

            val workoutName = workoutView.workoutNameText
            val workoutTime = workoutView.intervalTime

            workoutName.text = workout.name
            workoutTime.text = Interval.getIntervalDuration(workout.length) + " minutes"

            val popUpMenuButton = workoutView.popUpMenuButton

            popUpMenuButton.setOnClickListener {
                val popup = PopupMenu(workoutView.context, popUpMenuButton)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.actions_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    onWorkoutMenuClickListener(it, workout)
                }

                popup.show()
            }
        }

        private fun onWorkoutMenuClickListener(it: MenuItem, workout: Workout) : Boolean
        {
            val viewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]
            when (it.itemId) {
                R.id.menuDelete -> {
                    viewModel.delete(workout)
                    return true
                }
                R.id.menuEdit -> {
                    NavHostFragment.findNavController(fragment).navigate(
                        LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(
                            workout
                        )
                    )
                    return true
                }
                R.id.menuPreview -> {
                    //do something
                    return true
                }
                else -> {
                    return false
                }
            }
        }

        override fun onClick(view:View?) {
            workoutView.findViewById<ImageButton>(R.id.popUpMenuButton).callOnClick()
            onItemClickListener.onItemClick(null, view, adapterPosition, view!!.id.toLong())
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(
            R.layout.workout_cardview,
            parent,
            false
        )

        return WorkoutViewHolder(view, onItemClickListener!!, fragment!!)

    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {

        val appDatabase = AppDatabase.getInstance(fragment!!.context)
        val workoutRepository = WorkoutRepository(appDatabase.workoutDao())
        workoutRepository.getWorkoutById(sequenceRefs!![position].workoutId).observe(
            fragment!!.viewLifecycleOwner,
            {
                holder.bind(position, it)
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

    fun insertItem(at: Int, sequenceWorkoutCrossRef: SequenceWorkoutCrossRef) {
        if (sequenceRefs != null && sequenceRefs!!.isEmpty()) {
            sequenceRefs?.add(sequenceWorkoutCrossRef)
        } else {
            sequenceRefs?.add(at, sequenceWorkoutCrossRef)
        }
        notifyDataSetChanged()
    }
}