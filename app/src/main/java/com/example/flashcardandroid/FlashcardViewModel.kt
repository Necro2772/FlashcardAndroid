package com.example.flashcardandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.flashcardandroid.data.FlashcardsRepository
import com.example.flashcardandroid.navigation.Flashcards
import kotlinx.coroutines.flow.first

class FlashcardViewModel(savedStateHandle: SavedStateHandle, private val flashcardsRepository: FlashcardsRepository): ViewModel() {
    val args: List<Int> = savedStateHandle.toRoute<Flashcards>().flashcardList

    var flashcardList by mutableStateOf(listOf(FlashcardDetails()))

    suspend fun loadCards() {
        flashcardList = flashcardsRepository.getByIds(args).first().map {card -> card.toFlashcardDetails()}
    }

}