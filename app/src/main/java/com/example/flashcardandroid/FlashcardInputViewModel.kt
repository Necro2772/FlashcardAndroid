package com.example.flashcardandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.flashcardandroid.data.Flashcard
import com.example.flashcardandroid.data.FlashcardTag
import com.example.flashcardandroid.data.FlashcardsRepository
import kotlinx.coroutines.flow.first

const val disabledTag = "disabled"
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

    fun getCardDeck(): List<Int> {
        return flashcardList.filter { card -> card.isEnabled() }.map { card -> card.uid }
    }

    suspend fun updateCard(flashcardDetails: FlashcardDetails = flashcardUiState.flashcardDetails) {
        flashcardsRepository.update(flashcardDetails.toFlashcard())
        val index = flashcardList.binarySearch { card -> card.uid - flashcardDetails.uid }
        val tmp = flashcardList.toMutableList()
        tmp[index] = flashcardDetails
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
        flashcardList = flashcardsRepository.getAll().first().map { pair ->
            FlashcardDetails(
                uid = pair.key.uid,
                frontText = pair.key.frontText,
                backText = pair.key.backText,
                tags = pair.value.map { flashcardTag -> flashcardTag.tag }
                ) }
    }

    suspend fun onToggleEnabled(flashcardDetails: FlashcardDetails) {
        var cardTags = flashcardDetails.tags
        if (cardTags.contains(disabledTag)) {
            flashcardsRepository.deleteTag(flashcardDetails.uid, disabledTag)
            cardTags = cardTags.minus(disabledTag)
        } else {
            flashcardsRepository.insert(FlashcardTag(cardId = flashcardDetails.uid, tag = disabledTag))
            cardTags = cardTags.plus(disabledTag)
        }
        val index = flashcardList.binarySearch { card -> card.uid - flashcardDetails.uid }
        val updatedList = flashcardList.toMutableList()
        updatedList[index] = FlashcardDetails(
            flashcardDetails.uid,
            flashcardDetails.frontText,
            flashcardDetails.backText,
            cardTags
        )
        flashcardList = updatedList
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

fun FlashcardDetails.isEnabled(): Boolean = !this.tags.contains(disabledTag)

fun Flashcard.toFlashcardDetails(): FlashcardDetails = FlashcardDetails(
    uid = uid,
    frontText = frontText,
    backText = backText
)