package com.popop.lifesimulator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for Ultimate Life Simulator.
 * 
 * This class serves as the entry point for the application and initializes
 * Hilt dependency injection. All game systems are managed through Hilt modules.
 * 
 * @author Popop Games
 * @version 0.1.0
 */
@HiltAndroidApp
class UltimateLifeSimulatorApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        // Additional initialization can be done here
    }
    
    companion object {
        lateinit var instance: UltimateLifeSimulatorApp
            private set
    }
}
