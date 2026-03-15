package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Inventory entities.
 */
@Dao
interface InventoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItemEntity)
    
    @Update
    suspend fun update(item: InventoryItemEntity)
    
    @Delete
    suspend fun delete(item: InventoryItemEntity)
    
    @Query("SELECT * FROM inventory_items WHERE id = :itemId")
    suspend fun getById(itemId: String): InventoryItemEntity?
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType")
    suspend fun getByOwner(ownerId: String, ownerType: String = "player"): List<InventoryItemEntity>
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType")
    fun getByOwnerFlow(ownerId: String, ownerType: String = "player"): Flow<List<InventoryItemEntity>>
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType AND isEquipped = 1")
    suspend fun getEquippedItems(ownerId: String, ownerType: String = "player"): List<InventoryItemEntity>
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType AND itemType = :itemType")
    suspend fun getByType(ownerId: String, ownerType: String, itemType: String): List<InventoryItemEntity>
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType AND isQuestItem = 1")
    suspend fun getQuestItems(ownerId: String, ownerType: String = "player"): List<InventoryItemEntity>
    
    @Query("SELECT * FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType AND itemId = :templateId")
    suspend fun getItemByTemplate(ownerId: String, ownerType: String, templateId: String): InventoryItemEntity?
    
    @Query("SELECT SUM(quantity) FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType AND itemId = :templateId")
    suspend fun getItemQuantity(ownerId: String, ownerType: String, templateId: String): Int?
    
    @Query("UPDATE inventory_items SET quantity = :quantity WHERE id = :itemId")
    suspend fun updateQuantity(itemId: String, quantity: Int)
    
    @Query("UPDATE inventory_items SET isEquipped = :equipped WHERE id = :itemId")
    suspend fun updateEquipped(itemId: String, equipped: Boolean)
    
    @Query("UPDATE inventory_items SET durability = :durability WHERE id = :itemId")
    suspend fun updateDurability(itemId: String, durability: Int)
    
    @Query("DELETE FROM inventory_items WHERE id = :itemId")
    suspend fun deleteById(itemId: String)
    
    @Query("DELETE FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType")
    suspend fun deleteAllForOwner(ownerId: String, ownerType: String = "player")
    
    @Query("SELECT COUNT(*) FROM inventory_items WHERE ownerId = :ownerId AND ownerType = :ownerType")
    suspend fun getCount(ownerId: String, ownerType: String = "player"): Int
}
