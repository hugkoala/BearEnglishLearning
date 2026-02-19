package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_videos")
data class CachedVideo(
    @PrimaryKey val videoId: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val channelTitle: String,
    val scenarioQuery: String,
    val cachedAtMillis: Long = System.currentTimeMillis()
)
