package com.popop.lifesimulator.core.events

import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.data.models.character.SkillType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import kotlin.random.Random

/**
 * Base event class for all game events
 */
data class GameEvent(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory,
    val priority: EventPriority = EventPriority.NORMAL,
    
    // Requirements
    val minAge: Int = 0,
    val maxAge: Int = 100,
    val requiredLifePath: String? = null,
    val requiredLocation: String? = null,
    val requiredFaction: String? = null,
    val requiredFlags: Map<String, Any> = emptyMap(),
    val forbiddenFlags: List<String> = emptyList(),
    val statRequirements: Map<String, Int> = emptyMap(),
    val skillRequirements: Map<SkillType, Int> = emptyMap(),
    
    // Content
    val narrativeText: String,
    val choices: List<EventChoice>,
    
    // Chances
    val baseChance: Double = 1.0,
    val cooldown: Long = 0,  // milliseconds
    
    // Metadata
    val tags: List<String> = emptyList(),
    val isRepeatable: Boolean = false,
    val chainId: String? = null,  // For event chains
    val chainPosition: Int = 0
) {
    fun meetsRequirements(character: Character, flags: Map<String, Any>): Boolean {
        // Check age
        if (character.age < minAge || character.age > maxAge) return false
        
        // Check life path
        if (requiredLifePath != null && character.currentLifePath?.name != requiredLifePath) return false
        
        // Check flags
        requiredFlags.forEach { (key, value) ->
            if (flags[key] != value) return false
        }
        
        // Check forbidden flags
        forbiddenFlags.forEach { flag ->
            if (flags.containsKey(flag)) return false
        }
        
        // Check stat requirements
        statRequirements.forEach { (stat, min) ->
            val actual = when (stat) {
                "health" -> character.health
                "energy" -> character.energy
                "stress" -> character.stress
                "charisma" -> character.charisma
                "intellect" -> character.intellect
                "cunning" -> character.cunning
                "violence" -> character.violence
                "stealth" -> character.stealth
                "perception" -> character.perception
                "willpower" -> character.willpower
                "reputation" -> character.reputation
                "wealth" -> character.wealth.toInt()
                else -> 0
            }
            if (actual < min) return false
        }
        
        return true
    }
}

/**
 * Choice within an event
 */
data class EventChoice(
    val id: String,
    val text: String,
    val description: String? = null,
    
    // Requirements
    val skillCheck: SkillCheck? = null,
    val statCheck: StatCheck? = null,
    val cost: Double = 0.0,
    val requiredItem: String? = null,
    
    // Outcomes
    val successOutcome: EventOutcome,
    val failureOutcome: EventOutcome? = null,
    val autoSuccess: Boolean = false,
    
    // Display
    val isHidden: Boolean = false,
    val hiddenCondition: String? = null
)

data class SkillCheck(
    val skill: SkillType,
    val difficulty: Int,  // 0-100
    val bonus: Int = 0
) {
    fun getTargetNumber(): Int = difficulty
}

data class StatCheck(
    val stat: String,
    val difficulty: Int,
    val bonus: Int = 0
)

/**
 * Outcome from an event choice
 */
data class EventOutcome(
    val narrativeText: String,
    val statChanges: Map<String, Int> = emptyMap(),
    val skillXp: Map<SkillType, Double> = emptyMap(),
    val itemChanges: Map<String, Int> = emptyMap(),  // itemId to quantity change
    val relationshipChanges: Map<Long, Map<String, Int>> = emptyMap(),  // npcId to changes
    val factionChanges: Map<Long, Int> = emptyMap(),  // factionId to opinion change
    val flagChanges: Map<String, Any> = emptyMap(),
    val unlockLocation: String? = null,
    val nextEventId: String? = null,  // For chains
    val endsEventChain: Boolean = false
)

enum class EventCategory(val displayName: String) {
    GENERAL("General"),
    ROYALTY("Royalty"),
    POLITICS("Politics"),
    CRIME("Crime"),
    BUSINESS("Business"),
    CAREER("Career"),
    RELATIONSHIP("Relationship"),
    HEALTH("Health"),
    LOCATION("Location"),
    RANDOM("Random")
}

enum class EventPriority(val displayName: String) {
    LOW("Low"),
    NORMAL("Normal"),
    HIGH("High"),
    CRITICAL("Critical")
}

/**
 * Random event generator
 */
class RandomEventGenerator {
    
    private val eventRegistry = mutableMapOf<String, GameEvent>()
    private val eventCooldowns = mutableMapOf<String, Long>()
    private val completedEvents = mutableSetOf<String>()
    
