package com.example.data

import androidx.lifecycle.LiveData
import com.example.viewmodels.IntervalViewModel
import com.example.viewmodels.IntervalViewModel.AsyncIntervalInsert

class IntervalRepository(private val intervalDao: IntervalDao) {

    fun insert(vararg item: Interval) {
        AsyncIntervalInsert(intervalDao).execute(*item)
    }

    fun update(item: Interval) {
        IntervalViewModel.AsyncIntervalUpdate(intervalDao).execute(item)
    }


    fun delete(item: Interval) {
        IntervalViewModel.AsyncIntervalDelete(intervalDao).execute(item)
    }
}