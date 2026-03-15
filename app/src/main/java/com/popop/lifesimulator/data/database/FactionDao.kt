package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.world.Faction
import kotlinx.coroutines.flow.Flow

@Dao
interface FactionDao {
    
    @Query("SELECT * FROM factions WHERE id = :id")
    suspend fun getById(id: Long): Faction?
    
    @Query("SELECT * FROM factions WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<Faction?>
    
    @Query("SELECT * FROM factions WHERE category = :category")
    suspend fun getByCategory(category: String): List<Faction>
    
    @Query("SELECT * FROM factions WHERE isUnlocked = 1")
    suspend fun getAllUnlocked(): List<Faction>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(faction: Faction): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(factions: List<Faction>)
    
    @Update
    suspend fun update(faction: Faction)
    
    @Delete
    suspend fun delete(faction: Faction)
    
    @Query("UPDATE factions SET opinionOfPlayer = :opinion WHERE id = :id")
    suspend fun updateOpinion(id: Long, opinion: Int)
    
    @Query("UPDATE factions SET powerLevel = :power WHERE id = :id")
    suspend fun updatePower(id: Long, power: Int)
}
