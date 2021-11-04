package com.example.tabatatimer

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.data.WorkoutDao

class WorkoutAdapter(): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var workouts: List<Workout>? = null

    constructor(workouts: List<Workout>) : this(){
        this.workouts = workouts
    }

    class WorkoutViewHolder(val workoutView: View): RecyclerView.ViewHolder(workoutView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.workout_cardview,
            parent,
            false)
        view.setOnClickListener {
            TODO("Implement callback on clicked item")
        }

        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workoutName = holder.workoutView.findViewById<TextView>(R.id.workoutNameText)
        val workoutTime = holder.workoutView.findViewById<TextView>(R.id.workoutTimeText)

        val workout = workouts?.get(position) ?: Workout(0,"", 0.0);

        workoutName.text = workout.name
        workoutTime.text = workout.time.toString() + " minutes"
    }

    override fun getItemCount(): Int {
        return workouts ?.size?: 0
    }

}