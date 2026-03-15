package com.popop.lifesimulator.core.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.popop.lifesimulator.R
import com.popop.lifesimulator.ui.MainActivity

/**
 * Notification manager for game events
 */
class GameNotificationManager(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID_EVENTS = "game_events_channel"
        private const val CHANNEL_ID_REMINDERS = "game_reminders_channel"
        private const val CHANNEL_ID_BACKGROUND = "game_background_channel"
        
        private const val NOTIFICATION_ID_EVENT = 1001
        private const val NOTIFICATION_ID_REMINDER = 1002
        private const val NOTIFICATION_ID_BACKGROUND_EVENT = 1003
    }
    
    /**
     * Create notification channels (required for Android O+)
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val eventChannel = NotificationChannel(
                CHANNEL_ID_EVENTS,
                "Game Events",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important game events and choices"
                enableVibration(true)
            }
            
            val reminderChannel = NotificationChannel(
                CHANNEL_ID_REMINDERS,
                "Game Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for daily activities"
                enableVibration(false)
            }
            
            val backgroundChannel = NotificationChannel(
                CHANNEL_ID_BACKGROUND,
                "Background Events",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Events that happen while game is closed"
                enableVibration(false)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(eventChannel)
            notificationManager.createNotificationChannel(reminderChannel)
            notificationManager.createNotificationChannel(backgroundChannel)
        }
    }
    
    /**
     * Check if notification permission is granted
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true  // Not required for older versions
        }
    }
    
    /**
     * Show a game event notification
     */
    fun showEventNotification(
        title: String,
        message: String,
        characterId: Long
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("character_id", characterId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_EVENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            if (hasNotificationPermission()) {
                notify(NOTIFICATION_ID_EVENT, notification)
            }
        }
    }
    
    /**
     * Show a reminder notification
     */
    fun showReminderNotification(
        title: String,
        message: String
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            if (hasNotificationPermission()) {
                notify(NOTIFICATION_ID_REMINDER, notification)
            }
        }
    }
    
    /**
     * Show background event notification
     */
    fun showBackgroundEventNotification(
        title: String,
        message: String,
        characterId: Long
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("character_id", characterId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_BACKGROUND)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            if (hasNotificationPermission()) {
                notify(NOTIFICATION_ID_BACKGROUND_EVENT, notification)
            }
        }
    }
    
    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID_EVENT)
            cancel(NOTIFICATION_ID_REMINDER)
            cancel(NOTIFICATION_ID_BACKGROUND_EVENT)
        }
    }
    
    /**
     * Cancel a specific notification
     */
    fun cancelNotification(notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }
}

/**
 * Notification types for game events
 */
sealed class GameNotification {
    data class RandomEvent(val title: String, val description: String) : GameNotification()
    data class StatChange(val stat: String, val amount: Int) : GameNotification()
    data class RelationshipChange(val npcName: String, val change: String) : GameNotification()
    data class Achievement(val title: String, val description: String) : GameNotification()
    data class Reminder(val message: String) : GameNotification()
    data class BackgroundEvent(val title: String, val description: String) : GameNotification()
}
