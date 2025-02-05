package com.example.flashcardandroid.data

import kotlinx.coroutines.flow.Flow

interface FlashcardsRepository {
    fun getAll(): Flow<List<Flashcard>>
    fun getByText(text: String): Flow<List<Flashcard>>
    //fun getByTag(include: List<String>, exclude: List<String>): Flow<List<Flashcard>>
    suspend fun getLast(): Flow<Flashcard>
    suspend fun getByIds(cards: List<Int>): Flow<List<Flashcard>>
    suspend fun insert(card: Flashcard)
    suspend fun delete(card: Flashcard)
    suspend fun update(card: Flashcard)
}