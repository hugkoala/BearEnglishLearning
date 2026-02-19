package com.bear.englishlearning.data.repository

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
        private const val CACHE_DURATION_MS = 7 * 24 * 60 * 60 * 1000L // 7 days
    }

    suspend fun searchVideos(
        query: String,
        maxResults: Int = 5,
        forceRefresh: Boolean = false
    ): Resource<List<VideoResult>> = withContext(Dispatchers.IO) {
        try {
            val searchQuery = buildSearchQuery(query)

            // Check cache first
            if (!forceRefresh) {
                val cached = cachedVideoDao.getVideosByQuery(searchQuery)
                if (cached.isNotEmpty()) {
                    val oldestCache = cached.minOf { it.cachedAtMillis }
                    if (System.currentTimeMillis() - oldestCache < CACHE_DURATION_MS) {
                        return@withContext Resource.Success(cached.map { it.toDomainModel() })
                    }
                }
            }

            // Call YouTube API
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

            // Cache results
            val entities = videos.map { it.toCachedEntity(searchQuery) }
            cachedVideoDao.deleteByQuery(searchQuery)
            cachedVideoDao.insertAll(entities)
            cachedVideoDao.deleteOlderThan(System.currentTimeMillis() - CACHE_DURATION_MS)

            Resource.Success(videos)
        } catch (e: Exception) {
            // Fallback to cache on error
            val cached = cachedVideoDao.getVideosByQuery(buildSearchQuery(query))
            if (cached.isNotEmpty()) {
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
