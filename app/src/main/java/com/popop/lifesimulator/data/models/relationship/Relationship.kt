package com.popop.lifesimulator.data.models.relationship

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Relationship types between characters and NPCs
 */
enum class RelationshipType(val displayName: String, val category: RelationshipCategory) {
    // Familial
    PARENT_CHILD("Parent/Child", RelationshipCategory.FAMILIAL),
    CHILD_PARENT("Child/Parent", RelationshipCategory.FAMILIAL),
    SIBLING("Sibling", RelationshipCategory.FAMILIAL),
    GRANDPARENT_GRANDCHILD("Grandparent/Grandchild", RelationshipCategory.FAMILIAL),
    EXTENDED_FAMILY("Extended Family", RelationshipCategory.FAMILIAL),
    IN_LAW("In-Law", RelationshipCategory.FAMILIAL),
    STEP_FAMILY("Step-Family", RelationshipCategory.FAMILIAL),
    ADOPTED_FAMILY("Adopted Family", RelationshipCategory.FAMILIAL),
    GODPARENT_GODCHILD("Godparent/Godchild", RelationshipCategory.FAMILIAL),
    
    // Romantic
    SPOUSE("Spouse", RelationshipCategory.ROMANTIC),
    FIANCE("Fiancé(e)", RelationshipCategory.ROMANTIC),
    PARTNER("Partner", RelationshipCategory.ROMANTIC),
    LOVER("Lover", RelationshipCategory.ROMANTIC),
    EX_LOVER("Ex-Lover", RelationshipCategory.ROMANTIC),
    CRUSH("Crush", RelationshipCategory.ROMANTIC),
    ARRANGED_PARTNER("Arranged Partner", RelationshipCategory.ROMANTIC),
    ONE_NIGHT_STAND("One-Night Stand", RelationshipCategory.ROMANTIC),
    
    // Professional
    BOSS_EMPLOYEE("Boss/Employee", RelationshipCategory.PROFESSIONAL),
    EMPLOYEE_BOSS("Employee/Boss", RelationshipCategory.PROFESSIONAL),
    COWORKER("Coworker", RelationshipCategory.PROFESSIONAL),
    BUSINESS_PARTNER("Business Partner", RelationshipCategory.PROFESSIONAL),
    MENTOR_MENTEE("Mentor/Mentee", RelationshipCategory.PROFESSIONAL),
    MENTEE_MENTOR("Mentee/Mentor", RelationshipCategory.PROFESSIONAL),
    TEACHER_STUDENT("Teacher/Student", RelationshipCategory.PROFESSIONAL),
    STUDENT_TEACHER("Student/Teacher", RelationshipCategory.PROFESSIONAL),
    CLIENT_PROFESSIONAL("Client/Professional", RelationshipCategory.PROFESSIONAL),
    COMPETITOR("Competitor/Rival", RelationshipCategory.PROFESSIONAL),
    ALLY("Ally", RelationshipCategory.PROFESSIONAL),
    
    // Social
    FRIEND("Friend", RelationshipCategory.SOCIAL),
    CLOSE_FRIEND("Close Friend", RelationshipCategory.SOCIAL),
    BEST_FRIEND("Best Friend", RelationshipCategory.SOCIAL),
    ACQUAINTANCE("Acquaintance", RelationshipCategory.SOCIAL),
    CHILDHOOD_FRIEND("Childhood Friend", RelationshipCategory.SOCIAL),
    NEIGHBOR("Neighbor", RelationshipCategory.SOCIAL),
    GYM_BUDDY("Gym Buddy", RelationshipCategory.SOCIAL),
    ONLINE_FRIEND("Online Friend", RelationshipCategory.SOCIAL),
    
    // Criminal
    PARTNER_IN_CRIME("Partner in Crime", RelationshipCategory.CRIMINAL),
    CELLmate("Cellmate", RelationshipCategory.CRIMINAL),
    INFORMANT("Informant", RelationshipCategory.CRIMINAL),
    FENCE("Fence/Middleman", RelationshipCategory.CRIMINAL),
    MARK_VICTIM("Mark/Victim", RelationshipCategory.CRIMINAL),
    GANG_LEADER_MEMBER("Gang Leader/Member", RelationshipCategory.CRIMINAL),
    ENFORCER("Enforcer", RelationshipCategory.CRIMINAL),
    DRUG_CONNECT("Drug Connect", RelationshipCategory.CRIMINAL),
    CORRUPT_COP("Corrupt Cop", RelationshipCategory.CRIMINAL),
    
