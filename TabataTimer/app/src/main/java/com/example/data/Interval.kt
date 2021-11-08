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
    @ColumnInfo(name="i_name") val name: String,
    @ColumnInfo(name="i_type") val type: String,
    @ColumnInfo(name="i_time") val time: Int?,
    @ColumnInfo(name="i_reps") val reps: Int?,
    val workoutId: Int
) : Serializable {
    enum class IntervalType(val value: String)
    {
        Prepare("Prepare"),
        REST("REST"),
        Work("Work")
    }

    fun getIntervalDuration():String{
        return if (this.time != null) {
            val minutes : Int = this.time / 60
            val seconds : Int = this.time % 60
            "$minutes${ if (seconds < 10) "0" else ""}$seconds"
        } else "Error"
    }
}