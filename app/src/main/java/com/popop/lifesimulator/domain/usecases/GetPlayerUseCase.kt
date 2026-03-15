package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for getting a player character by ID
 */
class GetPlayerUseCase(private val repository: PlayerRepository) {
    suspend fun execute(id: Long) = repository.getPlayerById(id)
}
