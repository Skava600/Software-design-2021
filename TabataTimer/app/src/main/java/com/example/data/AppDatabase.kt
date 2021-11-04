package com.example.data

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Workout::class, Interval::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    /**
     * Singleton pattern to avoid building the database multiple times
     */
    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
            }

            return instance as AppDatabase
        }
    }
}