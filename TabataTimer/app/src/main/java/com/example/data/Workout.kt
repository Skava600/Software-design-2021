package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Workout(
    @PrimaryKey(autoGenerate=true) val workoutId: Int?,
    @ColumnInfo(name="w_name") val name: String,
    @ColumnInfo(name="w_length") val length: Double
): Serializable