package com.bear.englishlearning.data.remote.dto

import com.squareup.moshi.Json

data class YouTubeSearchResponse(
    @Json(name = "items") val items: List<SearchItem> = emptyList(),
    @Json(name = "nextPageToken") val nextPageToken: String? = null
)

data class SearchItem(
    @Json(name = "id") val id: VideoId,
    @Json(name = "snippet") val snippet: Snippet
)

data class VideoId(
    @Json(name = "videoId") val videoId: String? = null
)

data class Snippet(
    @Json(name = "title") val title: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "thumbnails") val thumbnails: Thumbnails? = null,
    @Json(name = "channelTitle") val channelTitle: String = ""
)

data class Thumbnails(
    @Json(name = "medium") val medium: ThumbnailDetail? = null,
    @Json(name = "high") val high: ThumbnailDetail? = null
)

data class ThumbnailDetail(
    @Json(name = "url") val url: String = "",
    @Json(name = "width") val width: Int? = null,
    @Json(name = "height") val height: Int? = null
)
