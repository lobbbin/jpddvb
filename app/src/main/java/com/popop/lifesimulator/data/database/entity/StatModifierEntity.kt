package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Stat modifier entity for temporary or permanent stat changes.
 * Used for buffs, debuffs, equipment bonuses, and status effects.
 */
@Entity(
    tableName = "stat_modifiers",
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["targetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["targetId"])]
)
data class StatModifierEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Modifier identification
    val modifierId: String, // Reference to modifier template
    val name: String,
    val description: String = "",
    val modifierType: String = "buff", // buff, debuff, passive, equipment
    
    // Target
    val targetId: String, // Player ID or NPC ID
    val targetType: String = "player", // "player" or "npc"
    
    // Stat changes (stored as JSON)
    val statChanges: String = "", // {"health": 10, "strength": 5, ...}
    val percentageChanges: String = "", // {"health": 0.1, ...} for 10% bonus
    
    // Duration
    val duration: Long? = null, // Duration in milliseconds, null = permanent
    val remainingTime: Long? = null,
    val expiresAt: Long? = null,
    
    // Stacking
    val maxStacks: Int = 1,
    val currentStacks: Int = 1,
    val stackType: String = "none", // none, intensity, duration
    
    // Source
    val sourceId: String? = null, // ID of what caused this (item, skill, etc.)
    val sourceType: String? = null, // "item", "skill", "spell", "condition"
    
    // Properties
    val isDispellable: Boolean = true,
    val isVisible: Boolean = true,
    val priority: Int = 0, // For conflict resolution
    
    // Timestamps
    val appliedAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
