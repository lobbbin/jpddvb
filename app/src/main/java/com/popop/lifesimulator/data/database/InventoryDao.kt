package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.inventory.InventoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    
    @Query("SELECT * FROM inventoryitems WHERE id = :id")
    suspend fun getById(id: Long): InventoryItem?
    
    @Query("SELECT * FROM inventoryitems WHERE ownerId = :ownerId")
    suspend fun getByOwner(ownerId: Long): List<InventoryItem>
    
    @Query("SELECT * FROM inventoryitems WHERE ownerId = :ownerId")
    fun getByOwnerFlow(ownerId: Long): Flow<List<InventoryItem>>
    
    @Query("SELECT * FROM inventoryitems WHERE ownerId = :ownerId AND category = :category")
    suspend fun getByCategory(ownerId: Long, category: String): List<InventoryItem>
    
    @Query("SELECT * FROM inventoryitems WHERE ownerId = :ownerId AND itemId = :itemId")
    suspend fun getByItemId(ownerId: Long, itemId: String): InventoryItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryItem>)
    
    @Update
    suspend fun update(item: InventoryItem)
    
    @Delete
    suspend fun delete(item: InventoryItem)
    
    @Query("UPDATE inventoryitems SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)
}
