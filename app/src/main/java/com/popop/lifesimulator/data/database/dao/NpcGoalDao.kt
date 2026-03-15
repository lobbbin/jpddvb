package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.NpcGoalEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for NPC Goal entities.
 */
@Dao
interface NpcGoalDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: NpcGoalEntity)
    
    @Update
    suspend fun update(goal: NpcGoalEntity)
    
    @Delete
    suspend fun delete(goal: NpcGoalEntity)
    
    @Query("SELECT * FROM npc_goals WHERE id = :goalId")
    suspend fun getById(goalId: String): NpcGoalEntity?
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId ORDER BY priority DESC")
    suspend fun getByNpc(npcId: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId ORDER BY priority DESC")
    fun getByNpcFlow(npcId: String): Flow<List<NpcGoalEntity>>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId AND status = :status")
    suspend fun getByStatus(npcId: String, status: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId AND status = 'active'")
    suspend fun getActiveGoals(npcId: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId AND goalType = :type")
    suspend fun getByType(npcId: String, type: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId AND priority >= :minPriority")
    suspend fun getHighPriorityGoals(npcId: String, minPriority: Int): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE npcId = :npcId AND isObsessive = 1")
    suspend fun getObsessiveGoals(npcId: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE relatedFactionId = :factionId")
    suspend fun getByFaction(factionId: String): List<NpcGoalEntity>
    
    @Query("SELECT * FROM npc_goals WHERE relatedQuestId = :questId")
    suspend fun getByQuest(questId: String): List<NpcGoalEntity>
    
    @Query("UPDATE npc_goals SET status = :status WHERE id = :goalId")
    suspend fun updateStatus(goalId: String, status: String)
    
    @Query("UPDATE npc_goals SET progress = :progress WHERE id = :goalId")
    suspend fun updateProgress(goalId: String, progress: Int)
    
    @Query("UPDATE npc_goals SET currentStep = :step WHERE id = :goalId")
    suspend fun updateCurrentStep(goalId: String, step: Int)
    
    @Query("UPDATE npc_goals SET lastActionTime = :timestamp WHERE id = :goalId")
    suspend fun updateLastActionTime(goalId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE npc_goals SET completedAt = :timestamp WHERE id = :goalId")
    suspend fun markCompleted(goalId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM npc_goals WHERE id = :goalId")
    suspend fun deleteById(goalId: String)
    
    @Query("DELETE FROM npc_goals WHERE npcId = :npcId")
    suspend fun deleteAllForNpc(npcId: String)
}
