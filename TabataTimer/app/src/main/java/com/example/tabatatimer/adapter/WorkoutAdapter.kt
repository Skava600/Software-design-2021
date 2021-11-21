package com.example.tabatatimer.adapter

import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.data.*
import com.example.database.AppDatabase
import com.example.database.IntervalRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.fragment.LandingFragment
import com.example.tabatatimer.fragment.LandingFragmentDirections
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import android.widget.AdapterView.OnItemClickListener


class WorkoutAdapter(): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {



    private var datas : List<SequenceWorkoutData>? = null
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null

    private var onItemClickListener: OnItemClickListener? = null



    constructor(fragment: Fragment, onItemClickListener: OnItemClickListener) : this() {
        this.fragment = fragment
        this.onItemClickListener = onItemClickListener
        this.viewModel = ViewModelProviders.of(fragment).get(WorkoutViewModel::class.java)

    }



    class WorkoutViewHolder(val workoutView: View,
                            private val onOnItemClickListener: OnItemClickListener):
        RecyclerView.ViewHolder(workoutView), View.OnClickListener {
        init {
            workoutView.setOnClickListener(this);
        }
        override fun onClick(view:View?) {
            workoutView.findViewById<ImageButton>(R.id.popUpMenuButton).callOnClick()
            onOnItemClickListener.onItemClick(null, view, adapterPosition, view!!.id.toLong())
        }
    }

    fun setData(newSequenceWorkoutData: List<SequenceWorkoutData>)
    {
        this.datas = newSequenceWorkoutData
        notifyDataSetChanged()
    }



    fun getData(): List<SequenceWorkoutData> = this.datas!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutAdapter.WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )
        return WorkoutViewHolder(view, onItemClickListener!!)

    }

    override fun getItemViewType(position: Int): Int {
        return if (datas!![position].type == 1) {
            R.layout.sequence_cardview
        } else {
            R.layout.workout_cardview
        }
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        if (datas?.get(position)?.type == 1)
        {
            val sequenceNameText =
                holder.workoutView.findViewById<TextView>(R.id.sequenceNameText)

            val sequence = datas?.get(position)?.sequence

            setBackgroundViewColor(holder, position, true)
        }
        else {
            initWorkoutViewHolder(holder, position)
        }




    }

    private fun setBackgroundViewColor(viewHolder: WorkoutAdapter.WorkoutViewHolder, position: Int, isSequence: Boolean) {
        if (isSequence)
        {

        }
        else{
            val color = datas!![position].workout!!.color
            viewHolder.workoutView.findViewById<CardView>(
                R.id.workout_cardview
            ).setCardBackgroundColor(color)

            viewHolder.workoutView.findViewById<ImageButton>(
                R.id.popUpMenuButton
            ).setBackgroundColor(color)
        }
    }


    override fun getItemCount(): Int {
        return datas?.count() ?: 0
    }


    private fun initWorkoutViewHolder(holder: WorkoutViewHolder, position: Int)
    {
        val workoutName =
            holder.workoutView.findViewById<TextView>(R.id.workoutNameText)
        val workoutTime =
            holder.workoutView.findViewById<TextView>(R.id.intervalTime)

        val workout = datas?.get(position)?.workout!!
        workoutName.text = workout.name
        workoutTime.text = workout.length.toString() + " minutes"

        setBackgroundViewColor(holder, position, false)

        val popUpMenuButton = holder.workoutView.findViewById<ImageButton>(R.id.popUpMenuButton)

        popUpMenuButton.setOnClickListener {
            val landFrag = fragment as LandingFragment
            if (!landFrag.isInitSequence!!) {
                val popup = PopupMenu(holder.workoutView.context, popUpMenuButton)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.actions_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    val i = it.getItemId()
                    when (i) {
                        R.id.menuDelete -> {
                            viewModel!!.delete(workout)
                            true
                        }
                        R.id.menuEdit -> {
                            findNavController(fragment!!).navigate(
                                LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(
                                    workout
                                )
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

                }
                popup.show()
            }
        }

    }

}