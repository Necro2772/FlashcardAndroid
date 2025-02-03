package com.example.flashcardandroid

import android.app.Application
import com.example.flashcardandroid.data.AppContainer
import com.example.flashcardandroid.data.AppDataContainer

class FlashcardApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}