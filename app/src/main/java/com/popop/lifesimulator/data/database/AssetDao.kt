package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.inventory.Asset
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    
    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getById(id: Long): Asset?
    
    @Query("SELECT * FROM assets WHERE ownerId = :ownerId")
    suspend fun getByOwner(ownerId: Long): List<Asset>
    
    @Query("SELECT * FROM assets WHERE ownerId = :ownerId")
    fun getByOwnerFlow(ownerId: Long): Flow<List<Asset>>
    
    @Query("SELECT * FROM assets WHERE ownerId = :ownerId AND assetType = :type")
    suspend fun getByType(ownerId: Long, type: String): List<Asset>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: Asset): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<Asset>)
    
    @Update
    suspend fun update(asset: Asset)
    
    @Delete
    suspend fun delete(asset: Asset)
    
    @Query("UPDATE assets SET value = :value WHERE id = :id")
    suspend fun updateValue(id: Long, value: Double)
    
    @Query("SELECT SUM(value) FROM assets WHERE ownerId = :ownerId")
    suspend fun getTotalValue(ownerId: Long): Double?
}
