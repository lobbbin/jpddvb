package com.popop.lifesimulator.core.utilities

import com.popop.lifesimulator.data.database.GameLogDao
import com.popop.lifesimulator.data.models.world.GameLog
import com.popop.lifesimulator.data.models.world.LogCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Logger system for tracking player actions and game events
 */
class Logger(
    private val dao: GameLogDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    
    private val _recentLogs = MutableStateFlow<List<GameLog>>(emptyList())
    val recentLogs: StateFlow<List<GameLog>> = _recentLogs.asStateFlow()
    
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()
    
    init {
        loadRecentLogs()
    }
    
    /**
     * Log a general game event
     */
    fun log(
        characterId: Long,
        category: LogCategory,
        title: String,
        message: String,
        detailedMessage: String? = null,
        locationId: Long? = null,
        npcId: Long? = null,
        factionId: Long? = null,
        isImportant: Boolean = false,
        tags: List<String> = emptyList()
    ) {
        val log = GameLog(
            characterId = characterId,
            category = category,
            title = title,
            message = message,
            detailedMessage = detailedMessage,
            locationId = locationId,
            npcId = npcId,
            factionId = factionId,
            isImportant = isImportant,
            tags = tags.joinToString(",")
        )
        
        scope.launch {
            dao.insert(log)
            loadRecentLogs()
        }
    }
    
    /**
     * Log with stat changes
     */
    fun logWithStatChanges(
        characterId: Long,
        category: LogCategory,
        title: String,
        message: String,
        statChanges: Map<String, Int>,
        locationId: Long? = null,
        isImportant: Boolean = false
    ) {
        val json = JSONObject()
        statChanges.forEach { (key, value) ->
            json.put(key, value)
        }
        
        log(
            characterId = characterId,
            category = category,
            title = title,
            message = message,
            locationId = locationId,
            isImportant = isImportant,
            tags = listOf("stat_change")
        )
    }
    
    /**
     * Log with relationship changes
     */
    fun logWithRelationshipChanges(
        characterId: Long,
        category: LogCategory,
        title: String,
        message: String,
        npcId: Long,
        relationshipChanges: Map<String, Int>,
        isImportant: Boolean = false
    ) {
        val json = JSONObject()
        relationshipChanges.forEach { (key, value) ->
            json.put(key, value)
        }
        
        log(
            characterId = characterId,
            category = category,
            title = title,
            message = message,
            npcId = npcId,
            isImportant = isImportant,
            tags = listOf("relationship_change")
        )
    }
    
    /**
     * Log with item changes
     */
    fun logWithItemChanges(
        characterId: Long,
        category: LogCategory,
        title: String,
        message: String,
        itemChanges: Map<String, Any>,
        isImportant: Boolean = false
    ) {
        log(
            characterId = characterId,
            category = category,
            title = title,
            message = message,
            isImportant = isImportant,
            tags = listOf("item_change")
        )
    }
    
    /**
     * Load recent logs
     */
    private fun loadRecentLogs() {
        scope.launch {
            val logs = dao.getRecent(50)
            _recentLogs.value = logs
            _unreadCount.value = logs.count { !it.isRead }
        }
    }
    
    /**
     * Mark log as read
     */
    fun markAsRead(logId: Long) {
        scope.launch {
            val log = dao.getById(logId)
            log?.let {
                dao.update(it.copy(isRead = true))
                loadRecentLogs()
            }
        }
    }
    
    /**
     * Mark all logs as read
     */
    fun markAllAsRead(characterId: Long) {
        scope.launch {
            val logs = dao.getByCharacter(characterId)
            logs.forEach { log ->
                if (!log.isRead) {
                    dao.update(log.copy(isRead = true))
                }
            }
            loadRecentLogs()
        }
    }
    
    /**
     * Get logs by category
     */
    fun getLogsByCategory(category: LogCategory, characterId: Long, callback: (List<GameLog>) -> Unit) {
        scope.launch {
            val logs = dao.getByCategory(category.name)
            callback(logs.filter { it.characterId == characterId })
        }
    }
    
    /**
     * Delete old logs
     */
    fun cleanupOldLogs(daysToKeep: Int = 30) {
        scope.launch {
            val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
            dao.deleteOlderThan(cutoffTime)
            loadRecentLogs()
        }
    }
}

/**
 * Flag manager for tracking game state flags
 */
class FlagManager {
    
    private val flags = mutableMapOf<String, Any>()
    private val _flagChanges = MutableStateFlow<List<FlagChange>>(emptyList())
    val flagChanges: StateFlow<List<FlagChange>> = _flagChanges.asStateFlow()
    