    private val _activeEvents = MutableStateFlow<List<ActiveEvent>>(emptyList())
    val activeEvents: StateFlow<List<ActiveEvent>> = _activeEvents.asStateFlow()
    
    /**
     * Register an event
     */
    fun registerEvent(event: GameEvent) {
        eventRegistry[event.id] = event
    }
    
    /**
     * Register multiple events
     */
    fun registerEvents(events: List<GameEvent>) {
        events.forEach { registerEvent(it) }
    }
    
    /**
     * Generate a random event based on context
     */
    fun generateEvent(
        character: Character,
        flags: Map<String, Any>,
        location: String? = null,
        lifePath: String? = null
    ): GameEvent? {
        val eligibleEvents = eventRegistry.values.filter { event ->
            // Check if event is on cooldown
            val cooldownEnd = eventCooldowns[event.id]
            if (cooldownEnd != null && System.currentTimeMillis() < cooldownEnd) {
                return@filter false
            }
            
            // Check if non-repeatable event was already completed
            if (!event.isRepeatable && completedEvents.contains(event.id)) {
                return@filter false
            }
            
            // Check requirements
            if (!event.meetsRequirements(character, flags)) {
                return@filter false
            }
            
            // Check location filter
            if (location != null && event.requiredLocation != location) {
                return@filter false
            }
            
            // Check life path filter
            if (lifePath != null && event.requiredLifePath != lifePath) {
                return@filter false
            }
            
            // Roll for chance
            Random.nextDouble() <= event.baseChance
        }
        
        if (eligibleEvents.isEmpty()) return null
        
        // Weight by priority
        val weightedEvents = eligibleEvents.flatMap { event ->
            val weight = when (event.priority) {
                EventPriority.LOW -> 1
                EventPriority.NORMAL -> 3
                EventPriority.HIGH -> 5
                EventPriority.CRITICAL -> 10
            }
            List(weight) { event }
        }
        
        return weightedEvents.randomOrNull()
    }
    
    /**
     * Start an active event
     */
    fun startActiveEvent(event: GameEvent, characterId: Long): ActiveEvent {
        val activeEvent = ActiveEvent(
            id = "${event.id}_${System.currentTimeMillis()}",
            eventId = event.id,
            characterId = characterId,
            currentChoiceIndex = 0,
            startedAt = System.currentTimeMillis()
        )
        
        _activeEvents.value = _activeEvents.value + activeEvent
        return activeEvent
    }
    
    /**
     * Complete an event
     */
    fun completeEvent(eventId: String, isRepeatable: Boolean = false) {
        // Set cooldown
        val event = eventRegistry[eventId]
        if (event != null && event.cooldown > 0) {
            eventCooldowns[eventId] = System.currentTimeMillis() + event.cooldown
        }
        
        // Mark as completed if not repeatable
        if (!isRepeatable) {
            completedEvents.add(eventId)
        }
    }
    
    /**
     * Remove active event
     */
    fun removeActiveEvent(activeEventId: String) {
        _activeEvents.value = _activeEvents.value.filter { it.id != activeEventId }
    }
    
    /**
     * Get event by ID
     */
    fun getEvent(eventId: String): GameEvent? = eventRegistry[eventId]
    
    /**
     * Check if event is available
     */
    fun isEventAvailable(eventId: String): Boolean {
        val cooldownEnd = eventCooldowns[eventId]
        return cooldownEnd == null || System.currentTimeMillis() >= cooldownEnd
    }
    
    /**
     * Reset cooldowns (for testing)
     */
    fun resetCooldowns() {
        eventCooldowns.clear()
    }
    
    /**
     * Clear completed events (for testing)
     */
    fun clearCompletedEvents() {
        completedEvents.clear()
    }
}

/**
 * Active event instance
 */
data class ActiveEvent(
    val id: String,
    val eventId: String,
    val characterId: Long,
    val currentChoiceIndex: Int,
    val selectedChoiceId: String? = null,
    val temporaryFlags: Map<String, Any> = emptyMap(),
    val startedAt: Long,
    val expiresAt: Long? = null
) {
    fun isExpired(): Boolean = expiresAt != null && System.currentTimeMillis() > expiresAt!!
}

/**
 * Event chain for multi-part storylines
 */
data class EventChain(
    val id: String,
    val name: String,
    val eventIds: List<String>,
    val currentEventIndex: Int = 0,
    val isCompleted: Boolean = false,
    val branchChoices: Map<String, String> = emptyMap()  // choiceId to next chain ID
)

/**
 * Predefined event templates
 */
