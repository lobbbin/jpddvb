package com.popop.lifesimulator.core.background

import android.content.Context
import androidx.work.*
import com.popop.lifesimulator.core.notifications.GameNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Background worker for processing game events while app is closed
 */
class GameEventWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Process background events
            val characterId = inputData.getLong("character_id", 0L)
            val hoursPassed = inputData.getInt("hours_passed", 8)
            
            // Simulate time passing and generate events
            val events = processBackgroundEvents(characterId, hoursPassed)
            
            // Show notifications for important events
            val notificationManager = GameNotificationManager(context)
            events.forEach { event ->
                if (event.isImportant) {
                    notificationManager.showBackgroundEventNotification(
                        event.title,
                        event.description,
                        characterId
                    )
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun processBackgroundEvents(
        characterId: Long,
        hoursPassed: Int
    ): List<BackgroundEvent> {
        // This would integrate with the game's event system
        // For now, return sample events
        return listOf(
            BackgroundEvent(
                title = "Time Passed",
                description = "$hoursPassed hours have passed while you were away",
                isImportant = false
            )
        )
    }
}

data class BackgroundEvent(
    val title: String,
    val description: String,
    val isImportant: Boolean
)

/**
 * Manager for scheduling background work
 */
class BackgroundWorkManager(private val context: Context) {
    
    companion object {
        private const val WORK_TAG_EVENTS = "game_events"
        private const val WORK_TAG_SAVE = "game_save"
        private const val WORK_TAG_NOTIFICATIONS = "game_notifications"
    }
    
    /**
     * Schedule periodic background event processing
     */
    fun scheduleEventProcessing(characterId: Long, intervalHours: Long = 1) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<GameEventWorker>(
            intervalHours,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(
                Data.Builder()
                    .putLong("character_id", characterId)
                    .putInt("hours_passed", intervalHours.toInt())
                    .build()
            )
            .addTag(WORK_TAG_EVENTS)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_TAG_EVENTS,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    /**
     * Schedule auto-save
     */
    fun scheduleAutoSave(characterId: Long, intervalMinutes: Long = 5) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<AutoSaveWorker>(
            intervalMinutes,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInputData(
                Data.Builder()
                    .putLong("character_id", characterId)
                    .build()
            )
            .addTag(WORK_TAG_SAVE)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_TAG_SAVE,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    /**
     * Schedule daily reminder notifications
     */
    fun scheduleDailyReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            24,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS)
            .addTag(WORK_TAG_NOTIFICATIONS)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_TAG_NOTIFICATIONS,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    /**
     * Cancel all background work
     */
    fun cancelAllWork() {
        WorkManager.getInstance(context).cancelAllWork()
    }
    
    /**
     * Cancel work by tag
     */
    fun cancelWorkByTag(tag: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }
}

/**
 * Auto-save worker
 */
class AutoSaveWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val characterId = inputData.getLong("character_id", 0L)
            
            // Trigger auto-save through repository
            // This would integrate with the game's save system
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

/**
 * Reminder worker for daily notifications
 */
class ReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val notificationManager = GameNotificationManager(context)
            
            // Show daily reminder
            notificationManager.showReminderNotification(
                "Ultimate Life Simulator",
                "Check in on your character! Time waits for no one."
            )
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
