package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Log entry entity for tracking player actions and game events.
 * Used for the journal, achievement tracking, and debugging.
 */
@Entity(
    tableName = "log_entries",
    indices = [Index(value = ["category"]), Index(value = ["timestamp"])]
)
data class LogEntryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Log content
    val message: String,
    val category: String = "general", // See LogCategory sealed class
    val logType: String = "info", // info, warning, error, success, failure
    
    // Context
    val playerId: String? = null,
    val locationId: String? = null,
    val npcId: String? = null,
    val factionId: String? = null,
    val questId: String? = null,
    
    // Additional data
    val data: String = "", // JSON string with additional context
    val tags: String = "", // Comma-separated tags for filtering
    
    // Importance
    val importance: Int = 1, // 1-5, affects journal visibility
    val isSaved: Boolean = true, // Whether to persist in journal
    
    // Timestamp
    val timestamp: Long = System.currentTimeMillis()
)
