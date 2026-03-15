package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.QuestDao
import com.popop.lifesimulator.data.database.entity.QuestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for quest data
 */
class QuestRepository(private val questDao: QuestDao) {
    val allQuests: Flow<List<QuestEntity>>? = null

    suspend fun getQuestById(id: String): QuestEntity? = questDao.getById(id)

    suspend fun insert(quest: QuestEntity) = questDao.insert(quest)

    suspend fun update(quest: QuestEntity) = questDao.update(quest)

    suspend fun delete(quest: QuestEntity) = questDao.delete(quest)
}
