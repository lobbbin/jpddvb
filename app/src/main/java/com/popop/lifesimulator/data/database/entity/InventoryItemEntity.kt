package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Inventory item entity representing items in player or NPC inventory.
 */
@Entity(
    tableName = "inventory_items",
    indices = [Index(value = ["ownerId"])]
)
data class InventoryItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Item identification
    val itemId: String, // Reference to item template
    val name: String,
    val description: String = "",
    val itemType: String = "misc", // See ItemType sealed class
    
    // Ownership
    val ownerId: String, // Player ID or NPC ID
    val ownerType: String = "player", // "player" or "npc"
    
    // Item properties
    val quantity: Int = 1,
    val maxStack: Int = 99,
    val weight: Float = 0.0f,
    val value: Long = 0,
    
    // Item stats and effects
    val durability: Int = -1, // -1 = infinite
    val maxDurability: Int = 100,
    val quality: String = "normal", // poor, normal, good, excellent, legendary
    val effects: String = "", // JSON array of effects
    
    // Equipment properties
    val isEquipped: Boolean = false,
    val equipmentSlot: String? = null, // head, chest, hands, etc.
    val statBonuses: String = "", // JSON of stat modifications
    
    // Special properties
    val isQuestItem: Boolean = false,
    val isTradeable: Boolean = true,
    val isSellable: Boolean = true,
    val isConsumable: Boolean = false,
    val properties: String = "", // JSON for additional properties
    
    // Timestamps
    val acquiredAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
