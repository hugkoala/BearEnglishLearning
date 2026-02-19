package com.bear.englishlearning.data.remote

import com.bear.englishlearning.data.remote.dto.YouTubeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 10,
        @Query("videoDuration") videoDuration: String = "short",
        @Query("relevanceLanguage") relevanceLanguage: String = "en",
        @Query("videoCaption") videoCaption: String = "closedCaption",
        @Query("safeSearch") safeSearch: String = "strict",
        @Query("order") order: String = "relevance",
        @Query("videoEmbeddable") videoEmbeddable: String = "true",
        @Query("videoSyndicated") videoSyndicated: String = "true"
    ): YouTubeSearchResponse

    companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    }
}
