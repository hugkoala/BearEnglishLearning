package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.DailyProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyProgressDao {

    @Query("SELECT * FROM daily_progress WHERE date = :date LIMIT 1")
    suspend fun getProgressByDate(date: String): DailyProgress?

    @Query("SELECT * FROM daily_progress WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getProgressRange(startDate: String, endDate: String): List<DailyProgress>

    @Query("SELECT * FROM daily_progress WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getProgressRangeFlow(startDate: String, endDate: String): Flow<List<DailyProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: DailyProgress): Long

    @Query("""
        UPDATE daily_progress 
        SET sentencesPracticed = sentencesPracticed + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementSentences(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET vocabularyLearned = vocabularyLearned + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementVocabulary(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET conversationTurns = conversationTurns + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementConversation(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET listeningQuizzes = listeningQuizzes + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementListening(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET memosCreated = memosCreated + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementMemos(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET translationsCount = translationsCount + :count, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementTranslations(date: String, count: Int = 1, ts: Long = System.currentTimeMillis())

    @Query("""
        UPDATE daily_progress 
        SET studyMinutes = studyMinutes + :minutes, timestamp = :ts 
        WHERE date = :date
    """)
    suspend fun incrementStudyMinutes(date: String, minutes: Int, ts: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM daily_progress WHERE sentencesPracticed > 0 OR conversationTurns > 0 OR vocabularyLearned > 0")
    suspend fun getTotalActiveDays(): Int

    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 1")
    suspend fun getLatestProgress(): DailyProgress?

    @Query("SELECT * FROM daily_progress ORDER BY date DESC")
    suspend fun getAllProgress(): List<DailyProgress>
}
