package com.popop.lifesimulator.data.models.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Primary Stats - Core character attributes (0-100 scale)
 */
data class PrimaryStats(
    val health: Int = 100,
    val energy: Int = 100,
    val stress: Int = 0,
    val charisma: Int = 50,
    val intellect: Int = 50,
    val cunning: Int = 50,
    val violence: Int = 50,
    val stealth: Int = 50,
    val perception: Int = 50,
    val willpower: Int = 50
) {
    fun clamp(): PrimaryStats = copy(
        health = health.coerceIn(0, 100),
        energy = energy.coerceIn(0, 100),
        stress = stress.coerceIn(0, 100),
        charisma = charisma.coerceIn(0, 100),
        intellect = intellect.coerceIn(0, 100),
        cunning = cunning.coerceIn(0, 100),
        violence = violence.coerceIn(0, 100),
        stealth = stealth.coerceIn(0, 100),
        perception = perception.coerceIn(0, 100),
        willpower = willpower.coerceIn(0, 100)
    )

    fun modifyHealth(amount: Int) = copy(health = (health + amount).coerceIn(0, 100))
    fun modifyEnergy(amount: Int) = copy(energy = (energy + amount).coerceIn(0, 100))
    fun modifyStress(amount: Int) = copy(stress = (stress + amount).coerceIn(0, 100))
    fun modifyCharisma(amount: Int) = copy(charisma = (charisma + amount).coerceIn(0, 100))
    fun modifyIntellect(amount: Int) = copy(intellect = (intellect + amount).coerceIn(0, 100))
    fun modifyCunning(amount: Int) = copy(cunning = (cunning + amount).coerceIn(0, 100))
    fun modifyViolence(amount: Int) = copy(violence = (violence + amount).coerceIn(0, 100))
    fun modifyStealth(amount: Int) = copy(stealth = (stealth + amount).coerceIn(0, 100))
    fun modifyPerception(amount: Int) = copy(perception = (perception + amount).coerceIn(0, 100))
    fun modifyWillpower(amount: Int) = copy(willpower = (willpower + amount).coerceIn(0, 100))
}

/**
 * Secondary Stats - Derived and situational attributes
 */
data class SecondaryStats(
    val reputation: Int = 0,           // -100 to +100
    val wealth: Double = 0.0,
    val piety: Int = 50,               // 0-100
    val loyaltyTendency: Int = 50,     // 0-100
    val addictionLevel: Int = 0,       // 0-100
    val heat: Int = 0,                 // 0-100 (law enforcement attention)
    val educationLevel: Int = 0,       // 0-100
    val streetCred: Int = 0,           // 0-100
    val nobleStanding: Int = 0,        // 0-100
    val politicalCapital: Int = 0      // 0-100
) {
    fun clamp(): SecondaryStats = copy(
        reputation = reputation.coerceIn(-100, 100),
        piety = piety.coerceIn(0, 100),
        loyaltyTendency = loyaltyTendency.coerceIn(0, 100),
        addictionLevel = addictionLevel.coerceIn(0, 100),
        heat = heat.coerceIn(0, 100),
        educationLevel = educationLevel.coerceIn(0, 100),
        streetCred = streetCred.coerceIn(0, 100),
        nobleStanding = nobleStanding.coerceIn(0, 100),
        politicalCapital = politicalCapital.coerceIn(0, 100)
    )

    fun modifyReputation(amount: Int) = copy(reputation = (reputation + amount).coerceIn(-100, 100))
    fun modifyWealth(amount: Double) = copy(wealth = maxOf(0.0, wealth + amount))
    fun modifyPiety(amount: Int) = copy(piety = (piety + amount).coerceIn(0, 100))
    fun modifyHeat(amount: Int) = copy(heat = (heat + amount).coerceIn(0, 100))
    fun modifyStreetCred(amount: Int) = copy(streetCred = (streetCred + amount).coerceIn(0, 100))
}

/**
 * Reputation tiers based on score
 */
enum class ReputationTier(val minScore: Int, val displayName: String) {
    HATED(-100, "Hated"),
    DISLIKED(-50, "Disliked"),
    NEUTRAL(-20, "Neutral"),
    ACCEPTED(0, "Accepted"),
    LIKED(20, "Liked"),
    RESPECTED(50, "Respected"),
    REVERED(80, "Revered");

    companion object {
        fun fromScore(score: Int): ReputationTier = entries.find { score >= it.minScore } ?: NEUTRAL
    }
}
