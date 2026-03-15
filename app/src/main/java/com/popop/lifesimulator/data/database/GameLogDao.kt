package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.world.GameLog
import kotlinx.coroutines.flow.Flow

@Dao
interface GameLogDao {
    
    @Query("SELECT * FROM gamelog WHERE id = :id")
    suspend fun getById(id: Long): GameLog?
    
    @Query("SELECT * FROM gamelog ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 50): List<GameLog>
    
    @Query("SELECT * FROM gamelog ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentFlow(limit: Int = 50): Flow<List<GameLog>>
    
    @Query("SELECT * FROM gamelog WHERE category = :category ORDER BY timestamp DESC")
    suspend fun getByCategory(category: String): List<GameLog>
    
    @Query("SELECT * FROM gamelog WHERE characterId = :characterId ORDER BY timestamp DESC")
    suspend fun getByCharacter(characterId: Long): List<GameLog>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: GameLog): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<GameLog>)
    
    @Delete
    suspend fun delete(log: GameLog)
    
    @Query("DELETE FROM gamelog WHERE characterId = :characterId")
    suspend fun deleteByCharacter(characterId: Long)
    
    @Query("DELETE FROM gamelog WHERE timestamp < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)
}
