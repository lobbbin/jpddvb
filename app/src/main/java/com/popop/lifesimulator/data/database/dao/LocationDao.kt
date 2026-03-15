package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Location entities.
 */
@Dao
interface LocationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(location: LocationEntity): Long
    
    @Update
    suspend fun update(location: LocationEntity)
    
    @Delete
    suspend fun delete(location: LocationEntity)
    
    @Query("SELECT * FROM locations WHERE id = :locationId")
    suspend fun getById(locationId: String): LocationEntity?
    
    @Query("SELECT * FROM locations WHERE id = :locationId")
    fun getByIdFlow(locationId: String): Flow<LocationEntity?>
    
    @Query("SELECT * FROM locations WHERE locationType = :type")
    suspend fun getByType(type: String): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE regionId = :regionId")
    suspend fun getByRegion(regionId: String): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE isAccessible = 1")
    suspend fun getAllAccessible(): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE isSafeZone = 1")
    suspend fun getSafeZones(): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE controlledByFactionId = :factionId")
    suspend fun getByFaction(factionId: String): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE name LIKE :query || '%' LIMIT :limit")
    suspend fun searchByName(query: String, limit: Int = 10): List<LocationEntity>
    
    @Query("SELECT * FROM locations WHERE :x BETWEEN CAST(SUBSTR(coordinates, 1, INSTR(coordinates, ',') - 1) AS INTEGER) - :radius AND CAST(SUBSTR(coordinates, 1, INSTR(coordinates, ',') - 1) AS INTEGER) + :radius AND :y BETWEEN CAST(SUBSTR(coordinates, INSTR(coordinates, ',') + 1) AS INTEGER) - :radius AND CAST(SUBSTR(coordinates, INSTR(coordinates, ',') + 1) AS INTEGER) + :radius")
    suspend fun getNearby(x: Int, y: Int, radius: Int): List<LocationEntity>
    
    @Query("UPDATE locations SET isAccessible = :accessible WHERE id = :locationId")
    suspend fun updateAccessibility(locationId: String, accessible: Boolean)
    
    @Query("UPDATE locations SET npcIds = :npcIds WHERE id = :locationId")
    suspend fun updateNpcs(locationId: String, npcIds: String)
    
    @Query("UPDATE locations SET lastUpdatedAt = :timestamp WHERE id = :locationId")
    suspend fun updateTimestamp(locationId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM locations WHERE id = :locationId")
    suspend fun deleteById(locationId: String)
    
    @Query("DELETE FROM locations")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getCount(): Int
}
