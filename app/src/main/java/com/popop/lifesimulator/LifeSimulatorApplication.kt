package com.popop.lifesimulator

import android.app.Application
import com.popop.lifesimulator.core.notifications.GameNotificationManager

class LifeSimulatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        val notificationManager = GameNotificationManager(this)
        notificationManager.createNotificationChannels()
    }

    companion object {
        lateinit var instance: LifeSimulatorApplication
            private set
    }
}
