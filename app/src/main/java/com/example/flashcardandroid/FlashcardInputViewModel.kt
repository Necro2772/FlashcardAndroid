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

    fun updateUiState(flashcardDetails: FlashcardDetails) {
        flashcardUiState = FlashcardUiState(flashcardDetails)
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
        flashcardUiState = FlashcardUiState()
    }

    suspend fun deleteCard() {
        flashcardsRepository.delete(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.minus(flashcardUiState.flashcardDetails)
        flashcardUiState = FlashcardUiState()
    }

    suspend fun saveCard() {
        flashcardsRepository.insert(flashcardUiState.flashcardDetails.toFlashcard())
        flashcardList = flashcardList.plus(flashcardsRepository.getLast().first().toFlashcardDetails())
        flashcardUiState = FlashcardUiState()
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
): Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashcardDetails> {
        override fun createFromParcel(parcel: Parcel): FlashcardDetails {
            return FlashcardDetails(parcel.readInt())
        }

        override fun newArray(size: Int): Array<FlashcardDetails?> {
            return arrayOfNulls(size)
        }
    }

}

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