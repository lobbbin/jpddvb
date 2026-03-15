package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * NPC goal entity for tracking NPC ambitions and objectives.
 * Drives NPC behavior and decision-making.
 */
@Entity(
    tableName = "npc_goals",
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
data class NpcGoalEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Goal identification
    val goalId: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val goalType: String = "personal", // personal, career, social, revenge, romantic
    
    // Goal owner
    val npcId: String,
    
    // Goal state
    val status: String = "active", // active, completed, abandoned, failed
    val priority: Int = 50, // 0-100
    val urgency: Int = 50, // 0-100
    
    // Progress
    val progress: Int = 0,
    val maxProgress: Int = 100,
    val steps: String = "", // JSON array of goal steps
    
    // Requirements and obstacles
    val requirements: String = "", // JSON of requirements
    val obstacles: String = "", // JSON of obstacles
    val requiredResources: String = "", // JSON of needed resources
    
    // Relationships to goal
    val alliedNpcIds: String = "", // NPCs who help with this goal
    val rivalNpcIds: String = "", // NPCs who oppose this goal
    val relatedFactionId: String? = null,
    val relatedQuestId: String? = null,
    
    // Rewards and consequences
    val rewardDescription: String = "",
    val failureConsequences: String = "",
    
    // Time constraints
    val deadline: Long? = null, // Null = no deadline
    val timeCreated: Long = System.currentTimeMillis(),
    
    // AI behavior
    val actionPlan: String = "", // JSON of planned actions
    val currentStep: Int = 0,
    val lastActionTime: Long = System.currentTimeMillis(),
    
    // Properties
    val isSecret: Boolean = false,
    val isObsessive: Boolean = false, // NPC will prioritize this above all
    val canBeShared: Boolean = true,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
