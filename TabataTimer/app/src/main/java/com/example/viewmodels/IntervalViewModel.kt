package com.example.viewmodels

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.data.AppDatabase
import com.example.data.Interval
import com.example.data.IntervalDao
import com.example.data.IntervalRepository

class IntervalViewModel(app: Application): AndroidViewModel(app) {
    private val repository: IntervalRepository
    private val intervalDao: IntervalDao
    init {
        val database = AppDatabase.getInstance(app)
        intervalDao = database.intervalDao()
        repository = IntervalRepository(intervalDao)
    }

    fun getIntervalsByWorkout(workoutId: Int): LiveData<List<Interval>> {
        return this.intervalDao.getByWorkout(workoutId)
    }

    class AsyncIntervalInsert(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            intervalDao.insert(*interval)
        }
    }

    class AsyncIntervalUpdate(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            intervalDao.update(*interval)
        }
    }

    class AsyncIntervalDelete(private val intervalDao: IntervalDao): AsyncTask<Interval, Void, Unit>() {
        override fun doInBackground(vararg interval: Interval) {
            intervalDao.delete(interval[0])
        }
    }
}