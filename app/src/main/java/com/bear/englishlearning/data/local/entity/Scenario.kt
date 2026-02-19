package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenarios")
data class Scenario(
    @PrimaryKey val scenarioId: Long,
    val title: String,
    val titleZh: String,
    val category: String,
    val difficulty: Int,
    val youtubeQuery: String
)
