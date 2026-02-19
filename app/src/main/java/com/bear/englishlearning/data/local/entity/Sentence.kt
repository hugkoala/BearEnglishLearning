package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sentences",
    foreignKeys = [ForeignKey(
        entity = Scenario::class,
        parentColumns = ["scenarioId"],
        childColumns = ["scenarioId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("scenarioId")]
)
data class Sentence(
    @PrimaryKey val sentenceId: Long,
    val scenarioId: Long,
    val englishText: String,
    val chineseText: String,
    val pronunciationTip: String,
    val orderIndex: Int
)
