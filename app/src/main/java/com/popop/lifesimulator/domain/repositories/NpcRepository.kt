package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.NpcDao
import com.popop.lifesimulator.data.database.dao.RelationshipDao
import com.popop.lifesimulator.data.models.relationship.Npc
import com.popop.lifesimulator.data.models.relationship.Relationship
import kotlinx.coroutines.flow.Flow

/**
 * Repository for NPC and relationship data
 */
class NpcRepository(
    private val npcDao: NpcDao,
    private val relationshipDao: RelationshipDao
) {
    val allNpcs: Flow<List<Npc>> = npcDao.getAll()

    suspend fun getNpcById(id: Long): Npc? = npcDao.getById(id)

    suspend fun insertNpc(npc: Npc) = npcDao.insert(npc)

    suspend fun updateNpc(npc: Npc) = npcDao.update(npc)

    suspend fun deleteNpc(npc: Npc) = npcDao.delete(npc)

    suspend fun getRelationship(playerId: Long, npcId: Long): Relationship? =
        relationshipDao.getByPlayerAndNpc(playerId, npcId)

    suspend fun insertRelationship(relationship: Relationship) =
        relationshipDao.insert(relationship)

    suspend fun updateRelationship(relationship: Relationship) =
        relationshipDao.update(relationship)
}
