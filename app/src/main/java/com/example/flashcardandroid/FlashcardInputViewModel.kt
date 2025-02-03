package com.example.flashcardandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.flashcardandroid.data.Flashcard
import com.example.flashcardandroid.data.FlashcardsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList

class FlashcardInputViewModel(private val flashcardsRepository: FlashcardsRepository): ViewModel() {
    var flashcardUiState by mutableStateOf(FlashcardUiState())
        private set

    var flashcardList by mutableStateOf(listOf(FlashcardDetails()))
        private set

    fun updateUiState(flashcardDetails: FlashcardDetails) {
        flashcardUiState = FlashcardUiState(flashcardDetails)
    }

    suspend fun updateCard() {
        flashcardsRepository.update(flashcardUiState.flashcardDetails.toFlashcard())
        val index = flashcardList.binarySearch { card -> card.uid - flashcardUiState.flashcardDetails.uid }
        val tmp = flashcardList.toMutableList()
        tmp[index] = flashcardUiState.flashcardDetails
        flashcardList = tmp
    }

    suspend fun deleteCard() {
        flashcardsRepository.delete(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.minus(flashcardUiState.flashcardDetails)
        flashcardUiState = FlashcardUiState()
    }

    suspend fun saveCard() {
        flashcardsRepository.insert(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.plus(flashcardsRepository.getLast().first().toFlashcardDetails())
    }

    suspend fun loadAllCards() {
        flashcardList = flashcardsRepository.getAll().first().map { card: Flashcard -> card.toFlashcardDetails() }
    }

}

data class FlashcardUiState(
    val flashcardDetails: FlashcardDetails = FlashcardDetails()
)

data class FlashcardDetails(
    val uid: Int = 0,
    val frontText: String = "",
    val backText: String = "",
    val tags: List<String> = listOf()
)

fun FlashcardDetails.toFlashcard(): Flashcard = Flashcard(
    uid = uid,
    frontText = frontText,
    backText = backText
)

fun Flashcard.toFlashcardDetails(): FlashcardDetails = FlashcardDetails(
    uid = uid,
    frontText = frontText,
    backText = backText
)