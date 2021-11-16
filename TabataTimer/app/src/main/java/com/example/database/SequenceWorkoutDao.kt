package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef

@Dao
interface SequenceWorkoutDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSequence(vararg : SequenceOfWorkouts)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSequenceSongRef(vararg :SequenceWorkoutCrossRef)

}