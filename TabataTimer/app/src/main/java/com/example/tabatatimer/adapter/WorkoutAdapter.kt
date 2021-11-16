package com.example.tabatatimer.adapter

import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.data.*
import com.example.database.AppDatabase
import com.example.database.IntervalRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.fragment.LandingFragmentDirections
import com.example.tabatatimer.viewmodels.WorkoutViewModel


class WorkoutAdapter(): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>(), AdapterView.OnItemSelectedListener {



    private var datas : List<SequenceWorkoutData>? = null
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null

    constructor(fragment: Fragment) : this() {
        this.fragment = fragment
        this.viewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)
    }

    class WorkoutViewHolder(val workoutView: View): RecyclerView.ViewHolder(workoutView)

    fun setData(newSequenceWorkoutData: List<SequenceWorkoutData>)
    {
        this.datas = newSequenceWorkoutData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                viewType,
                parent,
                false
            )
            return WorkoutViewHolder(view)

    }

    override fun getItemViewType(position: Int): Int {
        if(datas!![position].type == 1)
        {
            return R.layout.sequence_cardview
        }
        else
        {
            return R.layout.workout_cardview
        }
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        if (datas?.get(position)?.type == 1)
        {
            val sequenceNameText =
                holder.workoutView.findViewById<TextView>(R.id.sequenceNameText)

            val sequence = datas?.get(position)?.sequence
        }
        else {
            val workoutName =
                holder.workoutView.findViewById<TextView>(R.id.workoutNameText)
            val workoutTime =
                holder.workoutView.findViewById<TextView>(R.id.intervalTime)

            val workout = datas?.get(position)?.workout!!
            workoutName.text = workout.name
            workoutTime.text = workout.length.toString() + " minutes"

            val popUpMenuButton = holder.workoutView.findViewById<ImageButton>(R.id.popUpMenuButton)
            popUpMenuButton.setOnClickListener {
                val popup = PopupMenu(holder.workoutView.context, popUpMenuButton)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.actions_menu, popup.menu)
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener(){
                    val i = it.getItemId();
                    when (i) {
                        R.id.menuDelete -> {
                            viewModel!!.delete(workout)
                            true
                        }
                        R.id.menuEdit -> {
                            findNavController(fragment!!).navigate(
                                LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(
                                    workout)
                            )
                            true
                        }
                        R.id.menuPreview -> {
                            //do something
                            true;
                        }
                        else -> {
                            false
                        }
                    }

                })
                popup.show()
            }

//            holder.workoutView.setOnClickListener {
//                popUpMenuButton.callOnClick()
//            }

            holder.workoutView.findViewById<ImageButton>(R.id.start_workout).setOnClickListener {
                findNavController(fragment!!).navigate(
                    LandingFragmentDirections.actionWorkoutCardViewToTimerActivity(
                        getIntervalListDataFromWorkout(workout.workoutId!!)))
            }
        }


    }

    override fun getItemCount(): Int {
//        val seqSize = sequences?.size ?: 0
//        val workSize = workouts?.size ?: 0
//        return seqSize + workSize
        return datas?.count() ?: 0
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun getData(): List<SequenceWorkoutData> = this.datas!!

    private fun getIntervalListDataFromWorkout(workoutId: Int): IntervalListData
    {
        val appDatabase = AppDatabase.getInstance(fragment!!.context)
        val intervalRepository = IntervalRepository(appDatabase.intervalDao())
        var intervalList: IntervalListData? = null
        intervalRepository.getIntervalsByWorkout(workoutId).observe(
            fragment!!.viewLifecycleOwner,
            Observer<List<Interval>> { intervals ->
                intervalList = IntervalListData(intervals)
            }
        )
        return intervalList!!
    }

}