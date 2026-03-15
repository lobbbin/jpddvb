package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.QuestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Quest entities.
 */
@Dao
interface QuestDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quest: QuestEntity)
    
    @Update
    suspend fun update(quest: QuestEntity)
    
    @Delete
    suspend fun delete(quest: QuestEntity)
    
    @Query("SELECT * FROM quests WHERE id = :questId")
    suspend fun getById(questId: String): QuestEntity?
    
    @Query("SELECT * FROM quests WHERE questId = :templateId")
    suspend fun getByTemplateId(templateId: String): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE status = :status")
    suspend fun getByStatus(status: String): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE status = 'active'")
    suspend fun getActiveQuests(): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE status = 'completed'")
    suspend fun getCompletedQuests(): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE giverId = :npcId")
    suspend fun getByGiver(npcId: String): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE giverFactionId = :factionId")
    suspend fun getByFaction(factionId: String): List<QuestEntity>
    
    @Query("SELECT * FROM quests WHERE questType = :type")
    suspend fun getByType(type: String): List<QuestEntity>
    
    @Query("UPDATE quests SET status = :status WHERE id = :questId")
    suspend fun updateStatus(questId: String, status: String)
    
    @Query("UPDATE quests SET progress = :progress WHERE id = :questId")
    suspend fun updateProgress(questId: String, progress: Int)
    
    @Query("UPDATE quests SET objectives = :objectives WHERE id = :questId")
    suspend fun updateObjectives(questId: String, objectives: String)
    
    @Query("UPDATE quests SET completedAt = :timestamp WHERE id = :questId")
    suspend fun markCompleted(questId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE quests SET lastUpdatedAt = :timestamp WHERE id = :questId")
    suspend fun updateTimestamp(questId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM quests WHERE id = :questId")
    suspend fun deleteById(questId: String)
    
    @Query("DELETE FROM quests WHERE status = :status")
    suspend fun deleteByStatus(status: String)
}
