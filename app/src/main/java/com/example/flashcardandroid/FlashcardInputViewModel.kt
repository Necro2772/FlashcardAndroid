package com.example.flashcardandroid

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
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

    var isCardValid by mutableStateOf(false)

    fun updateUiState(flashcardDetails: FlashcardDetails) {
        flashcardUiState = FlashcardUiState(flashcardDetails)
        isCardValid = flashcardDetails.frontText.isNotEmpty() && flashcardDetails.backText.isNotEmpty()
    }

    fun getShuffledCards(): List<Int> {
        return flashcardList.map { card -> card.uid }
    }

    suspend fun updateCard() {
        flashcardsRepository.update(flashcardUiState.flashcardDetails.toFlashcard())
        val index = flashcardList.binarySearch { card -> card.uid - flashcardUiState.flashcardDetails.uid }
        val tmp = flashcardList.toMutableList()
        tmp[index] = flashcardUiState.flashcardDetails
        flashcardList = tmp
        updateUiState(FlashcardDetails())
    }

    suspend fun deleteCard() {
        flashcardsRepository.delete(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.minus(flashcardUiState.flashcardDetails)
        updateUiState(FlashcardDetails())
    }

    suspend fun saveCard() {
        flashcardsRepository.insert(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.plus(flashcardsRepository.getLast().first().toFlashcardDetails())
        updateUiState(FlashcardDetails())
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