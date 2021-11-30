package com.example.tabatatimer.fragment


import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.data.*
import com.example.database.AppDatabase
import com.example.database.SequenceRepository
import com.example.database.WorkoutRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.adapter.WorkoutAdapter
import com.example.tabatatimer.itemCallBack.WorkoutTouchCallback
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_landing.view.*


class LandingFragment : Fragment(), AdapterView.OnItemClickListener {

    var isInitSequence: Boolean? = false

    private lateinit var viewModel: WorkoutViewModel

    private var recyclerLayout: LinearLayoutManager? = null
    private lateinit var actionbar: ActionBar
    var recyclerAdapter: WorkoutAdapter? = null
    private lateinit var menu: Menu

    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var sequenceRepository: SequenceRepository

    private var workoutsToSequence: MutableList<Int>? = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        actionbar = (activity as AppCompatActivity).supportActionBar!!
        val mFab = view.findViewById<FloatingActionButton>(R.id.addWorkoutButton)
        mFab.setOnClickListener {
            val dialog = WorkoutDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "AddWorkoutFragment")
        }

        viewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]

        val appDatabase = AppDatabase.getInstance(context)
        workoutRepository = WorkoutRepository(appDatabase.workoutDao())
        sequenceRepository = SequenceRepository(appDatabase.sequenceDao())

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutAdapter(this, this)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_close_24)
        val addSequence = view.findViewById<FloatingActionButton>(R.id.newSequenceButton)
        addSequence.setOnClickListener {
            if (recyclerAdapter!!.getData().count() >= 2)
                showCreatingSequenceMenu()
        }

        ItemTouchHelper(
            WorkoutTouchCallback(
                recyclerAdapter!!,
                this
            )
        ).attachToRecyclerView(view.cardRecyclerView)



         viewModel.getAllSequences().observe(
            viewLifecycleOwner,
            {
                sequences -> viewModel.getAllWorkouts().observe(viewLifecycleOwner,
                {
                    workouts ->    recyclerAdapter?.setData(SequenceWorkoutData.merge(sequences, workouts))
                })
            })

        view.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.create_sequence_menu, menu);
        this.menu = menu
        menu.findItem(R.id.action_create_sequence).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean{
        if ((workoutsToSequence!!.count() < 2) && item.itemId == R.id.action_create_sequence)
        {
            return false
        }

        hideCreatingSequenceMenu()

        return when (item.itemId) {

            R.id.action_create_sequence -> {
                insertSequence()
                return true
            }
            android.R.id.home -> {
                for (position in workoutsToSequence!!)
                {
                    val color = getColorData(recyclerAdapter!!.getData()[position].type, position)
                    val cardViewId = getCardViewId(recyclerAdapter!!.getData()[position].type)
                    recyclerLayout!!.findViewByPosition(position)!!.findViewById<CardView>(cardViewId)
                        .setCardBackgroundColor(color)
                }
                workoutsToSequence = mutableListOf()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (isInitSequence!!)
        {
            val cardViewId = getCardViewId(recyclerAdapter!!.getData()[p2].type)
            var color = getColorData(recyclerAdapter!!.getData()[p2].type, p2)

            if (!workoutsToSequence!!.contains(p2)) {
                color = resources.getColor(R.color.colorAccentGreen)
                workoutsToSequence!!.add(p2)
            }
            else {
                workoutsToSequence!!.remove(p2)
            }

            p1!!.findViewById<CardView>(cardViewId)
                .setCardBackgroundColor(color)
        }
    }

    private fun showCreatingSequenceMenu()
    {
        // HIDE INTERFACE
        this.requireView().findViewById<FloatingActionButton>(R.id.newSequenceButton).visibility = View.GONE
        this.requireView().findViewById<FloatingActionButton>(R.id.addWorkoutButton).visibility = View.GONE
        menu.findItem(R.id.action_settings).isVisible = false

        isInitSequence = true

        // SHOW CREATING OPTIONS
        actionbar.setDisplayHomeAsUpEnabled(true)
        menu.findItem(R.id.action_create_sequence).isVisible = true


    }

    private fun hideCreatingSequenceMenu()
    {
        this.requireView().findViewById<FloatingActionButton>(R.id.newSequenceButton).visibility = View.VISIBLE
        this.requireView().findViewById<FloatingActionButton>(R.id.addWorkoutButton).visibility = View.VISIBLE
        isInitSequence = false
        actionbar.setDisplayHomeAsUpEnabled(false)
        menu.findItem(R.id.action_settings).isVisible = true
        menu.findItem(R.id.action_create_sequence).isVisible = false
    }

    private fun insertSequence()
    {
        val addSequences: (Long) -> Unit = {
            var indexBehind = 0
            for (position in workoutsToSequence!!)
            {
                val data = recyclerAdapter!!.getData()[position]
                if (data.workout != null) {
                    viewModel.insertSequenceCrossRef(SequenceWorkoutCrossRef(it.toInt(), data.workout!!.workoutId!!, indexBehind))
                    indexBehind++
                    recyclerLayout!!.findViewByPosition(position)!!
                        .findViewById<CardView>(R.id.workout_cardview)
                        .setCardBackgroundColor(recyclerAdapter!!.getData()[position].workout!!.color)

                }
                else
                {
                    for (workout in data.sequence!!.workouts)
                    {
                        viewModel.insertSequenceCrossRef(SequenceWorkoutCrossRef(it.toInt(), workout.workoutId!!, indexBehind))
                        indexBehind++
                    }
                    recyclerLayout!!.findViewByPosition(position)!!
                        .findViewById<CardView>(R.id.sequence_cardview)
                        .setCardBackgroundColor(data.sequence!!.sequenceOfWorkouts.color)
                }
            }
            workoutsToSequence = mutableListOf()
        }
            viewModel.insertSequence(SequenceOfWorkouts(null, R.color.color3), addSequences)


    }

    private fun getCardViewId(type: Int): Int {
        return when(type){
            1 -> {
                R.id.sequence_cardview
            }
            2 -> {
                R.id.workout_cardview
            }
            else -> {
                0
            }
        }
    }

    private fun getColorData(type: Int, pos: Int): Int{
        return when(type){
            1 -> {
                recyclerAdapter!!.getData()[pos].sequence!!.sequenceOfWorkouts.color
            }
            2 -> {
                recyclerAdapter!!.getData()[pos].workout!!.color
            }
            else -> {
                0
            }
        }
    }
}