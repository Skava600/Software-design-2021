package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef

@Dao
interface SequenceWorkoutDao {


    @Query("SELECT * FROM sequenceworkoutcrossref WHERE fk_sequence_id=:fk_sequence_id")
    fun getAllLiveRefsBySequenceId(fk_sequence_id:Int): LiveData<List<SequenceWorkoutCrossRef>>

    @Insert
    suspend fun insertSequence(sequence : SequenceOfWorkouts) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSequenceSongRef(sequenceSongRef :SequenceWorkoutCrossRef)

    @Update
    suspend fun updateSequenceCrossRef(sequenceSongRef: SequenceWorkoutCrossRef)

    @Delete
    suspend fun deleteSequence(sequence: SequenceOfWorkouts)

    @Query("DELETE FROM sequenceworkoutcrossref WHERE fk_sequence_id=:sequenceId")
    suspend fun deleteSequenceCrossRefs(sequenceId: Int)

    @Delete
    suspend fun deleteSequenceCrossRefWithWorkout(sequenceSongRef: SequenceWorkoutCrossRef)

    @Transaction
    suspend fun deleteSequenceWithRefs(sequence: SequenceOfWorkouts)
    {
        deleteSequenceCrossRefs(sequence.sequenceId!!)
        deleteSequence(sequence)
    }

    @Transaction
    @Query("SELECT * FROM SequenceOfWorkouts")
    fun getSequencesWithWorkouts(): LiveData<List<SequenceWithWorkouts>>


}