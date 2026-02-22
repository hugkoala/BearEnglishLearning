package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_progress",
    indices = [Index("date", unique = true)]
)
data class DailyProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,                    // "yyyy-MM-dd"
    val sentencesPracticed: Int = 0,     // 練習句子數
    val vocabularyLearned: Int = 0,      // 學習單字數
    val conversationTurns: Int = 0,      // 對話回合數
    val listeningQuizzes: Int = 0,       // 聽力測驗數
    val memosCreated: Int = 0,           // 建立的備忘錄數
    val translationsCount: Int = 0,      // 翻譯次數
    val studyMinutes: Int = 0,           // 學習分鐘數
    val timestamp: Long = System.currentTimeMillis()
)
