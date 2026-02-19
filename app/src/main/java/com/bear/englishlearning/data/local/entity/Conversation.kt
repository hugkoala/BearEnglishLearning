package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey val conversationId: Long,
    val title: String,
    val titleZh: String,
    val scenarioTag: String
)
