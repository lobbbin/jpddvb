package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getById(id: String): InventoryItemEntity?

    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId")
    suspend fun getByOwner(ownerId: String): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId")
    fun getByOwnerFlow(ownerId: String): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND itemType = :type")
    suspend fun getByCategory(ownerId: String, type: String): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND itemId = :itemId")
    suspend fun getByItemId(ownerId: String, itemId: String): InventoryItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryItemEntity>)

    @Update
    suspend fun update(item: InventoryItemEntity)

    @Delete
    suspend fun delete(item: InventoryItemEntity)

    @Query("UPDATE inventory_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: String, quantity: Int)
}
