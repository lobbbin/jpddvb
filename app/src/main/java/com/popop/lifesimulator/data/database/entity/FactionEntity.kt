package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Faction entity representing organizations, groups, and power structures.
 * Supports royalty, political parties, criminal organizations, and businesses.
 */
@Entity(tableName = "factions")
data class FactionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Basic info
    val name: String,
    val description: String = "",
    val factionType: String = "generic", // See FactionType sealed class
    
    // Faction status
    val isActive: Boolean = true,
    val isPlayerMember: Boolean = false,
    val playerRank: Int = 0,
    val playerJoinDate: Long? = null,
    
    // Power and influence
    val power: Int = 50, // 0-100 overall power
    val influence: Int = 50, // 0-100 political influence
    val wealth: Long = 0,
    val territory: Int = 0, // Number of controlled locations
    
    // Reputation
    val publicReputation: Int = 50, // -100 to 100
    val legality: String = "legal", // legal, tolerated, illegal, banned
    
    // Leadership
    val leaderId: String? = null, // NPC ID of leader
    val leadershipType: String = "single", // single, council, democratic
    
    // Faction stats
    val memberCount: Int = 0,
    val militaryStrength: Int = 0,
    val economicStrength: Int = 0,
    val politicalStrength: Int = 0,
    
    // Relationships with other factions (stored as JSON)
    val factionRelations: String = "", // {"factionId": opinion, ...}
    
    // Goals and agendas
    val currentGoal: String? = null,
    val longTermGoals: String = "", // JSON array
    val ideology: String = "",
    
    // Resources
    val resources: String = "", // JSON string of resource amounts
    
    // Laws and rules (for governing factions)
    val laws: String = "", // JSON array of active laws
    
    // Special properties
    val properties: String = "", // JSON for additional properties
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
