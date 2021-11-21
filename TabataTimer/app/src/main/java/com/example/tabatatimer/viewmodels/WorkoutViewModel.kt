package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.database.SequenceRepository
import com.example.tabatatimer.data.Workout
import com.example.database.WorkoutRepository
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(app: Application): AndroidViewModel(app) {
    private var allWorkouts: LiveData<List<Workout>>
    private var workoutRepository: WorkoutRepository
    private var sequenceRepository: SequenceRepository

    init {
        val db = AppDatabase.getInstance(app)

        val workoutDao = db.workoutDao()
        val sequenceWorkoutDao = db.sequenceDao()

        workoutRepository = WorkoutRepository(workoutDao)
        sequenceRepository = SequenceRepository(sequenceWorkoutDao)

        allWorkouts = workoutDao.getAll()
    }

    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutRepository.insert(workout)
        }
    }

    fun insertSequence(sequence: SequenceOfWorkouts) {
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.insertSequence(sequence)
        }
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

    fun deleteSequenceRef(sequenceId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.deleteSequenceRef(sequenceId)
        }
    }

    fun deleteAllWorkouts(){
        viewModelScope.launch(Dispatchers.IO){
            workoutRepository.deleteAllWorkouts()
        }
    }

    fun deleteAllSequences(){
        viewModelScope.launch(Dispatchers.IO) {
            sequenceRepository.deleteAllSequences()
        }
    }

    fun getAllWorkouts(): LiveData<List<Workout>> = this.allWorkouts


}