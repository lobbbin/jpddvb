package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Market price entity for tracking item prices across locations.
 * Enables dynamic economy with supply/demand mechanics.
 */
@Entity(
    tableName = "market_prices",
    indices = [
        Index(value = ["locationId"]),
        Index(value = ["itemId"]),
        Index(value = ["regionId"])
    ]
)
data class MarketPriceEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    // Price identification
    val itemId: String,
    val itemName: String,
    
    // Location context
    val locationId: String? = null, // Null = global average
    val regionId: String? = null,
    val marketType: String = "local", // local, regional, national, global
    
    // Price data
    val basePrice: Long = 100,
    val currentPrice: Long = 100,
    val minPrice: Long = 50,
    val maxPrice: Long = 500,
    val priceHistory: String = "", // JSON array of historical prices
    
    // Market dynamics
    val supply: Float = 1.0f, // 0.0-2.0, 1.0 = normal
    val demand: Float = 1.0f, // 0.0-2.0, 1.0 = normal
    val supplyTrend: Float = 0.0f, // -1.0 to 1.0, rate of change
    val demandTrend: Float = 0.0f,
    
    // Modifiers
    val seasonalModifier: Float = 1.0f,
    val eventModifier: Float = 1.0f,
    val factionModifier: Float = 1.0f, // Faction control affects prices
    val scarcityModifier: Float = 1.0f,
    
    // Economic data
    val volumeTraded: Int = 0, // Units traded today
    val lastTradePrice: Long = 100,
    val averagePrice: Long = 100,
    val priceVolatility: Float = 0.1f, // 0.0-1.0
    
    // Special conditions
    val isControlled: Boolean = false, // Price controlled by faction
    val controllingFactionId: String? = null,
    val isBlackMarket: Boolean = false,
    val blackMarketPremium: Float = 1.5f,
    
    // Timestamps
    val lastUpdated: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)
