package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.DailyTaskDao
import com.bear.englishlearning.data.local.entity.DailyTask
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyTaskRepository @Inject constructor(
    private val dailyTaskDao: DailyTaskDao
) {
    suspend fun getTaskByDate(date: String): DailyTask? = dailyTaskDao.getTaskByDate(date)

    suspend fun insertTask(task: DailyTask): Long = dailyTaskDao.insertTask(task)

    suspend fun completeTask(taskId: Long) = dailyTaskDao.completeTask(taskId)

    suspend fun getCompletedTaskCount(): Int = dailyTaskDao.getCompletedTaskCount()
}
