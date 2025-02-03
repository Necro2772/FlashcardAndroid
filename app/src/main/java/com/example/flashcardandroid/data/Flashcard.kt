package com.example.flashcardandroid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "front_text") val frontText: String,
    @ColumnInfo(name = "back_text") val backText: String
)