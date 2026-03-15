package com.popop.lifesimulator.core.background

import android.content.Context
import androidx.work.*
import com.popop.lifesimulator.core.notifications.GameNotificationManager
import java.util.concurrent.TimeUnit

/**
 * Background worker for processing game events
 */
class GameEventWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val characterId = inputData.getLong("character_id", 0L)
            
            // Show a simple notification
            val notificationManager = GameNotificationManager(context)
            notificationManager.showNotification(
                title = "Life Simulator",
                message = "Welcome to your new life!",
                priority = true
            )
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
