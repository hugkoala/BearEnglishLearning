package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.PracticeHistory

@Dao
interface PracticeHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: PracticeHistory): Long

    @Query("SELECT * FROM practice_history WHERE sentenceId = :sentenceId ORDER BY timestamp DESC")
    suspend fun getHistoryForSentence(sentenceId: Long): List<PracticeHistory>

    @Query("SELECT * FROM practice_history WHERE date = :date ORDER BY timestamp DESC")
    suspend fun getHistoryByDate(date: String): List<PracticeHistory>

    @Query("SELECT AVG(accuracyScore) FROM practice_history WHERE date = :date")
    suspend fun getAverageAccuracyByDate(date: String): Float?
}
