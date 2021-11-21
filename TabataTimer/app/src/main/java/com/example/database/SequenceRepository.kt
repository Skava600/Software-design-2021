package com.example.database

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.data.Workout

class SequenceRepository(private val sequenceWorkoutDao: SequenceWorkoutDao) {

    suspend fun insertSequence(sequenceOfWorkouts: SequenceOfWorkouts) {
        sequenceWorkoutDao.insertSequence(sequenceOfWorkouts)
    }

    suspend fun insertSequenceCrossRef(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef){
        sequenceWorkoutDao.insertSequenceSongRef(sequenceWorkoutCrossRef)
    }

    suspend fun deleteSequence(sequenceOfWorkouts: SequenceOfWorkouts){
        sequenceWorkoutDao.deleteSequence(sequenceOfWorkouts)
    }

    suspend fun deleteSequenceRef(sequenceId: Int)
    {
        sequenceWorkoutDao.deleteSequenceCrossRef(sequenceId)
    }

    suspend fun deleteAllSequences()
    {
        sequenceWorkoutDao.deleteAllSequencesTrans()
    }

    fun getAllSequences(): LiveData<List<SequenceWithWorkouts>> = sequenceWorkoutDao.getSequencesWithWorkouts()
}