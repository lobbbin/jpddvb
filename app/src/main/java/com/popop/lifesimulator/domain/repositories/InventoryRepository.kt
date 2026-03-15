package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.InventoryDao
import com.popop.lifesimulator.data.models.inventory.InventoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository for inventory data
 */
class InventoryRepository(private val inventoryDao: InventoryDao) {
    val allItems: Flow<List<InventoryItem>> = inventoryDao.getAll()

    suspend fun getItemById(id: Long): InventoryItem? = inventoryDao.getById(id)

    suspend fun insert(item: InventoryItem) = inventoryDao.insert(item)

    suspend fun update(item: InventoryItem) = inventoryDao.update(item)

    suspend fun delete(item: InventoryItem) = inventoryDao.delete(item)
}
