package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.relationship.Npc
import kotlinx.coroutines.flow.Flow

@Dao
interface NpcDao {
    
    @Query("SELECT * FROM npcs WHERE id = :id")
    suspend fun getById(id: Long): Npc?
    
    @Query("SELECT * FROM npcs WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<Npc?>
    
    @Query("SELECT * FROM npcs WHERE isAlive = 1")
    suspend fun getAllAlive(): List<Npc>
    
    @Query("SELECT * FROM npcs WHERE factionId = :factionId")
    suspend fun getByFaction(factionId: Long): List<Npc>
    
    @Query("SELECT * FROM npcs WHERE locationId = :locationId")
    suspend fun getByLocation(locationId: Long): List<Npc>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(npc: Npc): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(npcs: List<Npc>)
    
    @Update
    suspend fun update(npc: Npc)
    
    @Delete
    suspend fun delete(npc: Npc)
    
    @Query("UPDATE npcs SET isAlive = 0 WHERE id = :id")
    suspend fun markAsDead(id: Long)
    
    @Query("SELECT * FROM npcs WHERE relationshipWithPlayer IS NOT NULL")
    suspend fun getAllWithPlayerRelationship(): List<Npc>
}
