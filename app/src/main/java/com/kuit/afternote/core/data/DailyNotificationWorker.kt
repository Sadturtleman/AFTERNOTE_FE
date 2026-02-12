package com.kuit.afternote.core.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kuit.afternote.R

class DailyNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        // 1. Notification Channel 생성 (Android 8.0 이상 필수)
        createNotificationChannel()

        // 2. 알림 빌드 및 발송
        showNotification()

        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification() {
        val notification = NotificationCompat.Builder(applicationContext, "DAILY_CHANNEL_ID")
            .setSmallIcon(R.drawable.img_logo) // 리소스 확인 필수
            .setContentTitle("afternote")
            .setContentText("오늘 하루 누구한테 가장 고마웠나요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("DAILY_CHANNEL_ID", name, importance)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
