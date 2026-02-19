package com.bear.englishlearning.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bear.englishlearning.BearApp
import com.bear.englishlearning.MainActivity
import com.bear.englishlearning.R
import com.bear.englishlearning.data.local.dao.MemoDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyReviewReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val memoDao: MemoDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val unreviewedMemos = memoDao.getMemosToReview(System.currentTimeMillis())
        if (unreviewedMemos.isNotEmpty()) {
            showNotification(unreviewedMemos.size)
        }
        return Result.success()
    }

    private fun showNotification(count: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "review")
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, BearApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bear)
            .setContentTitle("📖 複習時間到！")
            .setContentText("你有 $count 則昨天的學習筆記需要複習")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        }
    }
}