    // Unusual
    PET("Pet/Companion", RelationshipCategory.UNUSUAL),
    NEMESIS("Nemesis/Archenemy", RelationshipCategory.UNUSUAL),
    OBSESSION("Obsession", RelationshipCategory.UNUSUAL)
}

enum class RelationshipCategory(val displayName: String) {
    FAMILIAL("Familial"),
    ROMANTIC("Romantic"),
    PROFESSIONAL("Professional"),
    SOCIAL("Social"),
    CRIMINAL("Criminal"),
    UNUSUAL("Unusual")
}

/**
 * Relationship entity tracking the bond between character and NPC
 */
@Entity(
    tableName = "relationships",
    foreignKeys = [
        ForeignKey(
            entity = Npc::class,
            parentColumns = ["id"],
            childColumns = ["npcId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("npcId"), Index("characterId")]
)
data class Relationship(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val characterId: Long,
    val npcId: Long,
    val relationshipType: RelationshipType,
    
    // Core metrics (0-100 scale unless noted)
    val relationshipScore: Int = 0,  // -100 to +100 overall relationship
    val trust: Int = 50,
    val fear: Int = 0,
    val respect: Int = 50,
    val loyalty: Int = 50,
    val affection: Int = 0,  // Romantic/familial love
    val rivalry: Int = 0,
    
    // Debt/favors
    val owedByNpc: Int = 0,  // Favors NPC owes to character
    val owedToNpc: Int = 0,  // Favors character owes to NPC
    val monetaryDebt: Double = 0.0,  // Money owed (positive = NPC owes character)
    
    // Knowledge
    val knownSecrets: String = "",  // JSON of secret IDs
    val sharedMemories: String = "",  // JSON of memory IDs
    
    // State
    val isPublic: Boolean = true,  // Known to others
    val isStable: Boolean = true,  // Not in flux
    val lastInteraction: Long = System.currentTimeMillis(),
    
    // Metadata
    val notes: String = "",  // Player notes about this relationship
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getRelationshipStatus(): RelationshipStatus {
        return when {
            relationshipScore <= -80 -> RelationshipStatus.ARCHENEMY
            relationshipScore <= -50 -> RelationshipStatus.ENEMY
            relationshipScore <= -20 -> RelationshipStatus.HOSTILE
            relationshipScore <= 20 -> RelationshipStatus.NEUTRAL
            relationshipScore <= 50 -> RelationshipStatus.FRIENDLY
            relationshipScore <= 80 -> RelationshipStatus.CLOSE
            else -> RelationshipStatus.DEVOTED
        }
    }
    
    fun canAskFavor(): Boolean = relationshipScore > 30 && trust > 40
    
    fun canBetray(): Boolean = relationshipScore > 50 && (owedByNpc > 0 || monetaryDebt > 0)
    
    fun isRomantic(): Boolean = relationshipType.category == RelationshipCategory.ROMANTIC
    
    fun isFamilial(): Boolean = relationshipType.category == RelationshipCategory.FAMILIAL
    
    fun isProfessional(): Boolean = relationshipType.category == RelationshipCategory.PROFESSIONAL
    
    fun getTotalDebtValue(): Double = monetaryDebt + (owedByNpc * 10.0) - (owedToNpc * 10.0)
}

enum class RelationshipStatus(val displayName: String) {
    ARCHENEMY("Archenemy"),
    ENEMY("Enemy"),
    HOSTILE("Hostile"),
    NEUTRAL("Neutral"),
    FRIENDLY("Friendly"),
    CLOSE("Close"),
    DEVOTED("Devoted")
}

/**
 * Memory of an interaction or event between character and NPC
 */
data class RelationshipMemory(
    val id: String,
    val timestamp: Long,
    val event: String,
    val emotionalWeight: Int,  // -100 to +100
    val affectedMetrics: Map<String, Int>,  // Which metrics changed and by how much
    val location: String? = null,
    val witnesses: List<Long> = emptyList()  // NPC IDs
)

/**
 * NPC opinion modifiers based on traits and history
 */
data class OpinionModifiers(
    val traitCompatibility: Int = 0,
    val historyBonus: Int = 0,
    val reputationEffect: Int = 0,
    val factionEffect: Int = 0,
    val situationalEffect: Int = 0
) {
    fun getTotalModifier(): Int = traitCompatibility + historyBonus + reputationEffect + factionEffect + situationalEffect
}
