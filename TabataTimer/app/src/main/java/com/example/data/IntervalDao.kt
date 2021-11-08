package com.example.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface IntervalDao {

    @Insert
    fun insert(vararg interval: Interval)


    @Query("SELECT * FROM interval WHERE workoutId = :workoutId")
    fun getByWorkout(workoutId: Int): LiveData<List<Interval>>


    @Update
    fun update(vararg interval: Interval)


    @Delete
    fun delete(interval: Interval)
}