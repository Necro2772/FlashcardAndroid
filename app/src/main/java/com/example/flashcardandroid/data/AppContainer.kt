package com.example.flashcardandroid.data

import android.content.Context

interface AppContainer {
    val flashcardsRepository: FlashcardsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val flashcardsRepository: FlashcardsRepository by lazy {
        OfflineFlashcardsRepository(FlashcardDatabase.getDatabase(context).flashcardDao())
    }
}