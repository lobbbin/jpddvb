package com.popop.lifesimulator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popop.lifesimulator.data.database.CharacterDao
import com.popop.lifesimulator.data.database.GameStateDao
import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.data.models.character.PrimaryStats
import com.popop.lifesimulator.data.models.character.SecondaryStats
import com.popop.lifesimulator.data.models.world.GameState
import com.popop.lifesimulator.core.time.TimeManager
import com.popop.lifesimulator.core.utilities.Logger
import com.popop.lifesimulator.core.utilities.FlagManager
import com.popop.lifesimulator.core.events.RandomEventGenerator
import com.popop.lifesimulator.core.events.EventRegistry
import com.popop.lifesimulator.core.events.ActiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val characterDao: CharacterDao,
    private val gameStateDao: GameStateDao,
    private val timeManager: TimeManager,
    private val logger: Logger,
    private val flagManager: FlagManager,
    private val eventGenerator: RandomEventGenerator
) : ViewModel() {
    
    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character.asStateFlow()
    
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()
    
    private val _activeEvent = MutableStateFlow<ActiveEvent?>(null)
    val activeEvent: StateFlow<ActiveEvent?> = _activeEvent.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var currentCharacterId: Long = 0L
    
    fun loadCharacter(characterId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val char = characterDao.getById(characterId)
                _character.value = char
                currentCharacterId = characterId
                
                // Load game state
                val state = gameStateDao.getCurrent()
                _gameState.value = state
                state?.let { timeManager.initialize(it) }
                
                // Initialize event generator with registered events
                eventGenerator.registerEvents(EventRegistry.ALL_EVENTS)
                
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateCharacterStats(newPrimary: PrimaryStats, newSecondary: SecondaryStats) {
        viewModelScope.launch {
            val currentChar = _character.value ?: return@launch
            val updated = currentChar.updateStats(newPrimary, newSecondary)
            characterDao.update(updated)
            _character.value = updated
        }
    }
    
    fun modifyStat(stat: String, amount: Int) {
        viewModelScope.launch {
            val currentChar = _character.value ?: return@launch
            val primary = currentChar.getPrimaryStats()
            val secondary = currentChar.getSecondaryStats()
            
            val updatedPrimary = when (stat) {
                "health" -> primary.modifyHealth(amount)
                "energy" -> primary.modifyEnergy(amount)
                "stress" -> primary.modifyStress(amount)
                "charisma" -> primary.modifyCharisma(amount)
                "intellect" -> primary.modifyIntellect(amount)
                "cunning" -> primary.modifyCunning(amount)
                "violence" -> primary.modifyViolence(amount)
                "stealth" -> primary.modifyStealth(amount)
                "perception" -> primary.modifyPerception(amount)
                "willpower" -> primary.modifyWillpower(amount)
                else -> primary
            }
            
            val updatedSecondary = when (stat) {
                "reputation" -> secondary.modifyReputation(amount)
                "wealth" -> secondary.modifyWealth(amount.toDouble())
                "piety" -> secondary.modifyPiety(amount)
                "heat" -> secondary.modifyHeat(amount)
                "streetCred" -> secondary.modifyStreetCred(amount)
                else -> secondary
            }
            
            updateCharacterStats(updatedPrimary, updatedSecondary)
            
            // Log the change
            logger.log(
                characterId = currentCharacterId,
                category = com.popop.lifesimulator.data.models.world.LogCategory.GENERAL,
                title = "Stat Change",
                message = "$stat changed by $amount"
            )
        }
    }
    
    fun advanceTime(hours: Int) {
        viewModelScope.launch {
            repeat(hours) {
                timeManager.advanceHour()
            }
            
            // Update game state
            val newState = timeManager.gameState.value
            gameStateDao.update(newState)
            _gameState.value = newState
            
            // Check for random events
            checkForEvents()
            
            // Auto-save every 5 minutes (in game time)
            if (newState.currentMinute % 5 == 0 && newState.currentHour == 0) {
                autoSave()
            }
        }
    }
    
    fun advanceDay() {
        viewModelScope.launch {
            timeManager.advanceDay()
            
            val newState = timeManager.gameState.value
            gameStateDao.update(newState)
            _gameState.value = newState
            
            // Daily recovery
            val currentChar = _character.value ?: return@launch
            val primary = currentChar.getPrimaryStats()
            val recoveredEnergy = primary.modifyEnergy(20)
            val recoveredHealth = if (currentChar.health < 100) {
                primary.modifyHealth(5)
            } else {
                primary
            }
            
            updateCharacterStats(recoveredHealth, currentChar.getSecondaryStats())
            
            checkForEvents()
        }
    }
    
    private fun checkForEvents() {
        viewModelScope.launch {
            val currentChar = _character.value ?: return@launch
            val state = _gameState.value ?: return@launch
            
            val event = eventGenerator.generateEvent(
                character = currentChar,
                flags = emptyMap(),  // Use flagManager.getFlags()
                lifePath = currentChar.currentLifePath?.name
            )
            
            if (event != null) {
                _activeEvent.value = eventGenerator.startActiveEvent(event, currentCharacterId)
            }
        }
    }
    
    fun resolveEventChoice(choiceId: String) {
        viewModelScope.launch {
            val activeEvent = _activeEvent.value ?: return@launch
            val event = eventGenerator.getEvent(activeEvent.eventId) ?: return@launch
            
            val choice = event.choices.find { it.id == choiceId } ?: return@launch
            
            // Apply outcomes
            choice.successOutcome.statChanges.forEach { (stat, amount) ->
                modifyStat(stat, amount)
            }
            
            // Update flags
            choice.successOutcome.flagChanges.forEach { (key, value) ->
                flagManager.setFlag(key, value)
            }
            
            // Complete event
            eventGenerator.completeEvent(event.id, event.isRepeatable)
            eventGenerator.removeActiveEvent(activeEvent.id)
            _activeEvent.value = null
            
            // Log the event
            logger.log(
                characterId = currentCharacterId,
                category = com.popop.lifesimulator.data.models.world.LogCategory.EVENT,
                title = event.title,
                message = choice.text,
                isImportant = event.priority != com.popop.lifesimulator.core.events.EventPriority.NORMAL
            )
        }
    }
    
    fun dismissEvent() {
        viewModelScope.launch {
            val activeEvent = _activeEvent.value ?: return@launch
            eventGenerator.removeActiveEvent(activeEvent.id)
            _activeEvent.value = null
        }
    }
    
    fun autoSave() {
        viewModelScope.launch {
            // Save is handled by Room's automatic persistence
            logger.log(
                characterId = currentCharacterId,
                category = com.popop.lifesimulator.data.models.world.LogCategory.GENERAL,
                title = "Auto-Save",
                message = "Game progress saved automatically"
            )
        }
    }
    
    fun saveGame() {
        viewModelScope.launch {
            autoSave()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        // Save on cleanup
        saveGame()
    }
}
