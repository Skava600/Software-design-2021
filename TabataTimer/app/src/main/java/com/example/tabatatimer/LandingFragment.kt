package com.example.tabatatimer


import android.net.wifi.WpsInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
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

        val workouts = listOf<Workout>(
            Workout(1, "Бегит", 40.0),
            Workout(2, "Атжуманя", 20.0))
        recyclerLayout = LinearLayoutManager(this.context)
        recyclerAdapter = WorkoutAdapter(workouts)
        view.findViewById<RecyclerView>(R.id.cardRecyclerView).apply {
            layoutManager = recyclerLayout
            adapter = recyclerAdapter
        }
        return view
    }


}