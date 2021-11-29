package com.example.tabatatimer.data

import androidx.room.*

@Entity
data class SequenceOfWorkouts(
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name = "sequence_id")val sequenceId: Int?,
    var color: Int
)

@Entity(primaryKeys = ["fk_sequence_id", "fk_workout_id"],
    foreignKeys = [
        ForeignKey(
            entity = SequenceOfWorkouts::class,
            childColumns = arrayOf("fk_sequence_id"),
            parentColumns = arrayOf("sequence_id")
        ), ForeignKey(
            entity = Workout::class,
            childColumns = arrayOf("fk_workout_id"),
            parentColumns = arrayOf("workoutId")
        )
    ])
data class SequenceWorkoutCrossRef(
    @ColumnInfo(name = "fk_sequence_id")val sequenceId: Int,
    @ColumnInfo(name = "fk_workout_id")val workoutId: Int
)

data class SequenceWithWorkouts(
    @Embedded val sequenceOfWorkouts: SequenceOfWorkouts,
    @Relation(
        parentColumn = "sequence_id",
        entityColumn = "workoutId",
        entity = Workout::class,
        associateBy = Junction(SequenceWorkoutCrossRef::class,
            parentColumn = "fk_sequence_id",
            entityColumn = "fk_workout_id")
    )
    val workouts: List<Workout>
)
