package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.WorkoutWithIntervals


@Dao
interface IntervalDao {

    @Insert
    suspend fun insert(vararg interval: Interval)


    @Query("SELECT * FROM interval WHERE workoutId = :workoutId")
    fun getByWorkout(workoutId: Int): LiveData<List<Interval>>

    @Transaction
    @Query("Select * FROM Workout WHERE workoutId = :workoutId")
    fun getWorkoutWithIntervals(workoutId: Int): LiveData<WorkoutWithIntervals>

    @Update
    suspend fun update(vararg interval: Interval)


    @Delete
    suspend fun delete(interval: Interval)
}