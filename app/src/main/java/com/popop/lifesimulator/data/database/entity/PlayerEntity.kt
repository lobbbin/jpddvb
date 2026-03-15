package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Player entity representing the main character in the game.
 * Stores all player stats, attributes, and game state.
 */
@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Basic info
    val name: String,
    val age: Int = 18,
    val gender: String = "male", // male, female, non-binary
    val birthDate: Long, // Unix timestamp
    
    // Core stats (0-100 scale)
    val health: Int = 100,
    val maxHealth: Int = 100,
    val energy: Int = 100,
    val maxEnergy: Int = 100,
    val wealth: Long = 1000,
    val reputation: Int = 0, // -100 to 100
    val influence: Int = 0, // 0-100
    val happiness: Int = 50, // 0-100
    val stress: Int = 0, // 0-100
    
    // Attributes (affect skill gains and success rates)
    val intelligence: Int = 10, // 1-100
    val charisma: Int = 10,
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val wisdom: Int = 10,
    val luck: Int = 10,
    
    // Skills (0-100, can exceed with mastery)
    val combatSkill: Int = 0,
    val businessSkill: Int = 0,
    val politicsSkill: Int = 0,
    val stealthSkill: Int = 0,
    val persuasionSkill: Int = 0,
    val leadershipSkill: Int = 0,
    val craftingSkill: Int = 0,
    val knowledgeSkill: Int = 0,
    
    // Career and social
    val currentJob: String? = null,
    val careerLevel: Int = 0,
    val socialClass: String = "commoner", // commoner, merchant, noble, royalty
    val title: String? = null,
    
    // Location
    val currentLocationId: String? = null,
    val homeLocationId: String? = null,
    
    // Faction affiliations
    val factionId: String? = null,
    val factionRank: Int = 0,
    
    // Game state
    val isAlive: Boolean = true,
    val isImprisoned: Boolean = false,
    val isHospitalized: Boolean = false,
    val wantedLevel: Int = 0, // 0-100
    val bounty: Long = 0,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis(),
    val gameStartTime: Long = System.currentTimeMillis()
)
