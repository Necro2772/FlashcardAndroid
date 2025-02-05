package com.example.flashcardandroid.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards")
    fun getAll(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE front_text LIKE :text")
    fun getByText(text: String): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE uid = (SELECT max(uid) from flashcards)")
    fun getLast(): Flow<Flashcard>

//    @Query("SELECT front_text, back_text, tag FROM flashcard " +
//            "INNER JOIN flashcardtags ON flashcard.uid=flashcardtags.card_id " +
//            "WHERE tag IN (:include) " +
//            "GROUP BY flashcard.uid")
//    fun getByTag(include: List<String>, exclude: List<String>): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE uid IN (:cards)")
    fun getByIds(cards: List<Int>): Flow<List<Flashcard>>

    @Insert
    suspend fun insert(card: Flashcard)

    @Delete
    suspend fun delete(card: Flashcard)

    @Update
    suspend fun update(card: Flashcard)
}

@Database(entities = [Flashcard::class], version = 1, exportSchema = false)
abstract class FlashcardDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var Instance: FlashcardDatabase? = null

        fun getDatabase(context: Context): FlashcardDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlashcardDatabase::class.java, "flashcard_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}