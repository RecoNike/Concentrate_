package com.recon.concentrate.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.recon.concentrate.R

class TimerNotificationManager(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "timer_channel"
        private const val NOTIFICATION_ID = 1
    }

    private var notificationPermissionRequested = false

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for timer notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        return notificationManagerCompat.areNotificationsEnabled()
    }

    private fun buildNotification(timeRemaining: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Timer is running")
            .setContentText("Time remaining: $timeRemaining")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(null)  // Отключаем вибрацию
    }

    fun showTimerNotification(timeRemaining: String) {
        if (areNotificationsEnabled()) {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, buildNotification(timeRemaining).build())
            }
        } else {
            requestNotificationPermission()
        }
    }

    fun updateTimerNotification(timeRemaining: String) {
        if (areNotificationsEnabled()) {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, buildNotification(timeRemaining).build())
            }
        } else {
            // Можно также попробовать обновить уведомление без запроса разрешения
            // или запросить разрешение и обновить уведомление после получения разрешения
            requestNotificationPermission()
        }
    }

    fun cancelTimerNotification() {
        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID)
        }
    }

    private fun requestNotificationPermission() {
        if (!notificationPermissionRequested) {
            notificationPermissionRequested = true
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            context.startActivity(intent)
        }
    }
}
