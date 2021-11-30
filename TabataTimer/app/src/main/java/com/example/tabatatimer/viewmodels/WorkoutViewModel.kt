package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.database.SequenceRepository
import com.example.database.WorkoutRepository
import com.example.tabatatimer.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(app: Application): AndroidViewModel(app) {
    private var allWorkouts: LiveData<List<Workout>>
    private var allSequences: LiveData<List<SequenceWithWorkouts>>
    private var allWorkInt: LiveData<List<WorkoutWithIntervals>>
    private var workoutRepository: WorkoutRepository
    private var sequenceRepository: SequenceRepository

    init {
        val db = AppDatabase.getInstance(app)

        val workoutDao = db.workoutDao()
        val sequenceWorkoutDao = db.sequenceDao()

        workoutRepository = WorkoutRepository(workoutDao)
        sequenceRepository = SequenceRepository(sequenceWorkoutDao)

        allWorkouts = workoutRepository.getAllWorkouts()

        allWorkInt = workoutRepository.getWorkoutsWithInt()

        allSequences = sequenceRepository.getAllSequences()
    }

    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.insert(workout)
        }
    }

   fun insertSequence(sequence: SequenceOfWorkouts,  callback: (Long) -> Unit)  = viewModelScope.launch(Dispatchers.IO) {
       val id = sequenceRepository.insertSequence(sequence)
       callback.invoke(id)
   }


    fun insertSequenceCrossRef(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.insertSequenceCrossRef(sequenceWorkoutCrossRef)
        }
    }


    fun update(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.update(workout)
        }
    }

    fun updateSequenceCrossRef(workoutCrossRef: SequenceWorkoutCrossRef)
    {
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.updateSequenceCrossRef(workoutCrossRef)
        }
    }

    fun delete(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.delete(workout)
        }
    }

    fun deleteSequence(sequence: SequenceOfWorkouts) {
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.deleteSequence(sequence)
        }
    }

    fun deleteSequenceCrossRef(sequenceWorkoutCrossRef: SequenceWorkoutCrossRef){
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.deleteSequenceCrossRefWithWorkout(sequenceWorkoutCrossRef)
        }
    }

    fun wipeData(){
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.wipeData()
        }
    }

    fun getAllWorkouts(): LiveData<List<Workout>> = this.allWorkouts


    //fun getAllWorkInt(): LiveData<List<WorkoutWithIntervals>> = this.allWorkInt

    fun getAllSequences(): LiveData<List<SequenceWithWorkouts>> = this.allSequences

}