package com.popop.lifesimulator.data.models.character

/**
 * Trait categories for organization
 */
enum class TraitCategory(val displayName: String) {
    POSITIVE("Positive"),
    NEGATIVE("Negative"),
    NEUTRAL("Neutral/Quirk"),
    HEREDITARY("Hereditary"),
    ACQUIRED("Acquired")
}

/**
 * Trait effects that modify stats or behavior
 */
sealed class TraitEffect {
    data class StatModifier(val stat: String, val amount: Int) : TraitEffect()
    data class StatMultiplier(val stat: String, val multiplier: Double) : TraitEffect()
    data class SkillBonus(val skillType: SkillType, val bonus: Int) : TraitEffect()
    data class UnlockAbility(val abilityId: String) : TraitEffect()
    data class ModifyEventChance(val eventType: String, val modifier: Double) : TraitEffect()
    object BlockTrait : TraitEffect()
}

/**
 * Base trait definition
 */
data class Trait(
    val id: String,
    val displayName: String,
    val description: String,
    val category: TraitCategory,
    val rarity: TraitRarity = TraitRarity.COMMON,
    val effects: List<TraitEffect> = emptyList(),
    val conflictingTraits: List<String> = emptyList(),
    val synergisticTraits: List<String> = emptyList(),
    val acquisitionMethods: List<AcquisitionMethod> = emptyList(),
    val hereditary: Boolean = false,
    val removable: Boolean = true
)

enum class TraitRarity(val displayName: String, val weight: Int) {
    COMMON("Common", 100),
    UNCOMMON("Uncommon", 50),
    RARE("Rare", 20),
    EPIC("Epic", 10),
    LEGENDARY("Legendary", 5)
}

enum class AcquisitionMethod(val displayName: String) {
    BIRTH("Birth"),
    CHILDHOOD("Childhood Event"),
    LIFE_EVENT("Life Event"),
    CHOICE("Player Choice"),
    ACHIEVEMENT("Achievement"),
    INHERITANCE("Inheritance"),
    MEDICAL("Medical Condition"),
    TRAINING("Training")
}

/**
 * Trait registry containing all available traits
 */
object TraitRegistry {
    
