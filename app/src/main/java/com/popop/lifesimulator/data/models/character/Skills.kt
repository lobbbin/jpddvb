package com.popop.lifesimulator.data.models.character

/**
 * Skill types representing all learnable abilities
 */
enum class SkillType(
    val displayName: String,
    val category: SkillCategory,
    val baseXpRate: Double = 1.0
) {
    // Social Skills
    PUBLIC_SPEAKING("Public Speaking", SkillCategory.SOCIAL),
    DEBATE("Debate", SkillCategory.SOCIAL),
    FUNDRAISING("Fundraising", SkillCategory.SOCIAL),
    PERSUASION("Persuasion", SkillCategory.SOCIAL),
    NEGOTIATION("Negotiation", SkillCategory.SOCIAL),
    LEADERSHIP("Leadership", SkillCategory.SOCIAL),
    TEACHING("Teaching", SkillCategory.SOCIAL),
    
    // Intellectual Skills
    LAW("Law", SkillCategory.INTELLECTUAL),
    MEDICINE("Medicine", SkillCategory.INTELLECTUAL),
    PROGRAMMING("Programming", SkillCategory.INTELLECTUAL),
    WRITING("Writing", SkillCategory.INTELLECTUAL),
    MATHEMATICS("Mathematics", SkillCategory.INTELLECTUAL),
    HISTORY("History", SkillCategory.INTELLECTUAL),
    RESEARCH("Research", SkillCategory.INTELLECTUAL),
    GRANT_WRITING("Grant Writing", SkillCategory.INTELLECTUAL),
    DIAGNOSIS("Diagnosis", SkillCategory.INTELLECTUAL),
    SURGERY("Surgery", SkillCategory.INTELLECTUAL),
    TRIAL_ADVOCACY("Trial Advocacy", SkillCategory.INTELLECTUAL),
    LEGAL_RESEARCH("Legal Research", SkillCategory.INTELLECTUAL),
    CONTRACT_DRAFTING("Contract Drafting", SkillCategory.INTELLECTUAL),
    
    // Criminal Skills
    CRIMINAL_SKILL("Criminal Skill", SkillCategory.CRIMINAL),
    DRUG_CRAFTING("Drug Crafting", SkillCategory.CRIMINAL),
    FORGERY("Forgery", SkillCategory.CRIMINAL),
    LOCKPICKING("Lockpicking", SkillCategory.CRIMINAL),
    HACKING("Hacking", SkillCategory.CRIMINAL),
    STEALTH("Stealth", SkillCategory.CRIMINAL),
    
    // Combat Skills
    MELEE_COMBAT("Melee Combat", SkillCategory.COMBAT),
    RANGED_COMBAT("Ranged Combat", SkillCategory.COMBAT),
    MARTIAL("Martial", SkillCategory.COMBAT),
    
    // Physical Skills
    FITNESS("Fitness", SkillCategory.PHYSICAL),
    ATHLETICS("Athletics", SkillCategory.PHYSICAL),
    YOGA("Yoga", SkillCategory.PHYSICAL),
    MEDITATION("Meditation", SkillCategory.PHYSICAL),
    
    // Trade Skills
    COOKING("Cooking", SkillCategory.TRADE),
    CLEANING("Cleaning", SkillCategory.TRADE),
    GARDENING("Gardening", SkillCategory.TRADE),
    REPAIR("Repair", SkillCategory.TRADE),
    WELDING("Welding", SkillCategory.TRADE),
    ELECTRICAL_WORK("Electrical Work", SkillCategory.TRADE),
    PLUMBING("Plumbing", SkillCategory.TRADE),
    CARPENTRY("Carpentry", SkillCategory.TRADE),
    MASONRY("Masonry", SkillCategory.TRADE),
    HVAC("HVAC", SkillCategory.TRADE),
    
    // Creative Skills
    PAINTING("Painting", SkillCategory.CREATIVE),
    MUSIC("Music", SkillCategory.CREATIVE),
    
    // Business Skills
    STEWARDSHIP("Stewardship", SkillCategory.BUSINESS),
    INTRIGUE("Intrigue", SkillCategory.BUSINESS),
    ACCOUNTING("Accounting", SkillCategory.BUSINESS),
    MARKETING("Marketing", SkillCategory.BUSINESS),
    SALES("Sales", SkillCategory.BUSINESS),
    
    // Other Skills
    DRIVING("Driving/Piloting", SkillCategory.OTHER),
    SPORTS("Sports", SkillCategory.OTHER),
    SALES("Sales", SkillCategory.OTHER);

    fun isHigherThan(other: SkillType, level: Int, otherLevel: Int): Boolean {
        return level > otherLevel
    }
}

enum class SkillCategory(val displayName: String) {
    SOCIAL("Social"),
    INTELLECTUAL("Intellectual"),
    CRIMINAL("Criminal"),
    COMBAT("Combat"),
    PHYSICAL("Physical"),
    TRADE("Trade"),
    CREATIVE("Creative"),
    BUSINESS("Business"),
    OTHER("Other")
}

/**
 * Represents a skill with its current level and experience
 */
data class Skill(
    val type: SkillType,
    val level: Int = 0,
    val experience: Double = 0.0,
    val lastUsed: Long = System.currentTimeMillis()
) {
    companion object {
        const val MAX_LEVEL = 100
        const val XP_PER_LEVEL_BASE = 100.0
    }

    fun xpForNextLevel(): Double = XP_PER_LEVEL_BASE * (level + 1)

    fun gainXp(amount: Double): Skill {
        val newXp = experience + amount
        val newLevel = calculateLevel(newXp)
        val remainingXp = newXp - xpForLevel(newLevel)
        
        return copy(
            level = newLevel.coerceAtMost(MAX_LEVEL),
            experience = remainingXp,
            lastUsed = System.currentTimeMillis()
        )
    }

    private fun calculateLevel(totalXp: Double): Int {
        var level = 0
        var xpNeeded = XP_PER_LEVEL_BASE
        var remaining = totalXp
        
        while (remaining >= xpNeeded && level < MAX_LEVEL) {
            remaining -= xpNeeded
            level++
            xpNeeded = XP_PER_LEVEL_BASE * (level + 1)
        }
        
        return level
    }

    private fun xpForLevel(level: Int): Double {
        var total = 0.0
        for (i in 0 until level) {
            total += XP_PER_LEVEL_BASE * (i + 1)
        }
        return total
    }

    fun getProgressToNextLevel(): Float {
        if (level >= MAX_LEVEL) return 1.0f
        return (experience / xpForNextLevel()).toFloat().coerceIn(0f, 1f)
    }
}

/**
 * Registry of all skills for a character
 */
data class SkillRegistry(
    val skills: Map<SkillType, Skill> = emptyMap()
) {
    fun getSkill(type: SkillType): Skill = skills[type] ?: Skill(type)
    
    fun getSkillLevel(type: SkillType): Int = getSkill(type).level
    
    fun gainXp(type: SkillType, amount: Double): SkillRegistry {
        val currentSkill = getSkill(type)
        val updatedSkill = currentSkill.gainXp(amount)
        return copy(skills = skills + (type to updatedSkill))
    }
    
    fun getSkillsByCategory(category: SkillCategory): List<Skill> {
        return skills.values.filter { it.type.category == category }
    }
    
    fun getHighestSkill(): Skill? = skills.values.maxByOrNull { it.level }
    
    fun getAverageSkillLevel(): Double {
        return if (skills.isEmpty()) 0.0 else skills.values.map { it.level }.average()
    }
}
