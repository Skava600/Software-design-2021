package com.example.keepnotes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
class Note (
    @PrimaryKey(autoGenerate = true) val noteId: Int?,
    var title: String,
    var body: String,
    var tagList: String,
    var dateTime: String
): Serializable
