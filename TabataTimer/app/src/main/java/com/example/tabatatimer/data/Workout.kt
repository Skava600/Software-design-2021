package com.example.tabatatimer.data

import androidx.room.*
import java.io.Serializable

@Entity
data class Workout(
    @PrimaryKey(autoGenerate=true) val workoutId: Int?,
    @ColumnInfo(name="w_name") var name: String,
    @ColumnInfo(name="w_length") var length: Int,
    @ColumnInfo(name="w_color") var color: Int
): Serializable

data class WorkoutWithIntervals(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val intervals: List<Interval>
)