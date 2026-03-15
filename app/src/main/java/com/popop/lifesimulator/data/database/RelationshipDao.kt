package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.relationship.Relationship
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationshipDao {
    
    @Query("SELECT * FROM relationships WHERE id = :id")
    suspend fun getById(id: Long): Relationship?
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId AND npcId = :npcId")
    suspend fun getByCharacterAndNpc(characterId: Long, npcId: Long): Relationship?
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId")
    suspend fun getByCharacter(characterId: Long): List<Relationship>
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId")
    fun getByCharacterFlow(characterId: Long): Flow<List<Relationship>>
    
    @Query("SELECT * FROM relationships WHERE npcId = :npcId")
    suspend fun getByNpc(npcId: Long): List<Relationship>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relationship: Relationship): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(relationships: List<Relationship>)
    
    @Update
    suspend fun update(relationship: Relationship)
    
    @Delete
    suspend fun delete(relationship: Relationship)
    
    @Query("DELETE FROM relationships WHERE characterId = :characterId AND npcId = :npcId")
    suspend fun deleteByCharacterAndNpc(characterId: Long, npcId: Long)
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId AND relationshipType = :type")
    suspend fun getByType(characterId: Long, type: String): List<Relationship>
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId ORDER BY relationshipScore DESC")
    suspend fun getBestRelationships(characterId: Long): List<Relationship>
    
    @Query("SELECT * FROM relationships WHERE characterId = :characterId ORDER BY relationshipScore ASC")
    suspend fun getWorstRelationships(characterId: Long): List<Relationship>
}
