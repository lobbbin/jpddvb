package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.PlayerDao
import com.popop.lifesimulator.data.models.character.Character
import kotlinx.coroutines.flow.Flow

/**
 * Repository for player character data
 */
class PlayerRepository(private val playerDao: PlayerDao) {
    val allPlayers: Flow<List<Character>> = playerDao.getAll()

    suspend fun getPlayerById(id: Long): Character? = playerDao.getById(id)

    suspend fun insert(character: Character) = playerDao.insert(character)

    suspend fun update(character: Character) = playerDao.update(character)

    suspend fun delete(character: Character) = playerDao.delete(character)

    suspend fun getCurrentPlayer(): Character? = playerDao.getCurrent()
}
