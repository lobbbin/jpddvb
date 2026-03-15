package com.popop.lifesimulator.data.models.world

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Faction categories
 */
enum class FactionCategory(val displayName: String) {
    ROYALTY("Royalty"),
    POLITICAL("Political"),
    CRIMINAL("Criminal"),
    BUSINESS("Business"),
    RELIGIOUS("Religious"),
    SOCIAL("Social"),
    MILITARY("Military"),
    OTHER("Other")
}

/**
 * Faction entity representing organizations and groups
 */
@Entity(tableName = "factions")
data class Faction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
    val category: FactionCategory,
    
    // Power and influence
    val powerLevel: Int = 50,  // 0-100
    val wealth: Double = 0.0,
    val memberCount: Int = 0,
    val territoryControl: Int = 0,  // 0-100
    
    // Relationship with player
    val opinionOfPlayer: Int = 0,  // -100 to +100
    val playerReputation: Int = 0,  // Player's standing in faction
    val playerRank: String = "Outsider",
    val isPlayerMember: Boolean = false,
    
    // State
    val isUnlocked: Boolean = false,
    val isHostile: Boolean = false,
    val leaderNpcId: Long? = null,
    
    // Relationships with other factions
    val alliedFactionIds: String = "",  // Comma-separated IDs
    val enemyFactionIds: String = "",  // Comma-separated IDs
    
    // Agenda
    val currentAgenda: String? = null,
    val agendaProgress: Int = 0,  // 0-100
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getPlayerOpinionTier(): OpinionTier {
        return when {
            opinionOfPlayer <= -80 -> OpinionTier.HATED
            opinionOfPlayer <= -50 -> OpinionTier.DISTRUSTED
            opinionOfPlayer <= -20 -> OpinionTier.DISLIKED
            opinionOfPlayer <= 20 -> OpinionTier.NEUTRAL
            opinionOfPlayer <= 50 -> OpinionTier.LIKED
            opinionOfPlayer <= 80 -> OpinionTier.TRUSTED
            else -> OpinionTier.REVERED
        }
    }
    
    fun getPlayerRankIndex(): Int = when (playerRank) {
        "Outsider" -> 0
        "Associate" -> 1
        "Member" -> 2
        "Veteran" -> 3
        "Elite" -> 4
        "Leader" -> 5
        else -> 0
    }
    
    fun canPromotePlayer(): Boolean = playerRank != "Leader" && isPlayerMember
    
    fun canDemotePlayer(): Boolean = playerRank != "Outsider" && isPlayerMember
}

enum class OpinionTier(val displayName: String) {
    HATED("Hated"),
    DISTRUSTED("Distrusted"),
    DISLIKED("Disliked"),
    NEUTRAL("Neutral"),
    LIKED("Liked"),
    TRUSTED("Trusted"),
    REVERED("Revered")
}

/**
 * Faction hierarchy ranks
 */
data class FactionRank(
    val name: String,
    val level: Int,
    val requiredReputation: Int = 0,
    val privileges: List<String> = emptyList()
)

/**
 * Predefined factions for each category
 */
object FactionRegistry {
    
    // Royalty Factions
    val THE_CROWN = Faction(
        name = "The Crown",
        description = "The royal family and their direct authority",
        category = FactionCategory.ROYALTY,
        powerLevel = 95,
        opinionOfPlayer = 0
    )
    
    val NOBLE_HOUSES = Faction(
        name = "Noble Houses",
        description = "The aristocratic families of the realm",
        category = FactionCategory.ROYALTY,
        powerLevel = 75,
        opinionOfPlayer = 0
    )
    
    val HIGH_COUNCIL = Faction(
        name = "High Council",
        description = "The king's closest advisors",
        category = FactionCategory.ROYALTY,
        powerLevel = 70,
        opinionOfPlayer = 0
    )
    
    val ROYAL_GUARD = Faction(
        name = "Royal Guard",
        description = "Elite protectors of the crown",
        category = FactionCategory.ROYALTY,
        powerLevel = 60,
        opinionOfPlayer = 0
    )
    
    // Political Factions
    val DEMOCRATIC_PARTY = Faction(
        name = "Democratic Party",
        description = "Progressive political party",
        category = FactionCategory.POLITICAL,
        powerLevel = 70,
        opinionOfPlayer = 0
    )
    
    val REPUBLICAN_PARTY = Faction(
        name = "Republican Party",
        description = "Conservative political party",
        category = FactionCategory.POLITICAL,
        powerLevel = 70,
        opinionOfPlayer = 0
    )
    
    val LOBBYING_GROUP = Faction(
        name = "Corporate Lobbyists",
        description = "Influence peddlers for big business",
        category = FactionCategory.POLITICAL,
        powerLevel = 65,
        opinionOfPlayer = 0
    )
    
    val MEDIA_OUTLET = Faction(
        name = "Major Media",
        description = "News organizations and press",
        category = FactionCategory.POLITICAL,
        powerLevel = 60,
        opinionOfPlayer = 0
    )
    
    // Criminal Factions
    val STREET_GANGS = Faction(
        name = "Street Gangs",
        description = "Local urban gangs",
        category = FactionCategory.CRIMINAL,
        powerLevel = 40,
        opinionOfPlayer = 0
    )
    
    val MAFIA = Faction(
        name = "The Mafia",
        description = "Organized crime syndicate",
        category = FactionCategory.CRIMINAL,
        powerLevel = 70,
        opinionOfPlayer = 0
    )
    
    val CARTEL = Faction(
        name = "Drug Cartel",
        description = "International drug trafficking organization",
        category = FactionCategory.CRIMINAL,
        powerLevel = 75,
        opinionOfPlayer = 0
    )
    
    val RUSSIAN_MOB = Faction(
        name = "Russian Mob",
        description = "Eastern European crime syndicate",
        category = FactionCategory.CRIMINAL,
        powerLevel = 65,
        opinionOfPlayer = 0
    )
    
    // Business Factions
    val CHAMBER_OF_COMMERCE = Faction(
        name = "Chamber of Commerce",
        description = "Business leaders association",
        category = FactionCategory.BUSINESS,
        powerLevel = 60,
        opinionOfPlayer = 0
    )
    
    val STOCK_EXCHANGE = Faction(
        name = "Stock Exchange",
        description = "Financial markets and traders",
        category = FactionCategory.BUSINESS,
        powerLevel = 70,
        opinionOfPlayer = 0
    )
    
    val LABOR_UNION = Faction(
        name = "Labor Union",
        description = "Workers' collective bargaining group",
        category = FactionCategory.BUSINESS,
        powerLevel = 50,
        opinionOfPlayer = 0
    )
    
    // Religious Factions
    val STATE_CHURCH = Faction(
        name = "State Church",
        description = "Official religious authority",
        category = FactionCategory.RELIGIOUS,
        powerLevel = 65,
        opinionOfPlayer = 0
    )
    
    val ALL_FACTIONS = listOf(
        THE_CROWN, NOBLE_HOUSES, HIGH_COUNCIL, ROYAL_GUARD,
        DEMOCRATIC_PARTY, REPUBLICAN_PARTY, LOBBYING_GROUP, MEDIA_OUTLET,
        STREET_GANGS, MAFIA, CARTEL, RUSSIAN_MOB,
        CHAMBER_OF_COMMERCE, STOCK_EXCHANGE, LABOR_UNION,
        STATE_CHURCH
    )
}
