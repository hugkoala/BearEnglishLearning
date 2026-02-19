package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.DailyTask

@Dao
interface DailyTaskDao {

    @Query("SELECT * FROM daily_tasks WHERE date = :date LIMIT 1")
    suspend fun getTaskByDate(date: String): DailyTask?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DailyTask): Long

    @Query("UPDATE daily_tasks SET isCompleted = 1, completedAt = :completedAt WHERE taskId = :taskId")
    suspend fun completeTask(taskId: Long, completedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM daily_tasks ORDER BY date DESC")
    suspend fun getAllTasks(): List<DailyTask>

    @Query("SELECT COUNT(*) FROM daily_tasks WHERE isCompleted = 1")
    suspend fun getCompletedTaskCount(): Int
}