    /**
     * Set a flag value
     */
    fun setFlag(key: String, value: Any) {
        val oldValue = flags[key]
        flags[key] = value
        
        _flagChanges.value = _flagChanges.value + FlagChange(
            key = key,
            oldValue = oldValue,
            newValue = value,
            timestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Get a flag value
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getFlag(key: String, defaultValue: T): T {
        return flags[key] as? T ?: defaultValue
    }
    
    /**
     * Check if flag exists
     */
    fun hasFlag(key: String): Boolean = flags.containsKey(key)
    
    /**
     * Remove a flag
     */
    fun removeFlag(key: String) {
        flags.remove(key)
    }
    
    /**
     * Clear all flags
     */
    fun clearAllFlags() {
        flags.clear()
    }
    
    /**
     * Get all flags as JSON string
     */
    fun toJson(): String {
        val json = JSONObject()
        flags.forEach { (key, value) ->
            json.put(key, value)
        }
        return json.toString()
    }
    
    /**
     * Load flags from JSON string
     */
    fun fromJson(jsonString: String) {
        try {
            val json = JSONObject(jsonString)
            flags.clear()
            json.keys().forEach { key ->
                flags[key] = json.get(key)
            }
        } catch (e: Exception) {
            // Ignore parse errors
        }
    }
    
    /**
     * Set boolean flag convenience method
     */
    fun setBoolean(key: String, value: Boolean) = setFlag(key, value)
    
    /**
     * Get boolean flag convenience method
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = getFlag(key, defaultValue)
    
    /**
     * Set integer flag convenience method
     */
    fun setInt(key: String, value: Int) = setFlag(key, value)
    
    /**
     * Get integer flag convenience method
     */
    fun getInt(key: String, defaultValue: Int = 0): Int = getFlag(key, defaultValue)
    
    /**
     * Increment integer flag
     */
    fun increment(key: String, amount: Int = 1) {
        val currentValue = getInt(key, 0)
        setInt(key, currentValue + amount)
    }
    
    /**
     * Decrement integer flag
     */
    fun decrement(key: String, amount: Int = 1) {
        increment(key, -amount)
    }
}

data class FlagChange(
    val key: String,
    val oldValue: Any?,
    val newValue: Any,
    val timestamp: Long
)

/**
 * Stat modifier system for temporary buffs/debuffs
 */
data class StatModifier(
    val id: String,
    val stat: String,
    val amount: Int,
    val isPercentage: Boolean = false,
    val source: String = "",
    val expiresAt: Long? = null,
    val isPermanent: Boolean = false
) {
    fun isExpired(): Boolean = expiresAt != null && System.currentTimeMillis() > expiresAt!!
    
    fun getRemainingTime(): Long? {
        return expiresAt?.let { maxOf(0, it - System.currentTimeMillis()) }
    }
}

class StatModifierManager {
    
    private val modifiers = mutableListOf<StatModifier>()
    
    /**
     * Add a modifier
     */
    fun addModifier(modifier: StatModifier) {
        // Remove existing modifier with same ID
        modifiers.removeAll { it.id == modifier.id }
        modifiers.add(modifier)
    }
    
    /**
     * Remove a modifier
     */
    fun removeModifier(id: String) {
        modifiers.removeAll { it.id == id }
    }
    
    /**
     * Get all modifiers for a stat
     */
    fun getModifiersForStat(stat: String): List<StatModifier> {
        return modifiers.filter { it.stat == stat && !it.isExpired() }
    }
    
    /**
     * Get total modifier for a stat
     */
    fun getTotalModifier(stat: String, baseValue: Int): Int {
        val activeModifiers = getModifiersForStat(stat)
        var flatModifier = 0
        var percentageMultiplier = 1.0
        
        activeModifiers.forEach { modifier ->
            if (modifier.isPercentage) {
                percentageMultiplier += modifier.amount / 100.0
            } else {
                flatModifier += modifier.amount
            }
        }
        
        return ((baseValue * percentageMultiplier).toInt() + flatModifier).coerceAtLeast(0)
    }
    
    /**
     * Clean up expired modifiers
     */
    fun cleanupExpiredModifiers() {
        modifiers.removeAll { it.isExpired() }
    }
    
    /**
     * Add temporary modifier with duration
     */
    fun addTemporaryModifier(
        id: String,
        stat: String,
        amount: Int,
        durationMs: Long,
        source: String = "",
        isPercentage: Boolean = false
    ) {
        val modifier = StatModifier(
            id = id,
            stat = stat,
            amount = amount,
            isPercentage = isPercentage,
            source = source,
            expiresAt = System.currentTimeMillis() + durationMs
        )
        addModifier(modifier)
    }
    
    /**
     * Add permanent modifier
     */
    fun addPermanentModifier(
        id: String,
        stat: String,
        amount: Int,
        source: String = "",
        isPercentage: Boolean = false
    ) {
        val modifier = StatModifier(
            id = id,
            stat = stat,
            amount = amount,
            isPercentage = isPercentage,
            source = source,
            isPermanent = true
        )
        addModifier(modifier)
    }
}
