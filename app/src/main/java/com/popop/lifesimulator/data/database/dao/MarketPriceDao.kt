package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.MarketPriceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Market Price entities.
 */
@Dao
interface MarketPriceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(price: MarketPriceEntity)
    
    @Update
    suspend fun update(price: MarketPriceEntity)
    
    @Delete
    suspend fun delete(price: MarketPriceEntity)
    
    @Query("SELECT * FROM market_prices WHERE id = :priceId")
    suspend fun getById(priceId: String): MarketPriceEntity?
    
    @Query("SELECT * FROM market_prices WHERE itemId = :itemId AND locationId = :locationId")
    suspend fun getByItemAndLocation(itemId: String, locationId: String?): MarketPriceEntity?
    
    @Query("SELECT * FROM market_prices WHERE locationId = :locationId")
    suspend fun getByLocation(locationId: String): List<MarketPriceEntity>
    
    @Query("SELECT * FROM market_prices WHERE regionId = :regionId")
    suspend fun getByRegion(regionId: String): List<MarketPriceEntity>
    
    @Query("SELECT * FROM market_prices WHERE marketType = :marketType")
    suspend fun getByMarketType(marketType: String): List<MarketPriceEntity>
    
    @Query("SELECT * FROM market_prices WHERE itemId = :itemId ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestForItem(itemId: String): MarketPriceEntity?
    
    @Query("SELECT * FROM market_prices WHERE currentPrice < basePrice * :threshold")
    suspend fun getUnderpricedItems(threshold: Float = 0.8f): List<MarketPriceEntity>
    
    @Query("SELECT * FROM market_prices WHERE currentPrice > basePrice * :threshold")
    suspend fun getOverpricedItems(threshold: Float = 1.2f): List<MarketPriceEntity>
    
    @Query("SELECT * FROM market_prices WHERE isBlackMarket = 1")
    suspend fun getBlackMarketPrices(): List<MarketPriceEntity>
    
    @Query("UPDATE market_prices SET currentPrice = :price, lastUpdated = :timestamp WHERE id = :priceId")
    suspend fun updatePrice(priceId: String, price: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE market_prices SET supply = :supply, demand = :demand, lastUpdated = :timestamp WHERE id = :priceId")
    suspend fun updateSupplyDemand(priceId: String, supply: Float, demand: Float, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM market_prices WHERE id = :priceId")
    suspend fun deleteById(priceId: String)
    
    @Query("DELETE FROM market_prices WHERE locationId = :locationId")
    suspend fun deleteByLocation(locationId: String)
}
