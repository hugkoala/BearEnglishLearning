package com.bear.englishlearning

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bear.englishlearning.worker.DailyReviewReminderWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BearApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduleDailyReminder()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "每日複習提醒",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "每天提醒你複習昨天的學習筆記"
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleDailyReminder() {
        val now = LocalDateTime.now()
        val target = now.toLocalDate().atTime(LocalTime.of(9, 0))
        val targetTime = if (now.isAfter(target)) {
            target.plusDays(1)
        } else {
            target
        }
        val initialDelay = Duration.between(now, targetTime).toMillis()

        val dailyRequest = PeriodicWorkRequestBuilder<DailyReviewReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }

    companion object {
        const val CHANNEL_ID = "daily_review_channel"
        const val WORK_TAG = "daily_review_reminder"
    }
}
