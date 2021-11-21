package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.data.SequenceOfWorkouts
import com.example.tabatatimer.data.SequenceWorkoutCrossRef
import com.example.tabatatimer.data.Workout

@Database(entities = [Workout::class, Interval::class, SequenceOfWorkouts::class, SequenceWorkoutCrossRef::class], version = 8, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun intervalDao(): IntervalDao
    abstract fun sequenceDao(): SequenceWorkoutDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context?): AppDatabase {
            if (instance == null) {
                instance = context?.let {
                    Room.databaseBuilder(
                        it,
                        AppDatabase::class.java,
                        "app_db"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return instance as AppDatabase
        }
    }
}