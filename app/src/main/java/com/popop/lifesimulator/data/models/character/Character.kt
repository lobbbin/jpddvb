package com.popop.lifesimulator.data.models.character

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Gender options
 */
enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-Binary")
}

/**
 * Main Character entity - the player's character
 */
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: Gender,
    val birthDate: Long,
    
    // Embedded stats
    val health: Int = 100,
    val energy: Int = 100,
    val stress: Int = 0,
    val charisma: Int = 50,
    val intellect: Int = 50,
    val cunning: Int = 50,
    val violence: Int = 50,
    val stealth: Int = 50,
    val perception: Int = 50,
    val willpower: Int = 50,
    
    // Secondary stats
    val reputation: Int = 0,
    val wealth: Double = 0.0,
    val piety: Int = 50,
    val loyaltyTendency: Int = 50,
    val addictionLevel: Int = 0,
    val heat: Int = 0,
    val educationLevel: Int = 0,
    val streetCred: Int = 0,
    val nobleStanding: Int = 0,
    val politicalCapital: Int = 0,
    
    // Serialized data
    val skillsJson: String = "{}",
    val traitsJson: String = "[]",
    
    // State
    val isAlive: Boolean = true,
    val currentLocationId: Long? = null,
    val currentLifePath: LifePath? = null,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long = System.currentTimeMillis()
) {
    fun getFullName(): String = "$firstName $lastName"
    
    fun getPrimaryStats(): PrimaryStats = PrimaryStats(
        health = health,
        energy = energy,
        stress = stress,
        charisma = charisma,
        intellect = intellect,
        cunning = cunning,
        violence = violence,
        stealth = stealth,
        perception = perception,
        willpower = willpower
    )
    
    fun getSecondaryStats(): SecondaryStats = SecondaryStats(
        reputation = reputation,
        wealth = wealth,
        piety = piety,
        loyaltyTendency = loyaltyTendency,
        addictionLevel = addictionLevel,
        heat = heat,
        educationLevel = educationLevel,
        streetCred = streetCred,
        nobleStanding = nobleStanding,
        politicalCapital = politicalCapital
    )
    
    fun getReputationTier(): ReputationTier = ReputationTier.fromScore(reputation)
    
    fun updateStats(newPrimary: PrimaryStats, newSecondary: SecondaryStats): Character = copy(
        health = newPrimary.health,
        energy = newPrimary.energy,
        stress = newPrimary.stress,
        charisma = newPrimary.charisma,
        intellect = newPrimary.intellect,
        cunning = newPrimary.cunning,
        violence = newPrimary.violence,
        stealth = newPrimary.stealth,
        perception = newPrimary.perception,
        willpower = newPrimary.willpower,
        reputation = newSecondary.reputation,
        wealth = newSecondary.wealth,
        piety = newSecondary.piety,
        loyaltyTendency = newSecondary.loyaltyTendency,
        addictionLevel = newSecondary.addictionLevel,
        heat = newSecondary.heat,
        educationLevel = newSecondary.educationLevel,
        streetCred = newSecondary.streetCred,
        nobleStanding = newSecondary.nobleStanding,
        politicalCapital = newSecondary.politicalCapital,
        updatedAt = System.currentTimeMillis()
    )
}

/**
 * Life path options - the major gameplay paths
 */
enum class LifePath(val displayName: String, val description: String) {
    NONE("None", "No chosen path yet"),
    ROYALTY("Royalty", "Rule as a monarch or noble"),
    POLITICS("Politics", "Climb the political ladder"),
    CRIME("Crime", "Build a criminal empire"),
    BUSINESS("Business", "Become a tycoon"),
    CAREER("Career", "Pursue a professional career")
}

/**
 * Character builder for creating new characters
 */
class CharacterBuilder {
    var firstName: String = ""
    var lastName: String = ""
    var age: Int = 18
    var gender: Gender = Gender.MALE
    var startingWealth: Double = 1000.0
    var startingTraits: List<String> = emptyList()
    var bonusSkills: Map<SkillType, Int> = emptyMap()
    
    fun build(): Character {
        val character = Character(
            firstName = firstName,
            lastName = lastName,
            age = age,
            gender = gender,
            birthDate = System.currentTimeMillis(),
            wealth = startingWealth
        )
        
        // Apply starting traits
        val traitLoadout = TraitLoadout(emptyList())
        val finalTraits = startingTraits.fold(traitLoadout) { acc, traitId ->
            acc.addTrait(traitId)
        }
        
        return character.copy(
            traitsJson = finalTraits.traits.joinToString(",")
        )
    }
}
