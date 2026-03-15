package com.popop.lifesimulator.core.utils

/**
 * Represents a stat modifier (buff/debuff) that affects entity stats.
 * Supports both flat and percentage-based modifications.
 */
data class StatModifier(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val modifierType: ModifierType = ModifierType.BUFF,
    
    // Stat changes (flat values)
    val flatChanges: Map<StatType, Int> = emptyMap(),
    
    // Percentage changes (0.1 = 10% bonus)
    val percentageChanges: Map<StatType, Float> = emptyMap(),
    
    // Duration
    val durationMs: Long? = null, // null = permanent
    val appliedAt: Long = System.currentTimeMillis(),
    
    // Stacking
    val maxStacks: Int = 1,
    val currentStacks: Int = 1,
    val stackType: StackType = StackType.NONE,
    
    // Properties
    val isDispellable: Boolean = true,
    val isVisible: Boolean = true,
    val priority: Int = 0,
    
    // Source tracking
    val sourceId: String? = null,
    val sourceType: String? = null
) {
    /**
     * Check if this modifier has expired.
     */
    fun isExpired(currentTime: Long = System.currentTimeMillis()): Boolean {
        return durationMs?.let { (appliedAt + it) <= currentTime } ?: false
    }
    
    /**
     * Get remaining duration in milliseconds.
     */
    fun getRemainingDuration(currentTime: Long = System.currentTimeMillis()): Long? {
        return durationMs?.let { maxOf(0, (appliedAt + it) - currentTime) }
    }
    
    /**
     * Get formatted remaining time string.
     */
    fun getFormattedRemainingTime(currentTime: Long = System.currentTimeMillis()): String {
        val remaining = getRemainingDuration(currentTime) ?: return "Permanent"
        
        val seconds = remaining / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        
        return when {
            days > 0 -> "${days}d ${hours % 24}h"
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
    
    /**
     * Check if this modifier can stack with another.
     */
    fun canStackWith(other: StatModifier): Boolean {
        if (id != other.id) return false
        if (currentStacks >= maxStacks) return false
        
        return when (stackType) {
            StackType.NONE -> false
            StackType.INTENSITY -> true
            StackType.DURATION -> true
        }
    }
    
    /**
     * Apply stacking.
     */
    fun stack(): StatModifier {
        return if (canStackWith(this)) {
            when (stackType) {
                StackType.INTENSITY -> copy(currentStacks = currentStacks + 1)
                StackType.DURATION -> copy(durationMs = durationMs?.let { it * 2 })
                StackType.NONE -> this
            }
        } else {
            this
        }
    }
    
    companion object {
        /**
         * Create a temporary buff.
         */
        fun createBuff(
            name: String,
            statChanges: Map<StatType, Int>,
            durationMinutes: Int
        ): StatModifier {
            return StatModifier(
                name = name,
                modifierType = ModifierType.BUFF,
                flatChanges = statChanges,
                durationMs = durationMinutes * 60 * 1000L
            )
        }
        
        /**
         * Create a permanent passive bonus.
         */
        fun createPassive(
            name: String,
            percentageChanges: Map<StatType, Float>
        ): StatModifier {
            return StatModifier(
                name = name,
                modifierType = ModifierType.PASSIVE,
                percentageChanges = percentageChanges,
                durationMs = null,
                isDispellable = false
            )
        }
        
        /**
         * Create a debuff.
         */
        fun createDebuff(
            name: String,
            statChanges: Map<StatType, Int>,
            durationMinutes: Int
        ): StatModifier {
            return StatModifier(
                name = name,
                modifierType = ModifierType.DEBUFF,
                flatChanges = statChanges.mapValues { -it.value },
                durationMs = durationMinutes * 60 * 1000L
            )
        }
    }
}

/**
 * Types of stat modifiers.
 */
enum class ModifierType {
    BUFF,      // Positive temporary effect
    DEBUFF,    // Negative temporary effect
    PASSIVE,   // Permanent positive effect
    CURSE,     // Permanent negative effect
    EQUIPMENT  // From equipped items
}

/**
 * How modifiers stack.
 */
enum class StackType {
    NONE,       // Doesn't stack
    INTENSITY,  // Stacks by increasing effect
    DURATION    // Stacks by extending duration
}

/**
 * Types of stats that can be modified.
 */
enum class StatType {
    // Core stats
    HEALTH, MAX_HEALTH, ENERGY, MAX_ENERGY,
    WEALTH, REPUTATION, INFLUENCE, HAPPINESS, STRESS,
    
    // Attributes
    INTELLIGENCE, CHARISMA, STRENGTH, DEXTERITY, CONSTITUTION, WISDOM, LUCK,
    
    // Skills
    COMBAT, BUSINESS, POLITICS, STEALTH, PERSUASION, LEADERSHIP, CRAFTING, KNOWLEDGE,
    
    // Derived stats
    DAMAGE, DEFENSE, SPEED, CRITICAL_CHANCE, CRITICAL_DAMAGE,
    EXPERIENCE_GAIN, SKILL_GAIN, LOOT_QUALITY, PRICES,
    
    // Special
    MOVEMENT_SPEED, COOLDOWN_REDUCTION, RESOURCE_REGEN
}

/**
 * Calculator for applying stat modifiers.
 */
object StatModifierCalculator {
    
    /**
     * Calculate final stat value after applying all modifiers.
     */
    fun calculateStat(
        baseValue: Int,
        statType: StatType,
        modifiers: List<StatModifier>
    ): Int {
        var flatBonus = 0
        var percentageBonus = 0f
        
        for (modifier in modifiers) {
            // Add flat changes
            flatBonus += modifier.flatChanges[statType] ?: 0
            
            // Add percentage changes (multiplied by stacks for intensity stacking)
            val percentChange = modifier.percentageChanges[statType] ?: 0f
            percentageBonus += percentChange * modifier.currentStacks
        }
        
        // Apply calculations
        var result = baseValue + flatBonus
        result = (result * (1 + percentageBonus)).toInt()
        
        return result.coerceAtLeast(0)
    }
    
    /**
     * Calculate all modified stats.
     */
    fun calculateAllStats(
        baseStats: Map<StatType, Int>,
        modifiers: List<StatModifier>
    ): Map<StatType, Int> {
        return baseStats.mapValues { (statType, baseValue) ->
            calculateStat(baseValue, statType, modifiers)
        }
    }
    
    /**
     * Get active modifiers from a list (filter out expired).
     */
    fun getActiveModifiers(
        modifiers: List<StatModifier>,
        currentTime: Long = System.currentTimeMillis()
    ): List<StatModifier> {
        return modifiers.filter { !it.isExpired(currentTime) }
    }
    
    /**
     * Get modifiers sorted by priority (highest first).
     */
    fun getModifiersByPriority(modifiers: List<StatModifier>): List<StatModifier> {
        return modifiers.sortedByDescending { it.priority }
    }
}
