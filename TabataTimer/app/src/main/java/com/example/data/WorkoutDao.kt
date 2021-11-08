package com.example.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WorkoutDao {

    @Insert
    fun insert(vararg workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAll(): LiveData<List<Workout>>

    @Update
    fun update(workout: Workout)

    @Delete
    fun delete(workout: Workout)
}