package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Law entity for tracking active laws and regulations.
 * Used by government factions and law enforcement.
 */
@Entity(
    tableName = "laws",
    indices = [
        Index(value = ["factionId"]),
        Index(value = ["regionId"]),
        Index(value = ["lawType"])
    ]
)
data class LawEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Law identification
    val lawId: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val lawType: String = "criminal", // criminal, civil, economic, social, trade
    
    // Jurisdiction
    val factionId: String? = null, // Faction that enacted this law
    val regionId: String? = null, // Geographic region where law applies
    val locationIds: String = "", // Specific locations where law applies
    
    // Law details
    val category: String = "general",
    val severity: Int = 50, // 0-100, affects punishment
    val enforcement: Int = 50, // 0-100, how strictly enforced
    
    // Requirements and restrictions
    val requirements: String = "", // JSON of what's required
    val prohibitions: String = "", // JSON of what's forbidden
    val exemptions: String = "", // JSON of who is exempt
    
    // Penalties
    val penalties: String = "", // JSON of penalties for violation
    val fineAmount: Long = 0,
    val imprisonmentDays: Int = 0,
    val reputationLoss: Int = 0,
    
    // Enforcement
    val enforcingFactionId: String? = null,
    val enforcementChance: Float = 0.5f, // Chance of being caught
    val canBeBribed: Boolean = true,
    val bribeMultiplier: Float = 0.3f, // Bribe cost as fraction of fine
    
    // Status
    val isActive: Boolean = true,
    val isControversial: Boolean = false,
    val publicSupport: Int = 50, // 0-100
    
    // Time
    val enactedDate: Long = System.currentTimeMillis(),
    val expiryDate: Long? = null, // Null = permanent
    
    // History
    val amendmentHistory: String = "", // JSON of changes
    val violationCount: Int = 0,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
