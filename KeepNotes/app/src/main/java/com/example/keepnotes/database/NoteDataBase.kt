package com.example.keepnotes.database
import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.keepnotes.models.Note

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        fun getInstance(context: Context?): NoteDatabase {
            if (instance == null) {
                instance = context?.let {
                    Room.databaseBuilder(
                        it,
                        NoteDatabase::class.java,
                        "note_db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return  instance as NoteDatabase
        }
    }
}