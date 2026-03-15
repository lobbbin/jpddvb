package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.character.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    
    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Long): Character?
    
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<Character?>
    
    @Query("SELECT * FROM characters WHERE isAlive = 1")
    suspend fun getAllAlive(): List<Character>
    
    @Query("SELECT * FROM characters WHERE isAlive = 1")
    fun getAllAliveFlow(): Flow<List<Character>>
    
    @Query("SELECT * FROM characters ORDER BY lastPlayedAt DESC LIMIT 1")
    suspend fun getLastPlayed(): Character?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character): Long
    
    @Update
    suspend fun update(character: Character)
    
    @Delete
    suspend fun delete(character: Character)
    
    @Query("UPDATE characters SET updatedAt = :timestamp, lastPlayedAt = :timestamp WHERE id = :id")
    suspend fun updateTimestamps(id: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE characters SET isAlive = 0 WHERE id = :id")
    suspend fun markAsDead(id: Long)
}
