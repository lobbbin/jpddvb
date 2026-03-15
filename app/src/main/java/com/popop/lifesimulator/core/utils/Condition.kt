package com.popop.lifesimulator.core.utils

/**
 * Represents a condition affecting an entity (disease, injury, blessing, curse, etc.).
 * Conditions can have severity, progression, and various effects.
 */
data class Condition(
    val id: String = java.util.UUID.randomUUID().toString(),
    val conditionId: String, // Template ID
    val name: String,
    val description: String = "",
    val conditionType: ConditionType = ConditionType.STATUS,
    
    // Severity (0-100)
    val severity: Int = 50,
    val maxSeverity: Int = 100,
    
    // Progression rate (-1.0 healing to 1.0 worsening per day)
    val progressionRate: Float = 0.0f,
    
    // Effects on stats
    val effects: Map<StatType, Int> = emptyMap(),
    val percentageEffects: Map<StatType, Float> = emptyMap(),
    
    // Duration
    val durationMs: Long? = null,
    val appliedAt: Long = System.currentTimeMillis(),
    
    // Triggers
    val triggerConditions: List<String> = emptyList(),
    val removalConditions: List<String> = emptyList(),
    
    // Treatment
    val cureMethod: String = "",
    val cureItemId: String? = null,
    val cureSkillRequired: Int = 0,
    
    // Properties
    val isContagious: Boolean = false,
    val isIncurable: Boolean = false,
    val isFatal: Boolean = false,
    val fatalityThreshold: Int = 100,
    val isVisible: Boolean = true,
    val isStackable: Boolean = false
) {
    /**
     * Check if condition has expired.
     */
    fun isExpired(currentTime: Long = System.currentTimeMillis()): Boolean {
        return durationMs?.let { (appliedAt + it) <= currentTime } ?: false
    }
    
    /**
     * Check if condition is fatal at current severity.
     */
    fun isCurrentlyFatal(): Boolean = isFatal && severity >= fatalityThreshold
    
    /**
     * Get remaining duration.
     */
    fun getRemainingDuration(currentTime: Long = System.currentTimeMillis()): Long? {
        return durationMs?.let { maxOf(0, (appliedAt + it) - currentTime) }
    }
    
    /**
     * Progress the condition by specified days.
     */
    fun progress(days: Int): Condition {
        if (isIncurable) return this
        
        val newSeverity = (severity + (progressionRate * days * 100)).toInt()
            .coerceIn(0, maxSeverity)
        
        return copy(severity = newSeverity)
    }
    
    /**
     * Apply treatment to reduce severity.
     */
    fun treat(effectiveness: Float): Condition {
        val reduction = (maxSeverity * effectiveness).toInt()
        val newSeverity = (severity - reduction).coerceAtLeast(0)
        
        return copy(severity = newSeverity)
    }
    
    /**
     * Check if condition can be cured by specified item.
     */
    fun canBeCuredBy(itemId: String): Boolean = cureItemId == itemId
    
    /**
     * Check if condition meets removal conditions.
     */
    fun meetsRemovalConditions(activeFlags: Set<String>): Boolean {
        return removalConditions.any { it in activeFlags }
    }
    
    /**
     * Get severity percentage.
     */
    fun severityPercentage(): Float = if (maxSeverity > 0) severity.toFloat() / maxSeverity else 0f
    
    /**
     * Get severity description.
     */
    fun getSeverityDescription(): String {
        return when (severityPercentage()) {
            in 0.0..0.2 -> "Minor"
            in 0.2..0.4 -> "Moderate"
            in 0.4..0.6 -> "Serious"
            in 0.6..0.8 -> "Severe"
            in 0.8..1.0 -> "Critical"
            else -> "Unknown"
        }
    }
    
    companion object {
        /**
         * Create a disease condition.
         */
        fun createDisease(
            name: String,
            severity: Int = 50,
            progressionRate: Float = 0.1f,
            effects: Map<StatType, Int> = emptyMap()
        ): Condition {
            return Condition(
                conditionId = "disease_${name.lowercase()}",
                name = name,
                conditionType = ConditionType.DISEASE,
                severity = severity,
                progressionRate = progressionRate,
                effects = effects,
                isContagious = true,
                cureMethod = "Medical treatment"
            )
        }
        
        /**
         * Create an injury condition.
         */
        fun createInjury(
            name: String,
            severity: Int = 50,
            effects: Map<StatType, Int> = emptyMap()
        ): Condition {
            return Condition(
                conditionId = "injury_${name.lowercase()}",
                name = name,
                conditionType = ConditionType.INJURY,
                severity = severity,
                progressionRate = -0.05f, // Natural healing
                effects = effects,
                cureMethod = "Rest and medical care"
            )
        }
        
        /**
         * Create a blessing condition.
         */
        fun createBlessing(
            name: String,
            durationHours: Int,
            effects: Map<StatType, Int> = emptyMap()
        ): Condition {
            return Condition(
                conditionId = "blessing_${name.lowercase()}",
                name = name,
                conditionType = ConditionType.BLESSING,
                effects = effects,
                durationMs = durationHours * 60 * 60 * 1000L,
                isVisible = true
            )
        }
        
        /**
         * Create a curse condition.
         */
        fun createCurse(
            name: String,
            severity: Int = 50,
            effects: Map<StatType, Int> = emptyMap()
        ): Condition {
            return Condition(
                conditionId = "curse_${name.lowercase()}",
                name = name,
                conditionType = ConditionType.CURSE,
                severity = severity,
                effects = effects.mapValues { -it.value },
                isIncurable = false,
                cureMethod = "Magical cleansing or ritual"
            )
        }
    }
}

