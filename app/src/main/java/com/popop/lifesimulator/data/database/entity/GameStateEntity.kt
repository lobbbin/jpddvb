package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Game state entity for saving and loading game progress.
 * Contains the complete game state snapshot.
 */
@Entity(tableName = "game_states")
data class GameStateEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Save info
    val saveName: String = "Save Game",
    val saveSlot: Int = 1,
    
    // Game time
    val gameDate: Long = 0, // Unix timestamp of in-game date
    val gameDay: Int = 1,
    val gameMonth: Int = 1,
    val gameYear: Int = 1,
    val gameHour: Int = 8,
    val totalPlayTime: Long = 0, // Total milliseconds played
    
    // Player reference
    val playerId: String,
    
    // World state summary
    val worldState: String = "", // JSON of world state
    
    // Active quests
    val activeQuestIds: String = "", // JSON array
    
    // Completed quests
    val completedQuestIds: String = "", // JSON array
    
    // Flags and variables
    val flags: String = "", // JSON of game flags
    
    // Statistics
    val playStats: String = "", // JSON of gameplay statistics
    
    // Version info
    val gameVersion: String = "0.1.0",
    val saveVersion: Int = 1,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long = System.currentTimeMillis()
)
