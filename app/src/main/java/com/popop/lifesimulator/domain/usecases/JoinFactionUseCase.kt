package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.domain.repositories.FactionRepository
import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for joining a faction
 */
class JoinFactionUseCase(
    private val factionRepository: FactionRepository,
    private val playerRepository: PlayerRepository
) {
    suspend fun execute(playerId: Long, factionId: Long) {
        // Implementation for joining faction
    }
}
