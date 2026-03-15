package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.GameStateDao
import com.popop.lifesimulator.data.database.entity.GameStateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for game state data
 */
class GameStateRepository(private val gameStateDao: GameStateDao) {
    val gameState: Flow<GameStateEntity?>? = null

    suspend fun getGameState(): GameStateEntity? = gameStateDao.getBySlot(1)

    suspend fun updateGameState(gameState: GameStateEntity) = gameStateDao.insert(gameState)
}
