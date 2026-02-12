package com.kuit.afternote.core.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
        val workManager = WorkManager.getInstance(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1) // 이미 지난 시간이라면 내일부터 시작
            }
        }

        val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
            24, TimeUnit.HOURS // 24시간 간격
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .addTag("daily_notification_work")
            .build()

        // 중복 예약을 방지하기 위해 KEEP 정책 사용
        workManager.enqueueUniquePeriodicWork(
            "daily_notification_work",
            ExistingPeriodicWorkPolicy.UPDATE, // 설정 변경 시 업데이트
            dailyWorkRequest
        )
    }
}
