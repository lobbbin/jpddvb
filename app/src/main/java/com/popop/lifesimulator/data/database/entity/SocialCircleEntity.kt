package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Social circle entity for tracking groups of connected NPCs.
 * Enables gossip propagation and social dynamics.
 */
@Entity(
    tableName = "social_circles",
    indices = [Index(value = ["circleType"])]
)
data class SocialCircleEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Circle identification
    val name: String,
    val description: String = "",
    val circleType: String = "informal", // informal, formal, family, workplace, criminal
    
    // Circle properties
    val memberIds: String = "", // JSON array of NPC IDs
    val leaderId: String? = null,
    val meetingLocationId: String? = null,
    
    // Social dynamics
    val cohesion: Int = 50, // 0-100, how tight-knit the group is
    val influence: Int = 50, // 0-100, group's social influence
    val reputation: Int = 50, // -100 to 100
    
    // Communication
    val gossipPool: String = "", // JSON of active gossip in this circle
    val sharedSecrets: String = "", // JSON of secrets known to the circle
    val communicationFrequency: Int = 50, // 0-100, how often members interact
    
    // Relationships with other circles
    val alliedCircleIds: String = "",
    val rivalCircleIds: String = "",
    
    // Activities
    val regularActivities: String = "", // JSON of group activities
    val sharedGoals: String = "", // JSON of group goals
    
    // Properties
    val isExclusive: Boolean = false,
    val requiresInvitation: Boolean = false,
    val entryRequirements: String = "", // JSON of requirements to join
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
