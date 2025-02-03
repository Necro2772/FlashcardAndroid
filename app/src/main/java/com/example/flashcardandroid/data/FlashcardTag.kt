package com.example.flashcardandroid.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FlashcardTags")
data class FlashcardTag(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "card_id") val cardId: Int,
    @ColumnInfo(name = "tag") val tag: String
)