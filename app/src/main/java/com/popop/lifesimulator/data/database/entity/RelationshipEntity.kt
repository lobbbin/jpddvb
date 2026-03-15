package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Relationship entity representing the connection between two entities.
 * Typically between player and NPC, but can be between any two characters.
 */
@Entity(
    tableName = "relationships",
    foreignKeys = [
        ForeignKey(
            entity = NpcEntity::class,
            parentColumns = ["id"],
            childColumns = ["npcId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["npcId"])]
)
data class RelationshipEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Relationship participants
    val playerId: String,
    val npcId: String,
    
    // Core relationship metrics (0-100 scale)
    val trust: Int = 50, // How much they trust the player
    val fear: Int = 0, // How much they fear the player
    val respect: Int = 50, // How much they respect the player
    val loyalty: Int = 50, // How loyal they are to the player
    val affection: Int = 50, // How much they like the player
    val rivalry: Int = 0, // How much they compete with the player
    
    // Relationship state
    val debtOwed: Long = 0, // Positive = NPC owes player, Negative = player owes NPC
    val favorsOwed: Int = 0, // Positive = NPC owes favors, Negative = player owes favors
    val secretsShared: Int = 0, // Number of secrets known about each other
    val interactionsCount: Int = 0, // Total number of interactions
    
    // Relationship type
    val relationshipType: String = "acquaintance", // See RelationshipType sealed class
    val romanticInterest: Int = 0, // 0-100 romantic attraction
    val isRomanticPartner: Boolean = false,
    val isFamily: Boolean = false,
    val familyRelationType: String? = null, // "parent", "child", "sibling", etc.
    
    // History and memory
    val firstMeetingDate: Long = System.currentTimeMillis(),
    val lastInteractionDate: Long = System.currentTimeMillis(),
    val relationshipHistory: String = "", // JSON array of significant events
    
    // Dynamic factors
    val opinion: Int = 50, // Overall opinion (-100 to 100)
    val moodTowardsPlayer: String = "neutral", // Current emotional state
    val gossipAboutPlayer: String = "", // Current gossip
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
