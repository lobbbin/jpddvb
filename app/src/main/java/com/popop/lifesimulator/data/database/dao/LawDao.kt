package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.LawEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Law entities.
 */
@Dao
interface LawDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(law: LawEntity)
    
    @Update
    suspend fun update(law: LawEntity)
    
    @Delete
    suspend fun delete(law: LawEntity)
    
    @Query("SELECT * FROM laws WHERE id = :lawId")
    suspend fun getById(lawId: String): LawEntity?
    
    @Query("SELECT * FROM laws WHERE factionId = :factionId AND isActive = 1")
    suspend fun getByFaction(factionId: String): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE regionId = :regionId AND isActive = 1")
    suspend fun getByRegion(regionId: String): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE lawType = :type AND isActive = 1")
    suspend fun getByType(type: String): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE category = :category AND isActive = 1")
    suspend fun getByCategory(category: String): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE locationIds LIKE '%' || :locationId || '%' AND isActive = 1")
    suspend fun getByLocation(locationId: String): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE severity >= :minSeverity AND isActive = 1")
    suspend fun getSevereLaws(minSeverity: Int): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE isControversial = 1 AND isActive = 1")
    suspend fun getControversialLaws(): List<LawEntity>
    
    @Query("SELECT * FROM laws WHERE enforcingFactionId = :factionId")
    suspend fun getByEnforcingFaction(factionId: String): List<LawEntity>
    
    @Query("UPDATE laws SET isActive = :active WHERE id = :lawId")
    suspend fun updateActive(lawId: String, active: Boolean)
    
    @Query("UPDATE laws SET violationCount = violationCount + 1 WHERE id = :lawId")
    suspend fun incrementViolationCount(lawId: String)
    
    @Query("UPDATE laws SET lastUpdatedAt = :timestamp WHERE id = :lawId")
    suspend fun updateTimestamp(lawId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM laws WHERE id = :lawId")
    suspend fun deleteById(lawId: String)
    
    @Query("DELETE FROM laws WHERE factionId = :factionId")
    suspend fun deleteByFaction(factionId: String)
    
    @Query("DELETE FROM laws WHERE isActive = 0")
    suspend fun deleteInactive()
}
