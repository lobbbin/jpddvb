package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.core.utils.TimeManager
import com.popop.lifesimulator.domain.repositories.PlayerRepository

/**
 * Use case for advancing game time
 */
class AdvanceTimeUseCase(
    private val timeManager: TimeManager,
    private val playerRepository: PlayerRepository
) {
    suspend fun execute(days: Int = 1) {
        repeat(days) {
            timeManager.advanceDay()
        }
    }
}
