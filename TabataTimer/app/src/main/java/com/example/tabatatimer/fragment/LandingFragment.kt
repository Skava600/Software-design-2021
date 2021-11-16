package com.example.tabatatimer.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.data.*
import com.example.database.AppDatabase
import com.example.database.WorkoutRepository
import com.example.tabatatimer.R
import com.example.tabatatimer.adapter.WorkoutAdapter
import com.example.tabatatimer.itemCallBack.WorkoutTouchCallback
import com.example.tabatatimer.viewmodels.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_landing.view.*


class LandingFragment : Fragment() {

    private var recyclerLayout: LinearLayoutManager? = null
    var recyclerAdapter: WorkoutAdapter? = null
    private lateinit var workoutRepository: WorkoutRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        val mFab = view.findViewById<FloatingActionButton>(R.id.addWorkoutButton)
        mFab.setOnClickListener {
            val dialog = WorkoutDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "AddWorkoutFragment")
        }

        val viewModel = ViewModelProviders.of(this).get(WorkoutViewModel::class.java)


        val appDatabase = AppDatabase.getInstance(context)
        workoutRepository = WorkoutRepository(appDatabase.workoutDao())

        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutAdapter(this)

        ItemTouchHelper(
            WorkoutTouchCallback(
                recyclerAdapter!!,
                this
            )
        ).attachToRecyclerView(view.cardRecyclerView)

        workoutRepository.getAllSequences().observe(
            viewLifecycleOwner,
            {
                sequences -> viewModel.getAllWorkouts().observe(viewLifecycleOwner,
                {
                    workouts ->    recyclerAdapter?.setData(SequenceWorkoutData.merge(sequences, workouts))
                }
            )

            }
        )

        view.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }


        return view
    }


}