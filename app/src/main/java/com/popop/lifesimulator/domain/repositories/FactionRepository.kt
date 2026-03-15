package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.FactionDao
import com.popop.lifesimulator.data.models.world.Faction
import kotlinx.coroutines.flow.Flow

/**
 * Repository for faction data
 */
class FactionRepository(private val factionDao: FactionDao) {
    val allFactions: Flow<List<Faction>> = factionDao.getAll()

    suspend fun getFactionById(id: Long): Faction? = factionDao.getById(id)

    suspend fun insert(faction: Faction) = factionDao.insert(faction)

    suspend fun update(faction: Faction) = factionDao.update(faction)
}
