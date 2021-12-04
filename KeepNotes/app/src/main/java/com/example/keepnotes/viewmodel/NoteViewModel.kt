package com.example.keepnotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.keepnotes.database.NoteDatabase
import com.example.keepnotes.database.NoteRepository
import com.example.keepnotes.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(app:Application): AndroidViewModel(app) {
    private val repository: NoteRepository
    private var allNotes: LiveData<List<Note>>
    init {
        val database = NoteDatabase.getInstance(app)
        repository = NoteRepository(database.noteDao())

        allNotes = repository.getAll()
    }

    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(note)
        }
    }

    fun update(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(note)
        }
    }

    fun delete(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    fun getAllNotes() = this.allNotes
}