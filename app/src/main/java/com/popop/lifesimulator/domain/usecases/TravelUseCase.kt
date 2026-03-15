package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.domain.repositories.LocationRepository
import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for traveling to a location
 */
class TravelUseCase(
    private val locationRepository: LocationRepository,
    private val playerRepository: PlayerRepository
) {
    suspend fun execute(playerId: Long, locationId: Long) {
        // Implementation for traveling
    }
}
