package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for updating a player character
 */
class UpdatePlayerUseCase(private val repository: PlayerRepository) {
    suspend fun execute(character: Character) {
        repository.update(character)
    }
}
