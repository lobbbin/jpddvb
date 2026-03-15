package com.popop.lifesimulator.data.models.inventory

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Inventory item entity for Room database
 */
@Entity(tableName = "inventoryitems")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val characterId: Long,
    val itemId: String,
    val name: String,
    val description: String = "",
    val category: ItemCategory,
    val quantity: Int = 1,
    val value: Double = 0.0,
    val weight: Double = 0.0,
    val condition: ItemCondition = ItemCondition.GOOD,
    val isEquipped: Boolean = false,
    val isFavorite: Boolean = false,
    val acquiredAt: Long = System.currentTimeMillis(),
    val metadata: String = ""  // JSON for additional properties
)
