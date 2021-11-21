package com.example.tabatatimer.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkoutListData(
    val List: List<WorkoutWithIntervals>

) : Parcelable