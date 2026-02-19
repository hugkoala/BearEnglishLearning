package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true) val memoId: Long = 0,
    val content: String,
    val voiceTranscription: String? = null,
    val relatedScenarioTitle: String? = null,
    val createdAt: Long,
    val nextReviewAt: Long,
    val isReviewed: Boolean = false
)
