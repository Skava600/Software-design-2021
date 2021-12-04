package com.example.keepnotes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.keepnotes.models.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    suspend fun insert(note:Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}