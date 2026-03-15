package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.GameStateDao
import com.popop.lifesimulator.data.models.world.GameState
import kotlinx.coroutines.flow.Flow

/**
 * Repository for game state data
 */
class GameStateRepository(private val gameStateDao: GameStateDao) {
    val gameState: Flow<GameState> = gameStateDao.getGameState()

    suspend fun getGameState(): GameState? = gameStateDao.getGameStateValue()

    suspend fun updateGameState(gameState: GameState) = gameStateDao.update(gameState)
}
