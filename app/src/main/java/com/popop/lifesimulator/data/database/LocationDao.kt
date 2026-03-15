package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.world.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    
    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getById(id: Long): Location?
    
    @Query("SELECT * FROM locations WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<Location?>
    
    @Query("SELECT * FROM locations WHERE regionId = :regionId")
    suspend fun getByRegion(regionId: Long): List<Location>
    
    @Query("SELECT * FROM locations WHERE type = :type")
    suspend fun getByType(type: String): List<Location>
    
    @Query("SELECT * FROM locations WHERE isUnlocked = 1")
    suspend fun getAllUnlocked(): List<Location>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<Location>)
    
    @Update
    suspend fun update(location: Location)
    
    @Delete
    suspend fun delete(location: Location)
    
    @Query("SELECT * FROM locations WHERE parentId = :parentId")
    suspend fun getChildren(parentId: Long): List<Location>
}
