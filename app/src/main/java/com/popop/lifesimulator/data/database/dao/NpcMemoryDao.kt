package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.NpcMemoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for NPC Memory entities.
 */
@Dao
interface NpcMemoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memory: NpcMemoryEntity)
    
    @Update
    suspend fun update(memory: NpcMemoryEntity)
    
    @Delete
    suspend fun delete(memory: NpcMemoryEntity)
    
    @Query("SELECT * FROM npc_memories WHERE id = :memoryId")
    suspend fun getById(memoryId: String): NpcMemoryEntity?
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId ORDER BY createdAt DESC")
    suspend fun getByNpc(npcId: String): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId ORDER BY createdAt DESC")
    fun getByNpcFlow(npcId: String): Flow<List<NpcMemoryEntity>>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND targetId = :targetId")
    suspend fun getByNpcAndTarget(npcId: String, targetId: String?): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND memoryType = :type")
    suspend fun getByType(npcId: String, type: String): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND category = :category")
    suspend fun getByCategory(npcId: String, category: String): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND isSecret = 1")
    suspend fun getSecrets(npcId: String): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND isTraumatic = 1")
    suspend fun getTraumaticMemories(npcId: String): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND (expiresAt IS NULL OR expiresAt > :currentTime)")
    suspend fun getActiveMemories(npcId: String, currentTime: Long = System.currentTimeMillis()): List<NpcMemoryEntity>
    
    @Query("SELECT * FROM npc_memories WHERE npcId = :npcId AND emotionalImpact >= :minImpact")
    suspend fun getSignificantMemories(npcId: String, minImpact: Int): List<NpcMemoryEntity>
    
    @Query("UPDATE npc_memories SET lastAccessedAt = :timestamp WHERE id = :memoryId")
    suspend fun updateLastAccessed(memoryId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM npc_memories WHERE id = :memoryId")
    suspend fun deleteById(memoryId: String)
    
    @Query("DELETE FROM npc_memories WHERE npcId = :npcId")
    suspend fun deleteAllForNpc(npcId: String)
    
    @Query("DELETE FROM npc_memories WHERE expiresAt IS NOT NULL AND expiresAt <= :currentTime")
    suspend fun deleteExpired(currentTime: Long = System.currentTimeMillis())
}
