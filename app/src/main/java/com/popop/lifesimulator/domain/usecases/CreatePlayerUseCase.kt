package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for creating a new player character
 */
class CreatePlayerUseCase(private val repository: PlayerRepository) {
    suspend fun execute(character: Character) {
        repository.insert(character)
    }
}