/**
 * Types of conditions.
 */
enum class ConditionType {
    STATUS,     // General status effect
    DISEASE,    // Illness that can spread
    INJURY,     // Physical harm
    BLESSING,   // Positive divine effect
    CURSE,      // Negative magical effect
    POISON,     // Toxic effect
    ENCHANTMENT // Magical enhancement
}

/**
 * Manager for tracking and processing conditions.
 */
class ConditionManager {
    
    private val activeConditions = mutableListOf<Condition>()
    
    /**
     * Add a condition.
     */
    fun addCondition(condition: Condition) {
        val existing = activeConditions.find { it.id == condition.id }
        if (existing != null && condition.isStackable) {
            // Stack if possible
            val index = activeConditions.indexOf(existing)
            activeConditions[index] = existing.copy(severity = (existing.severity + condition.severity) / 2)
        } else {
            activeConditions.add(condition)
        }
    }
    
    /**
     * Remove a condition.
     */
    fun removeCondition(conditionId: String): Boolean {
        return activeConditions.removeAll { it.id == conditionId }
    }
    
    /**
     * Get all active conditions.
     */
    fun getActiveConditions(): List<Condition> = activeConditions.toList()
    
    /**
     * Get conditions by type.
     */
    fun getConditionsByType(type: ConditionType): List<Condition> {
        return activeConditions.filter { it.conditionType == type }
    }
    
    /**
     * Get negative conditions (debuffs).
     */
    fun getNegativeConditions(): List<Condition> {
        return activeConditions.filter { 
            it.conditionType in listOf(ConditionType.DISEASE, ConditionType.INJURY, ConditionType.CURSE, ConditionType.POISON)
        }
    }
    
    /**
     * Get positive conditions (buffs).
     */
    fun getPositiveConditions(): List<Condition> {
        return activeConditions.filter {
            it.conditionType in listOf(ConditionType.BLESSING, ConditionType.ENCHANTMENT)
        }
    }
    
    /**
     * Progress all conditions by specified days.
     */
    fun progressConditions(days: Int) {
        for (i in activeConditions.indices) {
            activeConditions[i] = activeConditions[i].progress(days)
        }
        // Remove expired
        activeConditions.removeAll { it.isExpired() }
    }
    
    /**
     * Check for fatal conditions.
     */
    fun hasFatalCondition(): Boolean {
        return activeConditions.any { it.isCurrentlyFatal() }
    }
    
    /**
     * Get total stat effects from all conditions.
     */
    fun getTotalEffects(): Map<StatType, Int> {
        val effects = mutableMapOf<StatType, Int>()
        for (condition in activeConditions) {
            for ((stat, value) in condition.effects) {
                effects[stat] = (effects[stat] ?: 0) + value
            }
        }
        return effects
    }
    
    /**
     * Clear all conditions.
     */
    fun clearAll() {
        activeConditions.clear()
    }
}
