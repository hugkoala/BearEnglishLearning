package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "practice_history",
    indices = [Index("sentenceId"), Index("date")]
)
data class PracticeHistory(
    @PrimaryKey(autoGenerate = true) val historyId: Long = 0,
    val sentenceId: Long,
    val date: String,
    val userTranscription: String?,
    val accuracyScore: Float?,
    val timestamp: Long
)