object EventTemplates {
    
    // General Events
    val FOUND_MONEY = GameEvent(
        id = "found_money",
        title = "Found Money",
        description = "You spot something on the ground",
        category = EventCategory.GENERAL,
        narrativeText = "While walking down the street, you notice something glinting on the pavement. It's a wallet!",
        choices = listOf(
            EventChoice(
                id = "keep",
                text = "Keep the money",
                cost = 0.0,
                successOutcome = EventOutcome(
                    narrativeText = "You pocket the wallet. There's \$500 inside. Easy money!",
                    statChanges = mapOf("wealth" to 500, "stress" to 10),
                    flagChanges = mapOf("found_wallet_kept" to true)
                )
            ),
            EventChoice(
                id = "return",
                text = "Return to owner",
                cost = 0.0,
                successOutcome = EventOutcome(
                    narrativeText = "You track down the owner using the ID in the wallet. They're grateful and give you \$100 as a reward.",
                    statChanges = mapOf("wealth" to 100, "reputation" to 20),
                    flagChanges = mapOf("found_wallet_returned" to true)
                )
            ),
            EventChoice(
                id = "donate",
                text = "Donate to charity",
                cost = 0.0,
                successOutcome = EventOutcome(
                    narrativeText = "You donate the money to a local charity. It feels good to help others.",
                    statChanges = mapOf("stress" to -10),
                    flagChanges = mapOf("found_wallet_donated" to true)
                )
            )
        ),
        baseChance = 0.05,
        cooldown = 86400000 * 7  // 7 days
    )
    
    val STRANGE_OFFER = GameEvent(
        id = "strange_offer",
        title = "A Strange Offer",
        description = "Someone approaches you with a proposition",
        category = EventCategory.GENERAL,
        narrativeText = "A well-dressed stranger approaches you in a cafe. 'I've been watching you,' they say. 'I think you could be useful to me.'",
        choices = listOf(
            EventChoice(
                id = "listen",
                text = "Hear them out",
                skillCheck = SkillCheck(SkillType.PERCEPTION, 40),
                successOutcome = EventOutcome(
                    narrativeText = "They offer you a job that pays \$5000, but it seems... questionable. You sense there's more to this story.",
                    statChanges = mapOf("wealth" to 5000),
                    flagChanges = mapOf("strange_offer_accepted" to true)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "They try to explain, but you can't shake the feeling something is off. You decline.",
                    flagChanges = mapOf("strange_offer_declined" to true)
                )
            ),
            EventChoice(
                id = "reject",
                text = "Walk away",
                successOutcome = EventOutcome(
                    narrativeText = "You politely decline and leave. Better safe than sorry.",
                    statChanges = mapOf("stress" to -5)
                )
            )
        ),
        baseChance = 0.03,
        cooldown = 86400000 * 14  // 14 days
    )
    
    // Health Events
    val SUDDEN_ILLNESS = GameEvent(
        id = "sudden_illness",
        title = "Feeling Under the Weather",
        description = "You don't feel well",
        category = EventCategory.HEALTH,
        narrativeText = "You wake up feeling terrible. Your head is pounding and your body aches.",
        choices = listOf(
            EventChoice(
                id = "rest",
                text = "Rest at home",
                successOutcome = EventOutcome(
                    narrativeText = "You spend the day resting. You start to feel better.",
                    statChanges = mapOf("health" to 10, "energy" to 20, "stress" to -10)
                )
            ),
            EventChoice(
                id = "doctor",
                text = "Visit doctor (-\$200)",
                cost = 200.0,
                successOutcome = EventOutcome(
                    narrativeText = "The doctor diagnoses you with a viral infection and prescribes medication. You'll recover soon.",
                    statChanges = mapOf("health" to 25, "wealth" to -200)
                )
            ),
            EventChoice(
                id = "push_through",
                text = "Push through it",
                skillCheck = SkillCheck(SkillType.FITNESS, 50),
                successOutcome = EventOutcome(
                    narrativeText = "You manage to power through the day, but it takes a toll.",
                    statChanges = mapOf("health" to -5, "energy" to -20)
                ),
                failureOutcome = EventOutcome(
                    narrativeText = "You collapse from exhaustion. This was a bad idea.",
                    statChanges = mapOf("health" to -20, "energy" to -40)
                )
            )
        ),
        baseChance = 0.02,
        cooldown = 86400000 * 30  // 30 days
    )
    
    val ALL_EVENTS = listOf(
        FOUND_MONEY, STRANGE_OFFER, SUDDEN_ILLNESS
    )
}
