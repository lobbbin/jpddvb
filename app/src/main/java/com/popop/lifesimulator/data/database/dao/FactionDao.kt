package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.FactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Faction entities.
 */
@Dao
interface FactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(faction: FactionEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(faction: FactionEntity): Long
    
    @Update
    suspend fun update(faction: FactionEntity)
    
    @Delete
    suspend fun delete(faction: FactionEntity)
    
    @Query("SELECT * FROM factions WHERE id = :factionId")
    suspend fun getById(factionId: String): FactionEntity?
    
    @Query("SELECT * FROM factions WHERE id = :factionId")
    fun getByIdFlow(factionId: String): Flow<FactionEntity?>
    
    @Query("SELECT * FROM factions WHERE factionType = :type")
    suspend fun getByType(type: String): List<FactionEntity>
    
    @Query("SELECT * FROM factions WHERE isActive = 1")
    suspend fun getAllActive(): List<FactionEntity>
    
    @Query("SELECT * FROM factions WHERE legality = :legality")
    suspend fun getByLegality(legality: String): List<FactionEntity>
    
    @Query("SELECT * FROM factions WHERE isPlayerMember = 1")
    suspend fun getPlayerFactions(): List<FactionEntity>
    
    @Query("SELECT * FROM factions WHERE leaderId = :npcId")
    suspend fun getLedBy(npcId: String): List<FactionEntity>
    
    @Query("SELECT * FROM factions WHERE name LIKE :query || '%' LIMIT :limit")
    suspend fun searchByName(query: String, limit: Int = 10): List<FactionEntity>
    
    @Query("SELECT * FROM factions ORDER BY power DESC LIMIT :limit")
    suspend fun getMostPowerful(limit: Int = 10): List<FactionEntity>
    
    @Query("UPDATE factions SET power = :power WHERE id = :factionId")
    suspend fun updatePower(factionId: String, power: Int)
    
    @Query("UPDATE factions SET influence = :influence WHERE id = :factionId")
    suspend fun updateInfluence(factionId: String, influence: Int)
    
    @Query("UPDATE factions SET isPlayerMember = :isMember, playerRank = :rank, playerJoinDate = :joinDate WHERE id = :factionId")
    suspend fun updatePlayerMembership(factionId: String, isMember: Boolean, rank: Int, joinDate: Long?)
    
    @Query("UPDATE factions SET factionRelations = :relations WHERE id = :factionId")
    suspend fun updateRelations(factionId: String, relations: String)
    
    @Query("UPDATE factions SET lastUpdatedAt = :timestamp WHERE id = :factionId")
    suspend fun updateTimestamp(factionId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM factions WHERE id = :factionId")
    suspend fun deleteById(factionId: String)
    
    @Query("DELETE FROM factions")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM factions")
    suspend fun getCount(): Int
}
