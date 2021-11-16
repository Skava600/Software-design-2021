package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.tabatatimer.data.Workout
import com.example.database.WorkoutDao
import com.example.database.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(app: Application): AndroidViewModel(app) {
    private var workoutDao: WorkoutDao
    private var allWorkouts: LiveData<List<Workout>>
    private var repository: WorkoutRepository
    init{
        val db = AppDatabase.getInstance(app)
        workoutDao = db.workoutDao()
        repository = WorkoutRepository(workoutDao)
        allWorkouts = workoutDao.getAll()
    }
    fun insert(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(workout)
        }
    }


    fun update(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO){
            repository.update(workout)
        }
    }

    fun delete(workout: Workout) {
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(workout)
        }
    }

    fun deleteAllWorkouts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllWorkouts()
        }
    }

    fun getAllWorkouts(): LiveData<List<Workout>> = this.allWorkouts


}