package com.example.database

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWithWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef

class SequenceRepository(private val sequenceWorkoutDao: SequenceWorkoutDao) {

    suspend fun insertSequence(sequenceOfWorkouts: SequenceOfWorkouts): Long {
        return sequenceWorkoutDao.insertSequence(sequenceOfWorkouts)
    }

    suspend fun insertSequenceCrossRef(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef){
        sequenceWorkoutDao.insertSequenceSongRef(sequenceWorkoutCrossRef)
    }

    suspend fun updateSequenceCrossRef(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef)
    {
        sequenceWorkoutDao.updateSequenceCrossRef(sequenceWorkoutCrossRef)
    }

    suspend fun deleteSequence(sequenceOfWorkouts: SequenceOfWorkouts){
        sequenceWorkoutDao.deleteSequenceWithRefs(sequenceOfWorkouts)
    }

    suspend fun deleteSequenceCrossRefWithWorkout(workoutCrossRef: SequenceWorkoutCrossRef){
        sequenceWorkoutDao.deleteSequenceCrossRefWithWorkout(workoutCrossRef)
    }

    fun getAllSequences(): LiveData<List<SequenceWithWorkouts>> = sequenceWorkoutDao.getSequencesWithWorkouts()

    fun getSequenceRefsById(sequenceId: Int): LiveData<List<SequenceWorkoutCrossRef>> = sequenceWorkoutDao.getAllRefsById(sequenceId)
}