package com.popop.lifesimulator.domain.models

/**
 * Base interface for all game entities.
 * Provides common properties and methods for game objects.
 */
interface GameEntity {
    val id: String
    val name: String
    val isActive: Boolean
    val createdAt: Long
    val lastUpdatedAt: Long
    
    /**
     * Check if this entity is valid and can be used in game logic.
     */
    fun isValid(): Boolean = id.isNotBlank() && name.isNotBlank()
    
    /**
     * Get a summary description of this entity.
     */
    fun getSummary(): String = name
}

/**
 * Interface for entities that can have stats.
 */
interface HasStats {
    val health: Int
    val maxHealth: Int
    val isAlive: Boolean
        get() = health > 0
    
    fun healthPercentage(): Float = if (maxHealth > 0) health.toFloat() / maxHealth else 0f
}

/**
 * Interface for entities that can have relationships.
 */
interface HasRelationships {
    val id: String
    fun getRelationshipWith(otherId: String): Relationship?
}

/**
 * Interface for entities that can be located somewhere.
 */
interface HasLocation {
    val currentLocationId: String?
    val homeLocationId: String?
}

/**
 * Interface for entities that belong to factions.
 */
interface HasFaction {
    val factionId: String?
    val factionRank: Int
}

/**
 * Interface for entities that can have inventory.
 */
interface HasInventory {
    val id: String
    suspend fun getItems(): List<InventoryItem>
    suspend fun addItem(item: InventoryItem)
    suspend fun removeItem(itemId: String)
}
