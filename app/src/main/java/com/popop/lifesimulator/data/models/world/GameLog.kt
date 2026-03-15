package com.popop.lifesimulator.data.models.world

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Game log entry for tracking player actions and events
 */
@Entity(tableName = "gamelog")
data class GameLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val characterId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val category: LogCategory,
    val title: String,
    val message: String,
    val detailedMessage: String? = null,
    
    // Context
    val locationId: Long? = null,
    val npcId: Long? = null,
    val factionId: Long? = null,
    
    // Effects
    val statChanges: String = "",  // JSON of stat changes
    val itemChanges: String = "",  // JSON of item changes
    val relationshipChanges: String = "",  // JSON of relationship changes
    
    // Metadata
    val isImportant: Boolean = false,
    val isRead: Boolean = false,
    val tags: String = ""  // Comma-separated tags
)

enum class LogCategory(val displayName: String) {
    GENERAL("General"),
    STORY("Story"),
    COMBAT("Combat"),
    SOCIAL("Social"),
    BUSINESS("Business"),
    CRIME("Crime"),
    HEALTH("Health"),
    EVENT("Event"),
    ACHIEVEMENT("Achievement"),
    LOCATION("Location"),
    SKILL("Skill"),
    FINANCE("Finance")
}
