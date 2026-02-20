package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_words")
data class CustomWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val partOfSpeech: String = "",
    val phonetic: String = "",
    val meaningEn: String = "",
    val meaningZh: String = "",
    val exampleSentence: String = "",
    val exampleZh: String = "",
    val category: String = "My Words",
    val createdAt: Long = System.currentTimeMillis()
)
