package com.example.database

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.data.WorkoutWithIntervals

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    fun getAllWorkouts(): LiveData<List<Workout>> = workoutDao.getAll()

    fun getWorkoutById(id: Int): LiveData<WorkoutWithIntervals> = workoutDao.getById(id)

    fun getWorkoutsWithInt(): LiveData<List<WorkoutWithIntervals>> = workoutDao.getWorkoutsWithIntervals()

    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }

    suspend fun update(workout: Workout) {
        workoutDao.update(workout)
    }

    suspend fun delete(workout: Workout) {
        workoutDao.deleteWorkoutWithIntervals(workout)
    }

    suspend fun wipeData(){
        workoutDao.wipeData()
    }
}