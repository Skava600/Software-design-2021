package com.example.tabatatimer.data

import androidx.room.*

@Entity
data class SequenceOfWorkouts(
    @PrimaryKey(autoGenerate=true) val sequenceId: Int?,
    var color: Int
)

@Entity(primaryKeys = ["sequenceId", "workoutId"])
data class SequenceWorkoutCrossRef(
    val sequenceId: Int,
    val workoutId: Int
)

data class SequenceWithWorkouts(
    @Embedded val sequenceOfWorkouts: SequenceOfWorkouts,
    @Relation(
        parentColumn = "sequenceId",
        entityColumn = "workoutId",
        associateBy = Junction(SequenceWorkoutCrossRef::class)
    )
    val workouts: List<Workout>
)