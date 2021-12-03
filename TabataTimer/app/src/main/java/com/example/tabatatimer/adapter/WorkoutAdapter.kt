package com.example.tabatatimer.adapter

import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.data.*
import com.example.tabatatimer.R
import com.example.tabatatimer.fragment.LandingFragment
import com.example.tabatatimer.fragment.LandingFragmentDirections
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.sequence_cardview.view.*
import kotlinx.android.synthetic.main.workout_cardview.view.*


class WorkoutAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private var datas : List<SequenceWorkoutData>? = null
    private var fragment: Fragment? = null
    private var viewModel: WorkoutViewModel? = null

    private var onItemClickListener: OnItemClickListener? = null



    constructor(fragment: Fragment, onItemClickListener: OnItemClickListener?) : this() {
        this.fragment = fragment
        this.onItemClickListener = onItemClickListener
        this.viewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]

    }



    class WorkoutViewHolder(
        private val workoutView: View,
        private val onItemClickListener: OnItemClickListener,
        private val fragment: Fragment):
        RecyclerView.ViewHolder(workoutView), View.OnClickListener {
        init {
            workoutView.setOnClickListener(this);
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
                if (!(fragment as LandingFragment).isInitSequence!!) {
                    val popup = PopupMenu(workoutView.context, popUpMenuButton)
                    val inflater: MenuInflater = popup.menuInflater
                    inflater.inflate(R.menu.actions_menu, popup.menu)
                    popup.setOnMenuItemClickListener {
                        onWorkoutMenuClickListener(it, workout)
                    }

                    popup.show()
                }
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
                    findNavController(fragment).navigate(
                        LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(
                            workout
                        )
                    )
                    return true
                }
                R.id.menuPreview -> {
                    //do something
                    return true;
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

    class SequenceViewHolder(private val sequenceView: View,
                             private val onItemClickListener: OnItemClickListener,
                             private val fragment: Fragment):
            RecyclerView.ViewHolder(sequenceView), View.OnClickListener {

        val viewModel : WorkoutViewModel
        init {
            sequenceView.setOnClickListener(this);
            viewModel = ViewModelProvider(fragment)[WorkoutViewModel::class.java]
        }

        fun bind(position: Int, sequence: SequenceWithWorkouts) {
            if (sequence.workouts.count() == 1)
            {
                viewModel.deleteSequence(sequence.sequenceOfWorkouts)
            }
            val color = sequence.sequenceOfWorkouts.color
            sequenceView.sequence_cardview.setCardBackgroundColor(color)

            sequenceView.seqPopUpMenuButton.setBackgroundColor(color)

            val sequenceNameText = sequenceView.sequenceNameText

            val sequenceContent = sequenceView.sequenceContent

            val sequenceTimeText = sequenceView.sequenceTime

            sequenceNameText.text = sequenceView.context.getString(R.string.sequence_title)
            sequenceContent.text = ""
            var i = 0
            var time = 0
            while (i < sequence.workouts.count())
            {
                if (i < 5)
                {
                    sequenceContent.append("\n" + sequence.workouts[i].workout.name +
                            "\t\t" + Interval.getIntervalDuration(sequence.workouts[i].workout.length))
                }

                time += sequence.workouts[i].workout.length
                i++
            }

            if (sequence.workouts.count() > 5)
            {
                sequenceContent.append("\n" + ". . .")
            }

            sequenceTimeText.text = Interval.getIntervalDuration(time)

            val popupMenu = sequenceView.findViewById<ImageButton>(R.id.seqPopUpMenuButton)
            popupMenu.setOnClickListener {
                val landFrag = fragment as LandingFragment
                if (!landFrag.isInitSequence!!) {
                    val popup = PopupMenu(sequenceView.context, popupMenu)
                    val inflater: MenuInflater = popup.menuInflater
                    inflater.inflate(R.menu.actions_menu, popup.menu)
                    popup.setOnMenuItemClickListener {
                        onSequenceMenuClickListener(it, sequence.sequenceOfWorkouts)
                    }

                    popup.show()
                }
            }
        }

        private fun onSequenceMenuClickListener(it:MenuItem, sequenceOfWorkouts: SequenceOfWorkouts): Boolean{
            when (it.itemId) {
                R.id.menuDelete -> {
                    viewModel.deleteSequence(sequenceOfWorkouts)
                    return true
                }
                R.id.menuEdit -> {
                    findNavController(fragment).navigate(
                        LandingFragmentDirections.actionLandingFragmentToSequenceListFragment(
                            sequenceOfWorkouts
                        ))
                    return true
                }
                R.id.menuPreview -> {
                    //do something
                    return true;
                }
                else -> {
                    return false
                }
            }
        }


        override fun onClick(view: View?) {
            sequenceView.findViewById<ImageButton>(R.id.seqPopUpMenuButton).callOnClick()
            onItemClickListener.onItemClick(null, view, adapterPosition, view!!.id.toLong())
        }
    }


    fun setData(newSequenceWorkoutData: List<SequenceWorkoutData>)
    {
        this.datas = newSequenceWorkoutData
        notifyDataSetChanged()
    }



    fun getData(): List<SequenceWorkoutData> = this.datas!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )

        if (viewType == R.layout.workout_cardview) {
            return WorkoutViewHolder(view, onItemClickListener!!, fragment!!)
        }

        return SequenceViewHolder(view, onItemClickListener!!, fragment!!)

    }

    override fun getItemViewType(position: Int): Int {
        return if (datas!![position].type == 1) {
            R.layout.sequence_cardview
        } else {
            R.layout.workout_cardview
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is WorkoutViewHolder -> {
                holder.bind(position, datas!!.get(position).workout!!)
            }

            is SequenceViewHolder -> {
                holder.bind(position, datas!!.get(position).sequence!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return datas?.count() ?: 0
    }

}