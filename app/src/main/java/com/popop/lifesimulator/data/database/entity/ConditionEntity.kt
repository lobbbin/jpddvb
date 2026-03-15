package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Condition entity for state-based triggers and status effects.
 * Represents ongoing conditions affecting entities.
 */
@Entity(
    tableName = "conditions",
    indices = [Index(value = ["targetId"]), Index(value = ["conditionType"])]
)
data class ConditionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Condition identification
    val conditionId: String, // Reference to condition template
    val name: String,
    val description: String = "",
    val conditionType: String = "status", // status, disease, injury, blessing, curse
    
    // Target
    val targetId: String, // Player ID or NPC ID
    val targetType: String = "player",
    
    // Severity and progression
    val severity: Int = 50, // 0-100
    val maxSeverity: Int = 100,
    val progression: Float = 0.0f, // -1.0 (healing) to 1.0 (worsening)
    
    // Effects (stored as JSON)
    val effects: String = "", // JSON of stat changes and effects
    
    // Duration
    val duration: Long? = null, // Null = permanent until cured
    val remainingTime: Long? = null,
    val expiresAt: Long? = null,
    
    // Triggers
    val triggerConditions: String = "", // JSON of conditions that trigger this
    val removalConditions: String = "", // JSON of conditions that remove this
    
    // Treatment
    val cureMethod: String = "", // How to remove this condition
    val cureItemId: String? = null, // Item that cures this
    val cureSkillRequired: Int = 0,
    
    // Properties
    val isContagious: Boolean = false,
    val isIncurable: Boolean = false,
    val isFatal: Boolean = false,
    val fatalityThreshold: Int = 100, // Severity at which this becomes fatal
    val isVisible: Boolean = true,
    
    // Timestamps
    val appliedAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
