package com.popop.lifesimulator

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.popop.lifesimulator.core.background.BackgroundWorkManager
import com.popop.lifesimulator.core.notifications.GameNotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LifeSimulatorApplication : Application(), Configuration.Provider {
    
    private lateinit var backgroundWorkManager: BackgroundWorkManager
    private lateinit var notificationManager: GameNotificationManager
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        notificationManager = GameNotificationManager(this)
        backgroundWorkManager = BackgroundWorkManager(this)
        
        // Initialize notification channels
        notificationManager.createNotificationChannels()
    }
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    
    /**
     * Start background processing for a character
     */
    fun startBackgroundProcessing(characterId: Long) {
        backgroundWorkManager.scheduleEventProcessing(characterId)
        backgroundWorkManager.scheduleAutoSave(characterId)
        backgroundWorkManager.scheduleDailyReminders()
    }
    
    /**
     * Stop all background processing
     */
    fun stopBackgroundProcessing() {
        backgroundWorkManager.cancelAllWork()
    }
    
    companion object {
        lateinit var instance: LifeSimulatorApplication
            private set
    }
}
