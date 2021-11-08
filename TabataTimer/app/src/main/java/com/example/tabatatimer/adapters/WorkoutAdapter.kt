package com.example.tabatatimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.data.Workout
import com.example.tabatatimer.fragments.LandingFragmentDirections
import android.R




class WorkoutAdapter(): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>(), AdapterView.OnItemSelectedListener {
    private var workouts: List<Workout>? = null
    private var fragment: Fragment? = null

    constructor(workouts: List<Workout>, fragment: Fragment) : this(){
        this.workouts = workouts
        this.fragment = fragment
    }

    class WorkoutViewHolder(val workoutView: View): RecyclerView.ViewHolder(workoutView)

    fun setWorkouts(newWorkoutList: List<Workout>)
    {
        this.workouts = newWorkoutList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.example.tabatatimer.R.layout.workout_cardview,
            parent,
            false)


        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutAdapter.WorkoutViewHolder, position: Int) {
        val workoutName = holder.workoutView.findViewById<TextView>(com.example.tabatatimer.R.id.workoutNameText)
        val workoutTime = holder.workoutView.findViewById<TextView>(com.example.tabatatimer.R.id.workoutTimeText)

        setSpinner(holder.workoutView)

        val workout = workouts?.get(position) ?: Workout(0, "", 0.0);
        workoutName.text = workout.name
        workoutTime.text = workout.length.toString() + " minutes"

        holder.workoutView.setOnClickListener {
            findNavController(fragment!!).navigate(
                LandingFragmentDirections.actionLandingFragmentToIntervalListFragment(
                    Workout(workout.workoutId, workout.name, workout.length)))
        }


    }

    override fun getItemCount(): Int = workouts ?.size?: 0

    private fun setSpinner(view: View)
    {
        val spinner: Spinner = view.findViewById(com.example.tabatatimer.R.id.spinner)
        ArrayAdapter.createFromResource(
            view.context,
            com.example.tabatatimer.R.array.manipulate_workout,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun navigateToEditWorkout()
    {


    }

}