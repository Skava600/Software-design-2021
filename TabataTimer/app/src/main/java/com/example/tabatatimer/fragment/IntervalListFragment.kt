package com.example.tabatatimer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.AppDatabase
import com.example.tabatatimer.data.Interval
import com.example.database.IntervalRepository
import com.example.database.WorkoutRepository
import com.example.tabatatimer.adapter.IntervalAdapter
import com.example.tabatatimer.R
import com.example.tabatatimer.itemCallBack.IntervalTouchCallback
import com.example.tabatatimer.viewmodels.IntervalViewModel
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import kotlinx.android.synthetic.main.fragment_interval_list.view.*


class IntervalListFragment() : Fragment(), IntervalAdapter.OnIntervalChangedListener {

    private val args: IntervalListFragmentArgs by navArgs()
    private val viewModel: IntervalViewModel by activityViewModels()
    private lateinit var rootView: View

    private lateinit var intervalRepository: IntervalRepository
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var listener: IntervalAdapter.OnIntervalChangedListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        this.rootView = rootView
        val recyclerView = initRecyclerView(rootView.intervalList)

        rootView.intervalViewWorkoutName.text = args.workout.name
        rootView.intervalViewTotalTime.text =  Interval.getIntervalDuration(args.workout.length)

        rootView.addInterval.setOnClickListener {
            val dialog = IntervalDialogFragment(this)
            val bundle = Bundle()

            bundle.putInt("newIndex", recyclerView.adapter?.itemCount as Int)

            bundle.putSerializable("workout", args.workout)
            dialog.arguments = bundle

            dialog.show(requireActivity().supportFragmentManager, "fragment_interval_dialog_0")
        }


        return rootView
    }


    private fun initRecyclerView(recyclerView: RecyclerView): RecyclerView {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        listener = this
        val intervalAdapter = IntervalAdapter(listener)

        val appDatabase = AppDatabase.getInstance(context)
        intervalRepository = IntervalRepository(appDatabase.intervalDao())
        workoutRepository = WorkoutRepository(appDatabase.workoutDao())

        intervalRepository.getWorkoutWithIntervals(args.workout.workoutId!!).observe(viewLifecycleOwner,
            {
                    workoutWithIntervals ->
                intervalAdapter.setData(workoutWithIntervals.intervals.toMutableList())
            }
        )

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = intervalAdapter
        }

        ItemTouchHelper(
            IntervalTouchCallback(intervalAdapter, this, args.workout)
                ).attachToRecyclerView(recyclerView)
        return recyclerView
    }


    override fun onIntervalTimeChanged(interval: Interval, newTime: Int) {
        args.workout.length -= interval.time!!
        args.workout.length += newTime
        interval.time = newTime
        viewModel.update(interval)
        val workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)
        workoutViewModel.update(args.workout)
        rootView.intervalViewTotalTime.text = Interval.getIntervalDuration(args.workout.length)
        if (newTime == 0)
        {
            viewModel.delete(interval)
        }
    }

    override fun onDeleteInterval(interval: Interval) {
        args.workout.length -= interval.time!!
        viewModel.delete(interval)
        val workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)
        workoutViewModel.update(args.workout)
        rootView.intervalViewTotalTime.text = Interval.getIntervalDuration(args.workout.length)
    }
}