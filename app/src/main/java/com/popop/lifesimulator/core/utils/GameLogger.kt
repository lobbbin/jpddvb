package com.popop.lifesimulator.core.utils

import com.popop.lifesimulator.core.utilities.Logger
import com.popop.lifesimulator.data.database.GameLogDao
import com.popop.lifesimulator.data.models.world.LogCategory

/**
 * Game Logger wrapper for tracking player actions
 */
class GameLogger(private val logger: Logger? = null) {

    /**
     * Log an action
     */
    fun log(message: String) {
        // Simple logging without database for now
        println("[GameLog] $message")
    }

    /**
     * Log with category
     */
    fun log(category: String, message: String) {
        println("[$category] $message")
    }
}
