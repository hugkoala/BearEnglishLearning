package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bear.englishlearning.data.local.entity.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: Memo): Long

    @Update
    suspend fun updateMemo(memo: Memo)

    @Query("SELECT * FROM memos ORDER BY createdAt DESC")
    fun getAllMemos(): Flow<List<Memo>>

    @Query("SELECT * FROM memos WHERE nextReviewAt <= :now AND isReviewed = 0 ORDER BY createdAt DESC")
    suspend fun getMemosToReview(now: Long): List<Memo>

    @Query("SELECT * FROM memos WHERE nextReviewAt <= :now AND isReviewed = 0 ORDER BY createdAt DESC")
    fun getMemosToReviewFlow(now: Long): Flow<List<Memo>>

    @Query("UPDATE memos SET isReviewed = 1 WHERE memoId = :memoId")
    suspend fun markAsReviewed(memoId: Long)

    @Query("SELECT * FROM memos WHERE memoId = :id")
    suspend fun getMemoById(id: Long): Memo?

    @Query("DELETE FROM memos WHERE memoId = :memoId")
    suspend fun deleteMemo(memoId: Long)
}
