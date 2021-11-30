package com.example.tabatatimer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.AppDatabase
import com.example.database.SequenceRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.adapter.SequenceCrossRefAdapter
import com.example.tabatatimer.itemCallBack.WorkoutRefsTouchCallback
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import kotlinx.android.synthetic.main.fragment_sequence_list.view.*

class SequenceListFragment: Fragment(), AdapterView.OnItemClickListener {
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

        rootView.start_sequence.setOnClickListener {
//            viewModel.getAllSequences().observe(viewLifecycleOwner,
//                {
//                    sequences -> viewModel.getAllWorkInt().observe(viewLifecycleOwner,{
//                        workouts -> workouts.all{workoutWithIntervals -> workoutWithIntervals.workout.workoutId == args.sequence.}
//                    })
//                })
            val appDatabase = AppDatabase.getInstance(context)

        }


        return rootView
    }

    private fun initRecyclerView(workoutList: RecyclerView): RecyclerView {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        val workoutRefAdapter =  SequenceCrossRefAdapter(this, this)

        val appDatabase = AppDatabase.getInstance(context)
        sequenceRepository = SequenceRepository((appDatabase.sequenceDao()))

        sequenceRepository.getSequenceRefsById(args.sequence.sequenceId!!).observe(viewLifecycleOwner,
            {
                    sequenceRefs ->
                workoutRefAdapter.setData(sequenceRefs.toMutableList())
            })

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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }
}