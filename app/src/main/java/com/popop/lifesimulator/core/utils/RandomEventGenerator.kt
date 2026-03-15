package com.popop.lifesimulator.core.utils

import kotlin.random.Random

/**
 * Generates random events based on context, probability, and game state.
 * Supports weighted random selection and conditional event triggering.
 */
class RandomEventGenerator {
    
    // Event pools categorized by type
    private val eventPools = mutableMapOf<String, MutableList<GameEvent>>()
    
    // Active modifiers affecting event probabilities
    private val probabilityModifiers = mutableMapOf<String, Float>()
    
    // Event history for avoiding repeats
    private val recentEvents = mutableListOf<String>()
    private val maxHistorySize = 20
    
    // Random seed for reproducibility (useful for testing)
    private var randomSeed: Long? = null
    private val random: Random
        get() = randomSeed?.let { Random(it) } ?: Random
    
    init {
        initializeDefaultEvents()
    }
    
    /**
     * Initialize with default event pools.
     */
    private fun initializeDefaultEvents() {
        // General events
        addEventPool("general", listOf(
            GameEvent("found_money", "Found Money", "You found some money on the ground.", 
                category = EventCategory.GENERAL, rarity = EventRarity.COMMON),
            GameEvent("stranger_greeting", "Friendly Stranger", "A stranger greets you warmly.",
                category = EventCategory.SOCIAL, rarity = EventRarity.COMMON),
            GameEvent("weather_change", "Weather Change", "The weather suddenly changes.",
                category = EventCategory.ENVIRONMENT, rarity = EventRarity.COMMON)
        ))
        
        // Opportunity events
        addEventPool("opportunity", listOf(
            GameEvent("job_offer", "Job Offer", "You receive an interesting job offer.",
                category = EventCategory.CAREER, rarity = EventRarity.UNCOMMON),
            GameEvent("investment_chance", "Investment Opportunity", "A chance to invest arises.",
                category = EventCategory.BUSINESS, rarity = EventRarity.UNCOMMON),
            GameEvent("rare_item", "Rare Item Available", "A rare item is for sale.",
                category = EventCategory.COMMERCE, rarity = EventRarity.RARE)
        ))
        
        // Danger events
        addEventPool("danger", listOf(
            GameEvent("mugging_attempt", "Mugging Attempt", "Someone tries to rob you.",
                category = EventCategory.DANGER, rarity = EventRarity.UNCOMMON),
            GameEvent("accident", "Accident", "You have a minor accident.",
                category = EventCategory.DANGER, rarity = EventRarity.UNCOMMON),
            GameEvent("scam_attempt", "Scam Attempt", "Someone tries to scam you.",
                category = EventCategory.DANGER, rarity = EventRarity.COMMON)
        ))
        
        // Social events
        addEventPool("social", listOf(
            GameEvent("party_invitation", "Party Invitation", "You're invited to a party.",
                category = EventCategory.SOCIAL, rarity = EventRarity.UNCOMMON),
            GameEvent("gossip_heard", "Interesting Gossip", "You hear interesting gossip.",
                category = EventCategory.SOCIAL, rarity = EventRarity.COMMON),
            GameEvent("old_friend", "Old Friend", "You run into an old friend.",
                category = EventCategory.SOCIAL, rarity = EventRarity.RARE)
        ))
        
        // Crime events (for criminal players)
        addEventPool("crime", listOf(
            GameEvent("criminal_contact", "Criminal Contact", "A criminal contacts you.",
                category = EventCategory.CRIME, rarity = EventRarity.UNCOMMON),
            GameEvent("illegal_opportunity", "Illegal Opportunity", "An illegal job is offered.",
                category = EventCategory.CRIME, rarity = EventRarity.UNCOMMON),
            GameEvent("police_suspicion", "Police Suspicion", "Police are watching you.",
                category = EventCategory.CRIME, rarity = EventRarity.RARE)
        ))
        
        // Political events (for political players)
        addEventPool("political", listOf(
            GameEvent("political_scandal", "Political Scandal", "A scandal rocks the government.",
                category = EventCategory.POLITICAL, rarity = EventRarity.RARE),
            GameEvent("faction_conflict", "Faction Conflict", "Two factions clash.",
                category = EventCategory.POLITICAL, rarity = EventRarity.UNCOMMON),
            GameEvent("election_announcement", "Election Announced", "An election is coming.",
                category = EventCategory.POLITICAL, rarity = EventRarity.RARE)
        ))
        
        // Royal events (for royal players)
        addEventPool("royal", listOf(
            GameEvent("court_intrigue", "Court Intrigue", "Intrigue at the royal court.",
                category = EventCategory.ROYAL, rarity = EventRarity.UNCOMMON),
            GameEvent("diplomatic_visit", "Diplomatic Visit", "Foreign diplomats arrive.",
                category = EventCategory.ROYAL, rarity = EventRarity.RARE),
            GameEvent("royal_decree", "Royal Decree", "A new decree is issued.",
                category = EventCategory.ROYAL, rarity = EventRarity.UNCOMMON)
        ))
    }
    
    /**
     * Add events to a pool.
     */
    fun addEventPool(poolName: String, events: List<GameEvent>) {
        eventPools.getOrPut(poolName) { mutableListOf() }.addAll(events)
    }
    
