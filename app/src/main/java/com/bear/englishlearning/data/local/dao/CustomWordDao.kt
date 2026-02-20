package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.CustomWord
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomWordDao {
    @Query("SELECT * FROM custom_words ORDER BY createdAt DESC")
    fun getAllWords(): Flow<List<CustomWord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: CustomWord): Long

    @Delete
    suspend fun delete(word: CustomWord)

    @Query("DELETE FROM custom_words WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM custom_words")
    fun getWordCount(): Flow<Int>

    @Query("SELECT word FROM custom_words")
    fun getAllWordNames(): Flow<List<String>>
}
