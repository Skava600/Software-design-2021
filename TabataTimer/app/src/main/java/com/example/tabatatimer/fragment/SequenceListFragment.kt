package com.example.tabatatimer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.AppDatabase
import com.example.database.SequenceRepository
import com.example.database.WorkoutRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.adapter.SequenceCrossRefAdapter
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.data.WorkoutListData
import com.example.tabatatimer.data.WorkoutWithIntervals
import com.example.tabatatimer.itemCallBack.WorkoutRefsTouchCallback
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import kotlinx.android.synthetic.main.fragment_sequence_list.view.*

class SequenceListFragment: Fragment() {
    private val args: SequenceListFragmentArgs by navArgs()
    private val viewModel: WorkoutViewModel by activityViewModels()
    private lateinit var rootView: View

    private lateinit var sequenceRepository: SequenceRepository
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sequence_list, container, false)
        this.rootView = rootView
        val recyclerView = initRecyclerView(rootView.workoutList)
        setHasOptionsMenu(true)


        return rootView
    }

    private fun initRecyclerView(workoutList: RecyclerView): RecyclerView {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        val workoutRefAdapter =  SequenceCrossRefAdapter(this)

        val appDatabase = AppDatabase.getInstance(context)
        sequenceRepository = SequenceRepository((appDatabase.sequenceDao()))

        sequenceRepository.getLiveSequenceRefsById(args.sequence.sequenceId!!).observe(viewLifecycleOwner,
            {
                    sequenceRefs ->
                workoutRefAdapter.setSequenceRefs(sequenceRefs.toMutableList())
            })



        rootView.start_sequence.setOnClickListener {


            findNavController().navigate(
                SequenceListFragmentDirections.actionSequenceListFragmentToTimerActivity(
                    WorkoutListData(workoutRefAdapter.getWorkoutsWithIntervals())
                ))
        }

        workoutList.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = workoutRefAdapter
        }

        ItemTouchHelper(
            WorkoutRefsTouchCallback(workoutRefAdapter, this)
        ).attachToRecyclerView(workoutList)

        return workoutList
    }
}