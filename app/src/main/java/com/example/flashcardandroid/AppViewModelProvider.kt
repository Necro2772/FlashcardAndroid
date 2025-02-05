package com.example.flashcardandroid

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FlashcardInputViewModel(flashcardApplication().container.flashcardsRepository)
        }
        initializer {
            FlashcardViewModel(this.createSavedStateHandle(), flashcardApplication().container.flashcardsRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlashcardApplication].
 */
fun CreationExtras.flashcardApplication(): FlashcardApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlashcardApplication)