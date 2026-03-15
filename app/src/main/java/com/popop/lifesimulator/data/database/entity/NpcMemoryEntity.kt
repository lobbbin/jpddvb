package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * NPC memory entity for tracking what NPCs remember about interactions.
 * Enables NPCs to learn from experiences and react accordingly.
 */
@Entity(
    tableName = "npc_memories",
    foreignKeys = [
        ForeignKey(
            entity = NpcEntity::class,
            parentColumns = ["id"],
            childColumns = ["npcId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["npcId"]), Index(value = ["targetId"])]
)
data class NpcMemoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Memory identification
    val memoryId: String = UUID.randomUUID().toString(),
    val memoryType: String = "interaction", // interaction, observation, gossip, learned
    
    // Memory participants
    val npcId: String, // The NPC who remembers
    val targetId: String? = null, // Who the memory is about (player or other NPC)
    
    // Memory content
    val title: String,
    val description: String,
    val category: String = "social", // social, transaction, conflict, favor, secret
    
    // Emotional impact
    val emotionalImpact: Int = 0, // -100 to 100
    val emotions: String = "", // JSON of emotions felt (anger, joy, fear, etc.)
    
    // Memory details
    val locationId: String? = null,
    val witnesses: String = "", // Comma-separated NPC IDs who witnessed
    val evidence: String = "", // JSON of evidence related to this memory
    
    // Importance and retention
    val importance: Int = 50, // 0-100, affects how long memory lasts
    val decayRate: Float = 0.01f, // How fast memory fades (0-1 per day)
    val isSecret: Boolean = false,
    val isTraumatic: Boolean = false,
    val isFormative: Boolean = false, // Core memory that shapes personality
    
    // Associated data
    val relatedQuestId: String? = null,
    val relatedFactionId: String? = null,
    val itemsInvolved: String = "", // JSON of items involved
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val lastAccessedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null // Null = permanent memory
)
