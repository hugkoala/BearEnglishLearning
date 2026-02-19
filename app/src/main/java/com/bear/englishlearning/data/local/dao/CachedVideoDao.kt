package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.CachedVideo

@Dao
interface CachedVideoDao {

    @Query("SELECT * FROM cached_videos WHERE scenarioQuery = :query ORDER BY cachedAtMillis DESC")
    suspend fun getVideosByQuery(query: String): List<CachedVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<CachedVideo>)

    @Query("DELETE FROM cached_videos WHERE scenarioQuery = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM cached_videos WHERE cachedAtMillis < :thresholdMillis")
    suspend fun deleteOlderThan(thresholdMillis: Long)

    @Query("SELECT COUNT(*) FROM cached_videos WHERE scenarioQuery = :query")
    suspend fun countByQuery(query: String): Int
}
