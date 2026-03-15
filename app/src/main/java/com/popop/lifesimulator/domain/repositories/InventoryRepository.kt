package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.InventoryDao
import com.popop.lifesimulator.data.database.entity.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for inventory data
 */
class InventoryRepository(private val inventoryDao: InventoryDao) {
    val allItems: Flow<List<InventoryItemEntity>> = inventoryDao.getByOwnerFlow("")

    suspend fun getItemById(id: String): InventoryItemEntity? = inventoryDao.getById(id)

    suspend fun insert(item: InventoryItemEntity) = inventoryDao.insert(item)

    suspend fun update(item: InventoryItemEntity) = inventoryDao.update(item)

    suspend fun delete(item: InventoryItemEntity) = inventoryDao.delete(item)
}
