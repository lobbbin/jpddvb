package com.popop.lifesimulator.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.popop.lifesimulator.ui.MainActivity

class GameNotificationManager(private val context: Context) {

    companion object {
        private const val CHANNEL_ID_EVENTS = "game_events"
        private const val CHANNEL_ID_REMINDERS = "reminders"
        private const val REQUEST_CODE = 1001
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val eventChannel = NotificationChannel(
                CHANNEL_ID_EVENTS,
                "Game Events",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for game events"
            }

            val reminderChannel = NotificationChannel(
                CHANNEL_ID_REMINDERS,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Daily game reminders"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(eventChannel)
            manager.createNotificationChannel(reminderChannel)
        }
    }

    fun showNotification(title: String, message: String, priority: Boolean = false) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_EVENTS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (priority) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
