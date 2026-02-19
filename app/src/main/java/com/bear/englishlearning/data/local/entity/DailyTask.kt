package com.bear.englishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tasks")
data class DailyTask(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    val date: String,
    val scenarioId: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
