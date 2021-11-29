package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef

@Dao
interface SequenceWorkoutDao {


    @Insert
    suspend fun insertSequence(sequence : SequenceOfWorkouts) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSequenceSongRef(sequenceSongRef :SequenceWorkoutCrossRef)

    @Delete
    suspend fun deleteSequence(sequence: SequenceOfWorkouts)

    @Query("DELETE FROM sequenceworkoutcrossref WHERE fk_sequence_id=:sequenceId")
    suspend fun deleteSequenceCrossRef(sequenceId: Int)

    @Transaction
    suspend fun deleteSequenceWithRefs(sequence: SequenceOfWorkouts)
    {
        deleteSequenceCrossRef(sequence.sequenceId!!)
        deleteSequence(sequence)
    }


    @Transaction
    @Query("SELECT * FROM SequenceOfWorkouts")
    fun getSequencesWithWorkouts(): LiveData<List<SequenceWithWorkouts>>

}