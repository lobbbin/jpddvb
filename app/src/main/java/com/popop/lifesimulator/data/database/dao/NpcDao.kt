package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.NpcEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for NPC entities.
 */
@Dao
interface NpcDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(npc: NpcEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(npc: NpcEntity): Long
    
    @Update
    suspend fun update(npc: NpcEntity)
    
    @Delete
    suspend fun delete(npc: NpcEntity)
    
    @Query("SELECT * FROM npcs WHERE id = :npcId")
    suspend fun getById(npcId: String): NpcEntity?
    
    @Query("SELECT * FROM npcs WHERE id = :npcId")
    fun getByIdFlow(npcId: String): Flow<NpcEntity?>
    
    @Query("SELECT * FROM npcs WHERE factionId = :factionId")
    suspend fun getByFaction(factionId: String): List<NpcEntity>
    
    @Query("SELECT * FROM npcs WHERE currentLocationId = :locationId")
    suspend fun getByLocation(locationId: String): List<NpcEntity>
    
    @Query("SELECT * FROM npcs WHERE isAlive = 1")
    suspend fun getAllAlive(): List<NpcEntity>
    
    @Query("SELECT * FROM npcs WHERE name LIKE :query || '%' LIMIT :limit")
    suspend fun searchByName(query: String, limit: Int = 10): List<NpcEntity>
    
    @Query("SELECT * FROM npcs WHERE occupation = :occupation")
    suspend fun getByOccupation(occupation: String): List<NpcEntity>
    
    @Query("SELECT * FROM npcs WHERE socialClass = :socialClass")
    suspend fun getBySocialClass(socialClass: String): List<NpcEntity>
    
    @Query("UPDATE npcs SET health = :health WHERE id = :npcId")
    suspend fun updateHealth(npcId: String, health: Int)
    
    @Query("UPDATE npcs SET currentLocationId = :locationId WHERE id = :npcId")
    suspend fun updateLocation(npcId: String, locationId: String?)
    
    @Query("UPDATE npcs SET isAlive = :isAlive WHERE id = :npcId")
    suspend fun updateAliveStatus(npcId: String, isAlive: Boolean)
    
    @Query("UPDATE npcs SET lastUpdatedAt = :timestamp WHERE id = :npcId")
    suspend fun updateTimestamp(npcId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM npcs WHERE id = :npcId")
    suspend fun deleteById(npcId: String)
    
    @Query("DELETE FROM npcs")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM npcs")
    suspend fun getCount(): Int
}
