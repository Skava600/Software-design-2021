package com.example.database

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.WorkoutWithIntervals

class IntervalRepository(private val intervalDao: IntervalDao) {


    fun getWorkoutWithIntervals(workoutId: Int): LiveData<WorkoutWithIntervals>{
        return this.intervalDao.getWorkoutWithIntervals(workoutId)
    }

    suspend fun insert(item: Interval) {
        intervalDao.insert(item)
    }

    suspend fun update(item: Interval) {
        intervalDao.update(item)
    }


    suspend fun delete(item: Interval) {
        intervalDao.delete(item)
    }
}