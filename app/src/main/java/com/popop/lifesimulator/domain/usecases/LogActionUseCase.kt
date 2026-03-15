package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.domain.repositories.LogRepository
import com.popop.lifesimulator.core.utils.GameLogger

/**
 * Use case for logging player actions
 */
class LogActionUseCase(
    private val logRepository: LogRepository,
    private val gameLogger: GameLogger
) {
    suspend fun execute(playerId: Long, action: String) {
        gameLogger.log(action)
    }
}