    // Positive Traits
    val AMBITIOUS = Trait(
        id = "ambitious",
        displayName = "Ambitious",
        description = "Driven to achieve great things",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("stress", 10),
            TraitEffect.StatModifier("willpower", 15)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.CHOICE)
    )
    
    val CHARISMATIC = Trait(
        id = "charismatic",
        displayName = "Charismatic",
        description = "Naturally charming and persuasive",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("charisma", 20),
            TraitEffect.SkillBonus(SkillType.PUBLIC_SPEAKING, 10),
            TraitEffect.SkillBonus(SkillType.PERSUASION, 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH)
    )
    
    val LUCKY = Trait(
        id = "lucky",
        displayName = "Lucky",
        description = "Fortune favors you",
        category = TraitCategory.POSITIVE,
        rarity = TraitRarity.LEGENDARY,
        effects = listOf(
            TraitEffect.ModifyEventChance("positive", 1.5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH)
    )
    
    val CONNECTED = Trait(
        id = "connected",
        displayName = "Connected",
        description = "You know people in high places",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("politicalCapital", 15)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.INHERITANCE)
    )
    
    val RESILIENT = Trait(
        id = "resilient",
        displayName = "Resilient",
        description = "You bounce back from adversity",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("willpower", 15),
            TraitEffect.StatModifier("health", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.LIFE_EVENT)
    )
    
    val JUST = Trait(
        id = "just",
        displayName = "Just",
        description = "You have a strong sense of fairness",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("reputation", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val BRAVE = Trait(
        id = "brave",
        displayName = "Brave",
        description = "You face danger without fear",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("violence", 10),
            TraitEffect.StatModifier("willpower", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.LIFE_EVENT)
    )
    
    val DILIGENT = Trait(
        id = "diligent",
        displayName = "Diligent",
        description = "Hardworking and persistent",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("stress", 5),
            TraitEffect.StatModifier("willpower", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val ATHLETIC = Trait(
        id = "athletic",
        displayName = "Athletic",
        description = "Naturally gifted physically",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("health", 15),
            TraitEffect.SkillBonus(SkillType.FITNESS, 15)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH)
    )
    
    val BOOKWORM = Trait(
        id = "bookworm",
        displayName = "Bookworm",
        description = "You love learning from books",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("intellect", 15),
            TraitEffect.SkillBonus(SkillType.HISTORY, 10),
            TraitEffect.SkillBonus(SkillType.WRITING, 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val LEADER = Trait(
        id = "leader",
        displayName = "Leader",
        description = "Others naturally follow you",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("charisma", 10),
            TraitEffect.SkillBonus(SkillType.LEADERSHIP, 20)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.LIFE_EVENT)
    )
    
    // Negative Traits
    val GREEDY = Trait(
        id = "greedy",
        displayName = "Greedy",
        description = "You desire wealth above all",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("reputation", -10)
        ),
        conflictingTraits = listOf("just", "humble"),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val PARANOID = Trait(
        id = "paranoid",
        displayName = "Paranoid",
        description = "You trust no one",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("stress", 15),
            TraitEffect.StatModifier("perception", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.LIFE_EVENT)
    )
    
    val ADDICT = Trait(
        id = "addict",
        displayName = "Addict",
        description = "You struggle with substance dependency",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("health", -15),
            TraitEffect.StatModifier("willpower", -10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.LIFE_EVENT)
    )
    
    val HOT_HEADED = Trait(
        id = "hot_headed",
        displayName = "Hot-Headed",
        description = "You have a quick temper",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("violence", 10),
            TraitEffect.StatModifier("charisma", -5)
        ),
        conflictingTraits = listOf("patient", "just"),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.CHOICE)
    )
    
    val COWARDLY = Trait(
        id = "cowardly",
        displayName = "Cowardly",
        description = "You avoid danger and confrontation",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("violence", -15),
            TraitEffect.StatModifier("stealth", 10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH)
    )
    
    val DISHONEST = Trait(
        id = "dishonest",
        displayName = "Dishonest",
        description = "You lie and deceive easily",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("cunning", 15),
            TraitEffect.StatModifier("reputation", -10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val SICKLY = Trait(
        id = "sickly",
        displayName = "Sickly",
        description = "You have a weak constitution",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("health", -20),
            TraitEffect.StatModifier("energy", -10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.MEDICAL)
    )
    
    val LAZY = Trait(
        id = "lazy",
        displayName = "Lazy",
        description = "You avoid work and effort",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("stress", -5),
            TraitEffect.StatModifier("willpower", -10)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH, AcquisitionMethod.CHOICE)
    )
    
    val CRUEL = Trait(
        id = "cruel",
        displayName = "Cruel",
        description = "You take pleasure in others' suffering",
        category = TraitCategory.NEGATIVE,
        effects = listOf(
            TraitEffect.StatModifier("violence", 10),
            TraitEffect.StatModifier("reputation", -20)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    // Neutral/Quirk Traits
    val RELIGIOUS = Trait(
        id = "religious",
        displayName = "Religious",
        description = "You are devoted to your faith",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("piety", 20),
            TraitEffect.StatModifier("stress", -5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val CYNICAL = Trait(
        id = "cynical",
        displayName = "Cynical",
        description = "You distrust others' motives",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("perception", 10),
            TraitEffect.StatModifier("charisma", -5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.LIFE_EVENT)
    )
    
    val ROMANTIC = Trait(
        id = "romantic",
        displayName = "Romantic",
        description = "You believe in love and passion",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("charisma", 5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.BIRTH)
    )
    
    val SOLITARY = Trait(
        id = "solitary",
        displayName = "Solitary",
        description = "You prefer being alone",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("stress", -5),
            TraitEffect.StatModifier("charisma", -5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val NIGHT_OWL = Trait(
        id = "night_owl",
        displayName = "Night Owl",
        description = "You are most active at night",
        category = TraitCategory.NEUTRAL,
        effects = listOf(),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val EARLY_BIRD = Trait(
        id = "early_bird",
        displayName = "Early Bird",
        description = "You rise with the sun",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("energy", 5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val SUPERSTITIOUS = Trait(
        id = "superstitious",
        displayName = "Superstitious",
        description = "You believe in omens and luck",
        category = TraitCategory.NEUTRAL,
        effects = listOf(),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val PRAGMATIC = Trait(
        id = "pragmatic",
        displayName = "Pragmatic",
        description = "You focus on practical solutions",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("intellect", 5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val IDEALISTIC = Trait(
        id = "idealistic",
        displayName = "Idealistic",
        description = "You believe in making the world better",
        category = TraitCategory.NEUTRAL,
        effects = listOf(
            TraitEffect.StatModifier("reputation", 5)
        ),
        acquisitionMethods = listOf(AcquisitionMethod.CHOICE)
    )
    
    val ALL_TRAITS = listOf(
        AMBITIOUS, CHARISMATIC, LUCKY, CONNECTED, RESILIENT, JUST, BRAVE, DILIGENT, ATHLETIC, BOOKWORM, LEADER,
        GREEDY, PARANOID, ADDICT, HOT_HEADED, COWARDLY, DISHONEST, SICKLY, LAZY, CRUEL,
        RELIGIOUS, CYNICAL, ROMANTIC, SOLITARY, NIGHT_OWL, EARLY_BIRD, SUPERSTITIOUS, PRAGMATIC, IDEALISTIC
    )
    
    fun getTraitById(id: String): Trait? = ALL_TRAITS.find { it.id == id }
    
    fun getTraitsByCategory(category: TraitCategory): List<Trait> = ALL_TRAITS.filter { it.category == category }
    
    fun getConflictingTraits(trait: Trait): List<Trait> = ALL_TRAITS.filter { trait.conflictingTraits.contains(it.id) }
    
    fun getSynergisticTraits(trait: Trait): List<Trait> = ALL_TRAITS.filter { trait.synergisticTraits.contains(it.id) }
}

/**
 * Character's active traits
 */
data class TraitLoadout(
    val traits: List<String> = emptyList()
) {
    fun hasTrait(traitId: String): Boolean = traits.contains(traitId)
    
    fun addTrait(traitId: String): TraitLoadout {
        if (hasTrait(traitId)) return this
        val trait = TraitRegistry.getTraitById(traitId) ?: return this
        
        // Check for conflicts
        val conflicts = trait.conflictingTraits.filter { hasTrait(it) }
        if (conflicts.isNotEmpty()) return this
        
        return copy(traits = traits + traitId)
    }
    
    fun removeTrait(traitId: String): TraitLoadout {
        val trait = TraitRegistry.getTraitById(traitId) ?: return this
        if (!trait.removable) return this
        
        return copy(traits = traits - traitId)
    }
    
    fun getAllTraits(): List<Trait> = traits.mapNotNull { TraitRegistry.getTraitById(it) }
}
