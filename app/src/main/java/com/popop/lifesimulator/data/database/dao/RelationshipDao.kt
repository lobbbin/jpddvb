package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.RelationshipEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Relationship entities.
 */
@Dao
interface RelationshipDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relationship: RelationshipEntity)
    
    @Update
    suspend fun update(relationship: RelationshipEntity)
    
    @Delete
    suspend fun delete(relationship: RelationshipEntity)
    
    @Query("SELECT * FROM relationships WHERE id = :relationshipId")
    suspend fun getById(relationshipId: String): RelationshipEntity?
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND npcId = :npcId")
    suspend fun getByPlayerAndNpc(playerId: String, npcId: String): RelationshipEntity?
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND npcId = :npcId")
    fun getByPlayerAndNpcFlow(playerId: String, npcId: String): Flow<RelationshipEntity?>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId")
    suspend fun getAllForPlayer(playerId: String): List<RelationshipEntity>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId")
    fun getAllForPlayerFlow(playerId: String): Flow<List<RelationshipEntity>>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND relationshipType = :type")
    suspend fun getByType(playerId: String, type: String): List<RelationshipEntity>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND isRomanticPartner = 1")
    suspend fun getRomanticPartners(playerId: String): List<RelationshipEntity>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND isFamily = 1")
    suspend fun getFamily(playerId: String): List<RelationshipEntity>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND loyalty >= :minLoyalty")
    suspend fun getLoyalContacts(playerId: String, minLoyalty: Int): List<RelationshipEntity>
    
    @Query("SELECT * FROM relationships WHERE playerId = :playerId AND trust >= :minTrust")
    suspend fun getTrustedContacts(playerId: String, minTrust: Int): List<RelationshipEntity>
    
    @Query("UPDATE relationships SET trust = :trust WHERE id = :relationshipId")
    suspend fun updateTrust(relationshipId: String, trust: Int)
    
    @Query("UPDATE relationships SET loyalty = :loyalty WHERE id = :relationshipId")
    suspend fun updateLoyalty(relationshipId: String, loyalty: Int)
    
    @Query("UPDATE relationships SET opinion = :opinion WHERE id = :relationshipId")
    suspend fun updateOpinion(relationshipId: String, opinion: Int)
    
    @Query("UPDATE relationships SET debtOwed = :debt WHERE id = :relationshipId")
    suspend fun updateDebt(relationshipId: String, debt: Long)
    
    @Query("UPDATE relationships SET interactionsCount = interactionsCount + 1, lastInteractionDate = :timestamp WHERE id = :relationshipId")
    suspend fun recordInteraction(relationshipId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM relationships WHERE id = :relationshipId")
    suspend fun deleteById(relationshipId: String)
    
    @Query("DELETE FROM relationships WHERE playerId = :playerId")
    suspend fun deleteAllForPlayer(playerId: String)
}
