package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.Workout
import com.example.tabatatimer.data.WorkoutWithIntervals

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM workout")
    fun getAll(): LiveData<List<Workout>>

    @Query("Select * FROM workout WHERE workoutId=:id")
    fun getById(id: Int): LiveData<WorkoutWithIntervals>

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("DELETE FROM interval WHERE workoutId=:workoutId")
    suspend fun deleteWorkoutIntervals(workoutId:Int)

    @Query("Delete FROM sequenceworkoutcrossref WHERE fk_workout_id=:workoutId")
    suspend fun deleteWorkoutRefInSequences(workoutId: Int)

    @Transaction
    suspend fun deleteWorkoutWithIntervals(workout: Workout)
    {
        deleteWorkoutIntervals(workout.workoutId!!)
        deleteWorkoutRefInSequences(workout.workoutId)
        delete(workout)
    }

    @Query("DELETE FROM workout")
    suspend fun deleteAllWorkouts()

    @Query("DELETE FROM interval")
    suspend fun deleteAllIntervals()

    @Query("DELETE FROM sequenceofworkouts")
    suspend fun deleteAllSequences()

    @Query("DELETE FROM SEQUENCEWORKOUTCROSSREF")
    suspend fun deleteAllRefs()

    @Transaction
    suspend fun deleteAllSequencesTrans()
    {
        deleteAllRefs()
        deleteAllSequences()
    }

    @Transaction
    @Query("SELECT * FROM Workout")
    fun getWorkoutsWithIntervals(): LiveData<List<WorkoutWithIntervals>>

    @Transaction
    @Query("SELECT * FROM Workout WHERE workoutId=:workoutId")
    suspend fun getWorkoutWithIntervalsById(workoutId: Int): WorkoutWithIntervals

    @Query("SELECT * FROM SequenceWorkoutCrossRef INNER JOIN WORKOUT ON fk_workout_id=workoutId")

    @Transaction
    suspend fun wipeData(){
        deleteAllIntervals()
        deleteAllSequencesTrans()
        deleteAllWorkouts()
    }
}