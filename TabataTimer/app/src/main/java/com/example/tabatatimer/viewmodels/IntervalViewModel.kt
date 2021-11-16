package com.example.tabatatimer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.tabatatimer.data.Interval
import com.example.database.IntervalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IntervalViewModel(app: Application): AndroidViewModel(app) {
    private val repository: IntervalRepository
    init {
        val database = AppDatabase.getInstance(app)
        val intervalDao = database.intervalDao()
        repository = IntervalRepository(intervalDao)
    }



    fun insert(interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
             repository.insert(interval)
        }
    }


    fun update(interval: Interval) {
        viewModelScope.launch(Dispatchers.IO){
            repository.update(interval)
        }
    }

    fun delete(interval: Interval) {
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(interval)
        }
    }
}