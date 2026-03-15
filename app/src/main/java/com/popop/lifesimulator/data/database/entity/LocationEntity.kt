package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Location entity representing places in the game world.
 * Supports various location types from palaces to prisons.
 */
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Basic info
    val name: String,
    val description: String = "",
    val locationType: String = "generic", // See LocationType sealed class
    
    // Geographic info
    val regionId: String? = null,
    val provinceId: String? = null,
    val coordinates: String = "0,0", // X,Y coordinates on world map
    val terrainType: String = "plain",
    
    // Location properties
    val isAccessible: Boolean = true,
    val isPublic: Boolean = true,
    val isSafeZone: Boolean = false,
    val isInstanced: Boolean = false, // Unique instance per player
    
    // Access requirements
    val requiredFactionId: String? = null,
    val requiredFactionRank: Int = 0,
    val requiredWealth: Long = 0,
    val requiredReputation: Int = 0,
    val entryFee: Long = 0,
    
    // Services available (comma-separated)
    val services: String = "", // "healing,trading,banking,training"
    
    // NPCs at this location (comma-separated IDs)
    val npcIds: String = "",
    
    // Connected locations (for navigation)
    val connectedLocationIds: String = "",
    
    // Economic properties
    val marketType: String? = null, // null, "local", "regional", "national"
    val taxRate: Float = 0.0f,
    val corruptionLevel: Int = 0, // 0-100
    
    // Law and order
    val lawEnforcementLevel: Int = 50, // 0-100
    val crimeRate: Int = 0, // 0-100
    val controlledByFactionId: String? = null,
    
    // Special properties
    val properties: String = "", // JSON string for additional properties
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
