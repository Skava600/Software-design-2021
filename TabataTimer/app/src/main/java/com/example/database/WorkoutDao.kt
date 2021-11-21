package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.data.WorkoutWithIntervals

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAll(): LiveData<List<Workout>>

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("DELETE from workout")
    suspend fun deleteAllWorkouts()

    @Transaction
    @Query("SELECT * FROM Workout")
    fun getWorkoutsWithIntervals(): LiveData<List<WorkoutWithIntervals>>

}