package com.example.flashcardandroid.data

import kotlinx.coroutines.flow.Flow

class OfflineFlashcardsRepository(private val flashcardDao: FlashcardDao): FlashcardsRepository {
    override fun getAll(): Flow<Map<Flashcard, List<FlashcardTag>>> = flashcardDao.getAll()

    override fun getByText(text: String): Flow<List<Flashcard>> = flashcardDao.getByText(text)

    override suspend fun getLast(): Flow<Flashcard> = flashcardDao.getLast()

    //override fun getByTag(include: List<String>, exclude: List<String>): Flow<List<Flashcard>> = flashcardDao.getByTag(include, exclude)

    override suspend fun getByIds(cards: List<Int>) = flashcardDao.getByIds(cards)

    override suspend fun insert(card: Flashcard) = flashcardDao.insert(card)

    override suspend fun insert(flashcardTag: FlashcardTag) = flashcardDao.insert(flashcardTag)

    override suspend fun delete(card: Flashcard) = flashcardDao.delete(card)

    override suspend fun deleteTag(cardId: Int, tag: String) = flashcardDao.deleteTag(cardId, tag)

    override suspend fun update(card: Flashcard) = flashcardDao.update(card)
}