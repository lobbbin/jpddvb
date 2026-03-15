package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * NPC (Non-Player Character) entity.
 * Represents all characters in the game world except the player.
 */
@Entity(tableName = "npcs")
data class NpcEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Basic info
    val name: String,
    val age: Int = 25,
    val gender: String = "male",
    val birthDate: Long,
    
    // Appearance and description
    val description: String = "",
    val appearance: String = "",
    
    // Core stats
    val health: Int = 100,
    val maxHealth: Int = 100,
    val wealth: Long = 0,
    val reputation: Int = 0,
    val influence: Int = 0,
    
    // Attributes
    val intelligence: Int = 10,
    val charisma: Int = 10,
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val wisdom: Int = 10,
    val luck: Int = 10,
    
    // Skills
    val combatSkill: Int = 0,
    val businessSkill: Int = 0,
    val politicsSkill: Int = 0,
    val stealthSkill: Int = 0,
    val persuasionSkill: Int = 0,
    val leadershipSkill: Int = 0,
    val craftingSkill: Int = 0,
    val knowledgeSkill: Int = 0,
    
    // Personality traits (stored as comma-separated values)
    val personalityTraits: String = "", // e.g., "ambitious,loyal,cunning"
    val moralAlignment: String = "neutral", // lawful-good, neutral, chaotic-evil, etc.
    val temperament: String = "balanced", // calm, hot-tempered, melancholic, optimistic
    
    // Social and career
    val occupation: String? = null,
    val socialClass: String = "commoner",
    val title: String? = null,
    val factionId: String? = null,
    val factionRank: Int = 0,
    
    // Location
    val currentLocationId: String? = null,
    val homeLocationId: String? = null,
    
    // Family relations (stored as comma-separated NPC IDs)
    val familyIds: String = "",
    val spouseId: String? = null,
    val parentId: String? = null,
    val childrenIds: String = "",
    
    // State
    val isAlive: Boolean = true,
    val isImprisoned: Boolean = false,
    val isHospitalized: Boolean = false,
    val wantedLevel: Int = 0,
    val bounty: Long = 0,
    val dailyRoutine: String = "", // JSON string describing daily schedule
    
    // AI behavior
    val goalId: String? = null,
    val currentActivity: String = "idle",
    val lastSeenAt: Long = System.currentTimeMillis(),
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
