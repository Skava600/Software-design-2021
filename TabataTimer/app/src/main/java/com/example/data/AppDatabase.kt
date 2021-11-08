package com.example.data

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Workout::class, Interval::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun intervalDao(): IntervalDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_db"
                ).fallbackToDestructiveMigration().build()
            }

            return instance as AppDatabase
        }
    }
}