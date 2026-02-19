package com.bear.englishlearning.data.repository

import android.util.Log
import com.bear.englishlearning.BuildConfig
import com.bear.englishlearning.data.local.dao.CachedVideoDao
import com.bear.englishlearning.data.local.entity.CachedVideo
import com.bear.englishlearning.data.remote.YouTubeApiService
import com.bear.englishlearning.domain.model.VideoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}

@Singleton
class YouTubeRepository @Inject constructor(
    private val apiService: YouTubeApiService,
    private val cachedVideoDao: CachedVideoDao
) {
    companion object {
        private const val TAG = "YouTubeRepo"
        private const val CACHE_DURATION_MS = 7 * 24 * 60 * 60 * 1000L // 7 days
    }

    suspend fun clearCache() = withContext(Dispatchers.IO) {
        Log.d(TAG, "Clearing all video cache")
        cachedVideoDao.deleteOlderThan(System.currentTimeMillis())
    }

    suspend fun searchVideos(
        query: String,
        maxResults: Int = 20,
        forceRefresh: Boolean = false
    ): Resource<List<VideoResult>> = withContext(Dispatchers.IO) {
        try {
            val searchQuery = buildSearchQuery(query)
            Log.d(TAG, "Searching videos for: $searchQuery (forceRefresh=$forceRefresh)")

            // Check cache first
            if (!forceRefresh) {
                val cached = cachedVideoDao.getVideosByQuery(searchQuery)
                Log.d(TAG, "Cache has ${cached.size} videos for query")
                if (cached.isNotEmpty()) {
                    val oldestCache = cached.minOf { it.cachedAtMillis }
                    val cacheAge = System.currentTimeMillis() - oldestCache
                    if (cacheAge < CACHE_DURATION_MS) {
                        Log.d(TAG, "Using cached videos (age: ${cacheAge / 1000}s)")
                        return@withContext Resource.Success(cached.map { it.toDomainModel() })
                    } else {
                        Log.d(TAG, "Cache expired, fetching fresh")
                    }
                }
            }

            // Call YouTube API
            Log.d(TAG, "Calling YouTube API...")
            val response = apiService.searchVideos(
                query = searchQuery,
                apiKey = BuildConfig.YOUTUBE_API_KEY,
                maxResults = maxResults
            )

            val videos = response.items
                .filter { it.id.videoId != null }
                .map { item ->
                    VideoResult(
                        videoId = item.id.videoId!!,
                        title = item.snippet.title,
                        description = item.snippet.description,
                        thumbnailUrl = item.snippet.thumbnails?.medium?.url
                            ?: item.snippet.thumbnails?.high?.url ?: "",
                        channelTitle = item.snippet.channelTitle
                    )
                }

            Log.d(TAG, "API returned ${videos.size} videos: ${videos.map { it.videoId }}")

            // Cache results
            val entities = videos.map { it.toCachedEntity(searchQuery) }
            cachedVideoDao.deleteByQuery(searchQuery)
            cachedVideoDao.insertAll(entities)
            cachedVideoDao.deleteOlderThan(System.currentTimeMillis() - CACHE_DURATION_MS)

            Resource.Success(videos)
        } catch (e: Exception) {
            Log.e(TAG, "YouTube API error: ${e.message}", e)
            // Fallback to cache on error
            val cached = cachedVideoDao.getVideosByQuery(buildSearchQuery(query))
            if (cached.isNotEmpty()) {
                Log.d(TAG, "Falling back to ${cached.size} cached videos")
                Resource.Success(cached.map { it.toDomainModel() })
            } else {
                Resource.Error("搜尋影片失敗: ${e.message}", e)
            }
        }
    }

    private fun buildSearchQuery(scenario: String): String {
        return if (scenario.contains("english", ignoreCase = true)) {
            scenario
        } else {
            "$scenario English conversation"
        }
    }
}

private fun CachedVideo.toDomainModel() = VideoResult(
    videoId = videoId,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    channelTitle = channelTitle
)

private fun VideoResult.toCachedEntity(query: String) = CachedVideo(
    videoId = videoId,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    channelTitle = channelTitle,
    scenarioQuery = query
)
