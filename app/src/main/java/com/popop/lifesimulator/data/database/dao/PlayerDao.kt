package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Player entities.
 * Provides CRUD operations and queries for player data.
 */
@Dao
interface PlayerDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: PlayerEntity)
    
    @Update
    suspend fun update(player: PlayerEntity)
    
    @Delete
    suspend fun delete(player: PlayerEntity)
    
    @Query("SELECT * FROM players WHERE id = :playerId")
    suspend fun getById(playerId: String): PlayerEntity?
    
    @Query("SELECT * FROM players WHERE id = :playerId")
    fun getByIdFlow(playerId: String): Flow<PlayerEntity?>
    
    @Query("SELECT * FROM players LIMIT 1")
    suspend fun getCurrentPlayer(): PlayerEntity?
    
    @Query("SELECT * FROM players LIMIT 1")
    fun getCurrentPlayerFlow(): Flow<PlayerEntity?>
    
    @Query("SELECT EXISTS(SELECT 1 FROM players LIMIT 1)")
    suspend fun hasPlayer(): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM players LIMIT 1)")
    fun hasPlayerFlow(): Flow<Boolean>
    
    @Query("UPDATE players SET health = :health WHERE id = :playerId")
    suspend fun updateHealth(playerId: String, health: Int)
    
    @Query("UPDATE players SET wealth = :wealth WHERE id = :playerId")
    suspend fun updateWealth(playerId: String, wealth: Long)
    
    @Query("UPDATE players SET reputation = :reputation WHERE id = :playerId")
    suspend fun updateReputation(playerId: String, reputation: Int)
    
    @Query("UPDATE players SET currentLocationId = :locationId WHERE id = :playerId")
    suspend fun updateLocation(playerId: String, locationId: String?)
    
    @Query("UPDATE players SET factionId = :factionId, factionRank = :rank WHERE id = :playerId")
    suspend fun updateFaction(playerId: String, factionId: String?, rank: Int)
    
    @Query("UPDATE players SET lastUpdatedAt = :timestamp WHERE id = :playerId")
    suspend fun updateTimestamp(playerId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM players")
    suspend fun deleteAll()
}
