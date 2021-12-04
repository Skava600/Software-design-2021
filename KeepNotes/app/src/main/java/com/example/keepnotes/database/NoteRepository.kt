package com.example.keepnotes.database

import com.example.keepnotes.models.Note

class NoteRepository(private val noteDao: NoteDao) {

    fun getAll() = noteDao.getAllNotes()

    suspend fun insert(note: Note) = noteDao.insert(note)

    suspend fun update(note: Note) = noteDao.update(note)

    suspend fun delete(note: Note) = noteDao.delete(note)
}