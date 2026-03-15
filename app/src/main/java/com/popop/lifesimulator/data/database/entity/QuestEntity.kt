package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Quest entity representing missions, tasks, and objectives.
 */
@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Quest identification
    val questId: String, // Reference to quest template
    val name: String,
    val description: String = "",
    val questType: String = "main", // main, side, daily, repeatable
    
    // Quest giver
    val giverId: String? = null, // NPC ID who gave the quest
    val giverFactionId: String? = null,
    
    // Quest state
    val status: String = "available", // available, active, completed, failed
    val progress: Int = 0,
    val maxProgress: Int = 1,
    
    // Objectives (stored as JSON)
    val objectives: String = "", // JSON array of objectives with completion status
    
    // Rewards
    val experienceReward: Int = 0,
    val wealthReward: Long = 0,
    val reputationReward: Int = 0,
    val itemRewards: String = "", // JSON array of item rewards
    val factionReputationRewards: String = "", // JSON of faction reputation changes
    
    // Requirements
    val requiredLevel: Int = 0,
    val requiredFactionId: String? = null,
    val requiredFactionRank: Int = 0,
    val requiredQuestIds: String = "", // Prerequisite quests
    val requiredItems: String = "", // JSON of required items
    
    // Time constraints
    val timeLimit: Long? = null, // Null = no time limit
    val startTime: Long? = null,
    val expiryTime: Long? = null,
    
    // Difficulty and scaling
    val difficulty: String = "normal", // easy, normal, hard, very-hard
    val suggestedLevel: Int = 0,
    val scalesWithPlayer: Boolean = false,
    
    // Quest content
    val dialogueTree: String = "", // JSON dialogue tree
    val targetLocationIds: String = "", // Locations involved
    val targetNpcIds: String = "", // NPCs involved
    
    // Special properties
    val isRepeatable: Boolean = false,
    val repeatCooldown: Long = 0,
    val completionCount: Int = 0,
    val properties: String = "", // JSON for additional properties
    
    // Timestamps
    val acceptedAt: Long? = null,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