    /**
     * Add a single event to a pool.
     */
    fun addEvent(poolName: String, event: GameEvent) {
        eventPools.getOrPut(poolName) { mutableListOf() }.add(event)
    }
    
    /**
     * Set probability modifier for an event category.
     */
    fun setProbabilityModifier(category: String, modifier: Float) {
        probabilityModifiers[category] = modifier.coerceIn(0f, 2f)
    }
    
    /**
     * Clear probability modifier.
     */
    fun clearProbabilityModifier(category: String) {
        probabilityModifiers.remove(category)
    }
    
    /**
     * Generate a random event from specified pools.
     */
    fun generateEvent(
        pools: List<String> = listOf("general"),
        minRarity: EventRarity = EventRarity.COMMON,
        maxRarity: EventRarity = EventRarity.LEGENDARY,
        excludeRecent: Boolean = true
    ): GameEvent? {
        // Gather all eligible events
        val eligibleEvents = mutableListOf<GameEvent>()
        
        for (poolName in pools) {
            val pool = eventPools[poolName] ?: continue
            eligibleEvents.addAll(pool.filter { 
                it.rarity >= minRarity && 
                it.rarity <= maxRarity &&
                (!excludeRecent || it.id !in recentEvents)
            })
        }
        
        if (eligibleEvents.isEmpty()) return null
        
        // Weight by rarity
        val weightedEvents = eligibleEvents.flatMap { event ->
            val weight = when (event.rarity) {
                EventRarity.COMMON -> 100
                EventRarity.UNCOMMON -> 50
                EventRarity.RARE -> 20
                EventRarity.EPIC -> 5
                EventRarity.LEGENDARY -> 1
            }
            List(weight) { event }
        }
        
        if (weightedEvents.isEmpty()) return null
        
        // Select random event
        val selectedEvent = weightedEvents[random.nextInt(weightedEvents.size)]
        
        // Add to recent history
        addToRecent(selectedEvent.id)
        
        return selectedEvent
    }
    
    /**
     * Try to trigger an event based on probability.
     */
    fun tryTriggerEvent(
        baseChance: Float = 0.1f,
        pools: List<String> = listOf("general"),
        context: EventContext = EventContext()
    ): GameEvent? {
        // Calculate final chance with modifiers
        var finalChance = baseChance
        
        for ((category, modifier) in probabilityModifiers) {
            if (context.categories.contains(category)) {
                finalChance *= modifier
            }
        }
        
        // Apply context modifiers
        finalChance *= context.luckModifier
        finalChance = finalChance.coerceIn(0f, 1f)
        
        // Roll for event
        if (random.nextFloat() > finalChance) return null
        
        return generateEvent(pools, excludeRecent = context.excludeRecent)
    }
    
    /**
     * Generate multiple events.
     */
    fun generateEvents(
        count: Int,
        pools: List<String> = listOf("general"),
        minRarity: EventRarity = EventRarity.COMMON
    ): List<GameEvent> {
        return buildList {
            repeat(count) {
                generateEvent(pools, minRarity)?.let { add(it) }
            }
        }
    }
    
    /**
     * Get all events from a pool.
     */
    fun getEventsFromPool(poolName: String): List<GameEvent> {
        return eventPools[poolName]?.toList() ?: emptyList()
    }
    
    /**
     * Set random seed for reproducible results.
     */
    fun setSeed(seed: Long) {
        randomSeed = seed
    }
    
    /**
     * Clear random seed.
     */
    fun clearSeed() {
        randomSeed = null
    }
    
    /**
     * Clear recent event history.
     */
    fun clearRecentEvents() {
        recentEvents.clear()
    }
    
    private fun addToRecent(eventId: String) {
        recentEvents.add(eventId)
        if (recentEvents.size > maxHistorySize) {
            recentEvents.removeAt(0)
        }
    }
}

/**
 * Data class representing a game event.
 */
data class GameEvent(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory = EventCategory.GENERAL,
    val rarity: EventRarity = EventRarity.COMMON,
    val effects: Map<String, Int> = emptyMap(),
    val requirements: Map<String, Any> = emptyMap(),
    val choices: List<EventChoice> = emptyList(),
    val cooldown: Int = 0, // Days before this event can trigger again
    val isRepeatable: Boolean = true
)

/**
 * Choice available in an event.
 */
data class EventChoice(
    val text: String,
    val outcome: EventOutcome,
    val requirements: Map<String, Any> = emptyMap()
)

/**
 * Outcome of an event choice.
 */
data class EventOutcome(
    val message: String,
    val effects: Map<String, Int> = emptyMap(),
    val flags: Map<String, Boolean> = emptyMap()
)

/**
 * Context for event generation.
 */
data class EventContext(
    val categories: Set<String> = emptySet(),
    val location: String? = null,
    val timeOfDay: String? = null,
    val luckModifier: Float = 1.0f,
    val excludeRecent: Boolean = true
)

/**
 * Event categories.
 */
enum class EventCategory {
    GENERAL, SOCIAL, BUSINESS, CRIME, POLITICAL, ROYAL, 
    DANGER, OPPORTUNITY, ROMANCE, CAREER, ENVIRONMENT, COMMERCE
}

/**
 * Event rarity levels.
 */
enum class EventRarity(val weight: Int) {
    COMMON(100),
    UNCOMMON(50),
    RARE(20),
    EPIC(5),
    LEGENDARY(1)
}
