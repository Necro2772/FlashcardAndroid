package com.example.flashcardandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
        flashcardList = flashcardsRepository.getByIds(args).first().map {card -> card.toFlashcardDetails()}.shuffled()
    }

    fun removeCardFromDeck(flashcardDetails: FlashcardDetails) {
        flashcardList = flashcardList.minus(flashcardDetails)
    }

    fun reshuffle() {
        flashcardList = flashcardList.shuffled()
    }

    fun flip(index: Int) {
        val currentCard = flashcardList[index]
        val tmpCards = flashcardList.toMutableList()
        tmpCards[index] = FlashcardDetails(currentCard.uid, currentCard.backText, currentCard.frontText, currentCard.tags)
        flashcardList = tmpCards
    }

    fun flipAll() {
        flashcardList = flashcardList.map { card -> FlashcardDetails(uid = card.uid, frontText = card.backText, backText = card.frontText, tags = card.tags) }
    }

}
