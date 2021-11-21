package com.example.database

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.Workout

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    fun getAllWorkouts(): LiveData<List<Workout>> = workoutDao.getAll()


    suspend fun insert(workout: Workout) {
        workoutDao.insert(workout)
    }

    suspend fun update(workout: Workout) {
        workoutDao.update(workout)
    }

    suspend fun delete(workout: Workout) {
        workoutDao.delete(workout)
    }

    suspend fun deleteAllWorkouts(){
        workoutDao.deleteAllWorkouts()
    }
}