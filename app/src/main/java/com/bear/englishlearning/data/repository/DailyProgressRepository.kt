package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.DailyProgressDao
import com.bear.englishlearning.data.local.entity.DailyProgress
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyProgressRepository @Inject constructor(
    private val dao: DailyProgressDao
) {
    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE  // yyyy-MM-dd

    fun today(): String = LocalDate.now().format(dateFormat)

    suspend fun getProgressByDate(date: String): DailyProgress? = dao.getProgressByDate(date)

    suspend fun getProgressRange(startDate: String, endDate: String): List<DailyProgress> =
        dao.getProgressRange(startDate, endDate)

    fun getProgressRangeFlow(startDate: String, endDate: String): Flow<List<DailyProgress>> =
        dao.getProgressRangeFlow(startDate, endDate)

    suspend fun ensureTodayExists() {
        val date = today()
        if (dao.getProgressByDate(date) == null) {
            dao.upsertProgress(DailyProgress(date = date))
        }
    }

    suspend fun recordSentencePractice(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementSentences(date, count)
    }

    suspend fun recordVocabularyLearned(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementVocabulary(date, count)
    }

    suspend fun recordConversationTurn(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementConversation(date, count)
    }

    suspend fun recordListeningQuiz(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementListening(date, count)
    }

    suspend fun recordMemoCreated(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementMemos(date, count)
    }

    suspend fun recordTranslation(count: Int = 1) {
        val date = today()
        ensureTodayExists()
        dao.incrementTranslations(date, count)
    }

    suspend fun recordStudyMinutes(minutes: Int) {
        val date = today()
        ensureTodayExists()
        dao.incrementStudyMinutes(date, minutes)
    }

    suspend fun getTotalActiveDays(): Int = dao.getTotalActiveDays()

    suspend fun getAllProgress(): List<DailyProgress> = dao.getAllProgress()
}
