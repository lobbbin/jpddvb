package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.core.utils.RandomEventGenerator
import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.core.events.GameEvent

/**
 * Use case for generating a random event
 */
class GenerateRandomEventUseCase(private val randomEventGenerator: RandomEventGenerator) {
    fun execute(character: Character, flags: Map<String, Any>): GameEvent? {
        return randomEventGenerator.generateEvent(character, flags)
    }
}
