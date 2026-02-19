package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.PracticeHistoryDao
import com.bear.englishlearning.data.local.entity.PracticeHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PracticeRepository @Inject constructor(
    private val practiceHistoryDao: PracticeHistoryDao
) {
    suspend fun savePractice(history: PracticeHistory): Long =
        practiceHistoryDao.insertHistory(history)

    suspend fun getHistoryForSentence(sentenceId: Long): List<PracticeHistory> =
        practiceHistoryDao.getHistoryForSentence(sentenceId)

    suspend fun getHistoryByDate(date: String): List<PracticeHistory> =
        practiceHistoryDao.getHistoryByDate(date)

    suspend fun getAverageAccuracyByDate(date: String): Float? =
        practiceHistoryDao.getAverageAccuracyByDate(date)
}
