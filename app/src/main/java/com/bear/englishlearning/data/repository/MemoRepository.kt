package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.MemoDao
import com.bear.englishlearning.data.local.entity.Memo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoRepository @Inject constructor(
    private val memoDao: MemoDao
) {
    suspend fun insertMemo(memo: Memo): Long = memoDao.insertMemo(memo)

    fun getAllMemos(): Flow<List<Memo>> = memoDao.getAllMemos()

    suspend fun getMemosToReview(now: Long): List<Memo> = memoDao.getMemosToReview(now)

    fun getMemosToReviewFlow(now: Long): Flow<List<Memo>> = memoDao.getMemosToReviewFlow(now)

    suspend fun markAsReviewed(memoId: Long) = memoDao.markAsReviewed(memoId)

    suspend fun deleteMemo(memoId: Long) = memoDao.deleteMemo(memoId)
}
