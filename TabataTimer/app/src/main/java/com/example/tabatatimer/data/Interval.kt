package com.example.tabatatimer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Interval(
    @PrimaryKey(autoGenerate=true) val intervalId: Int?,
    @ColumnInfo(name="i_name") var name: String,
    @ColumnInfo(name="i_type") var type: String,
    @ColumnInfo(name="i_time") var time: Int?,
    @ColumnInfo(name="i_reps") var reps: Int?,
    @ColumnInfo(name="i_index") var index: Int,
    val workoutId: Int
) : Serializable {
    enum class IntervalType(val value: String)
    {
        Prepare("Prepare"),
        Rest("Rest"),
        Work("Work")
    }

    fun getIntervalDuration():String
    {
        val time = this.time;

        return when {
            time != null -> {
                val minutes: Int = time / 60
                val seconds = time % 60
                "$minutes:${if(seconds < 10) "0" else ""}$seconds"
            }
            this.reps != null -> "${this.reps} reps"
            else -> "Error"
        }
    }

    companion object{
        fun getIntervalDuration(time: Int?): String{
            if (time != null) {
                val minutes: Int = time / 60
                val seconds = time % 60
                // Add a 0 in front of the seconds if it's < 10
                // Turns this: 1:3 to this: 1:03
                return "$minutes:${if(seconds < 10) "0" else ""}$seconds"
            }
            else return  "Error"
        }
    }
}
