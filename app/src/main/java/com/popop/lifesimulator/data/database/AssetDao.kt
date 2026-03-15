package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getById(id: String): AssetEntity?

    @Query("SELECT * FROM assets WHERE ownerId = :ownerId")
    suspend fun getByOwner(ownerId: String): List<AssetEntity>

    @Query("SELECT * FROM assets WHERE ownerId = :ownerId")
    fun getByOwnerFlow(ownerId: String): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE ownerId = :ownerId AND assetType = :type")
    suspend fun getByType(ownerId: String, type: String): List<AssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: AssetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<AssetEntity>)

    @Update
    suspend fun update(asset: AssetEntity)

    @Delete
    suspend fun delete(asset: AssetEntity)

    @Query("UPDATE assets SET value = :value WHERE id = :id")
    suspend fun updateValue(id: String, value: Long)

    @Query("SELECT SUM(value) FROM assets WHERE ownerId = :ownerId")
    suspend fun getTotalValue(ownerId: String): Long?
}
