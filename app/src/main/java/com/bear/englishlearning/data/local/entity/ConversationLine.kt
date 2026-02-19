package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "conversation_lines",
    foreignKeys = [ForeignKey(
        entity = Conversation::class,
        parentColumns = ["conversationId"],
        childColumns = ["conversationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("conversationId")]
)
data class ConversationLine(
    @PrimaryKey val lineId: Long,
    val conversationId: Long,
    val speaker: String,
    val englishText: String,
    val chineseText: String,
    val pronunciationTip: String,
    val orderIndex: Int
)
