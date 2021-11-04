package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(foreignKeys = [
    ForeignKey(entity = Workout::class,
        parentColumns = ["workoutId"],
        childColumns = ["intervalId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)])
data class Interval(
    @PrimaryKey(autoGenerate=true) val intervalId: Int?,
    @ColumnInfo(name="i_name") val intervalName: String,
    @ColumnInfo(name="i_type") val intervalType: String,
    @ColumnInfo(name="i_time") val intervalTime: Int?,
    @ColumnInfo(name="i_reps") val intervalReps: Int?,
    val workoutId: Int
) : Serializable {
    /**
     * Enum representing the three types of intervals
     */
    enum class IntervalType(val value: String)
    {
        Prepare("Prepare"),
        REST("REST"),
        Work("Work")
    }
}