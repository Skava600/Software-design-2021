package com.example.tabatatimer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.Interval
import com.example.tabatatimer.adapters.IntervalAdapter
import com.example.tabatatimer.R
import com.example.viewmodels.IntervalViewModel
import kotlinx.android.synthetic.main.fragment_interval_list.view.*


class IntervalListFragment : Fragment() {

    private val args: IntervalListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_interval_list, container, false)
        initRecyclerView(rootView.intervalList)

        requireActivity().title = args.workout.name

        rootView.addInterval.setOnClickListener {
            // TODO: Implement addition of new intervals
        }

        return rootView
    }


    private fun initRecyclerView(recyclerView: RecyclerView) {
        val recyclerLayout = LinearLayoutManager(requireActivity().applicationContext)
        val intervalAdapter = IntervalAdapter()

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayout
            adapter = intervalAdapter
        }

        val viewModel = ViewModelProviders.of(this).get(IntervalViewModel::class.java)

        viewModel.getIntervalsByWorkout(args.workout.workoutId!!).observe(viewLifecycleOwner,
            Observer<List<Interval>> { intervals ->
                intervalAdapter.setIntervals(intervals)
            }
        )
    }


}