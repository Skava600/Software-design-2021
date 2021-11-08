package com.example.tabatatimer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.Workout
import com.example.tabatatimer.R
import com.example.tabatatimer.adapters.WorkoutAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LandingFragment : Fragment() {

    var recyclerLayout: LinearLayoutManager? = null
    var recyclerAdapter: WorkoutAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        val mFab = view.findViewById<FloatingActionButton>(R.id.addWorkoutButton)
        mFab.setOnClickListener {
            TODO("implement navigating to creating new workout")
        }

        val workouts = listOf(
            Workout(1,"Workout 1", 0.4),
            Workout(2,"Workout 2", 1.1),
            Workout(3, "Back Workout", 16.8) )
        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutAdapter(workouts, this)
        view.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }

        return view
    }


}