package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.GameStateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Game State entities.
 */
@Dao
interface GameStateDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameState: GameStateEntity)
    
    @Update
    suspend fun update(gameState: GameStateEntity)
    
    @Delete
    suspend fun delete(gameState: GameStateEntity)
    
    @Query("SELECT * FROM game_states WHERE id = :stateId")
    suspend fun getById(stateId: String): GameStateEntity?
    
    @Query("SELECT * FROM game_states WHERE saveSlot = :slot")
    suspend fun getBySlot(slot: Int): GameStateEntity?
    
    @Query("SELECT * FROM game_states WHERE playerId = :playerId ORDER BY lastPlayedAt DESC")
    suspend fun getAllForPlayer(playerId: String): List<GameStateEntity>
    
    @Query("SELECT * FROM game_states ORDER BY lastPlayedAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 10): List<GameStateEntity>
    
    @Query("SELECT * FROM game_states WHERE saveSlot IN (1, 2, 3)")
    suspend fun getQuickSaveSlots(): List<GameStateEntity>
    
    @Query("SELECT EXISTS(SELECT 1 FROM game_states WHERE saveSlot = :slot)")
    suspend fun hasSaveInSlot(slot: Int): Boolean
    
    @Query("UPDATE game_states SET lastPlayedAt = :timestamp WHERE id = :stateId")
    suspend fun updateLastPlayed(stateId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE game_states SET lastUpdatedAt = :timestamp WHERE id = :stateId")
    suspend fun updateTimestamp(stateId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE game_states SET totalPlayTime = :playTime WHERE id = :stateId")
    suspend fun updatePlayTime(stateId: String, playTime: Long)
    
    @Query("DELETE FROM game_states WHERE id = :stateId")
    suspend fun deleteById(stateId: String)
    
    @Query("DELETE FROM game_states WHERE saveSlot = :slot")
    suspend fun deleteBySlot(slot: Int)
    
    @Query("DELETE FROM game_states WHERE playerId = :playerId")
    suspend fun deleteAllForPlayer(playerId: String)
    
    @Query("SELECT COUNT(*) FROM game_states")
    suspend fun getCount(): Int
}
