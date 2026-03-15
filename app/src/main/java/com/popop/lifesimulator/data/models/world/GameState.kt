package com.popop.lifesimulator.data.models.world

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Game state entity - tracks the overall world state
 */
@Entity(tableName = "gamestate")
data class GameState(
    @PrimaryKey
    val id: Int = 1,  // Singleton, always ID 1
    
    // Time
    val currentYear: Int = 2024,
    val currentMonth: Int = 1,
    val currentDay: Int = 1,
    val currentHour: Int = 8,
    val currentMinute: Int = 0,
    val dayOfWeek: Int = 1,  // 1=Monday, 7=Sunday
    val season: Season = Season.SPRING,
    
    // Economy
    val inflationRate: Double = 2.0,
    val unemploymentRate: Double = 5.0,
    val gdpGrowth: Double = 2.5,
    val stockMarketIndex: Double = 1000.0,
    
    // Law & Order
    val nationalCrimeRate: Int = 50,
    val lawEnforcementEffectiveness: Int = 50,
    val corruptionLevel: Int = 30,
    
    // Politics
    val politicalStability: Int = 50,
    val publicMorale: Int = 50,
    val internationalTensions: Int = 30,
    
    // Events
    val activeEvents: String = "",  // JSON of active event IDs
    val completedEvents: String = "",  // JSON of completed event IDs
    val globalFlags: String = "",  // JSON of global state flags
    
    // World state
    val activeDisasters: String = "",  // JSON of active disasters
    val activeWars: String = "",  // JSON of active conflicts
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastTickAt: Long = System.currentTimeMillis()
) {
    fun getDateString(): String = "$currentYear-$currentMonth-$currentDay"
    
    fun getDateTimeString(): String = "$currentYear-$currentMonth-$currentDay $currentHour:$currentMinute"
    
    fun getSeasonString(): String = when (season) {
        Season.SPRING -> "Spring"
        Season.SUMMER -> "Summer"
        Season.FALL -> "Fall"
        Season.WINTER -> "Winter"
    }
    
    fun isWeekend(): Boolean = dayOfWeek >= 6
    
    fun isBusinessHours(): Boolean = currentHour in 9..17 && !isWeekend()
    
    fun getEconomyState(): EconomyState {
        return when {
            gdpGrowth > 4.0 -> EconomyState.BOOMING
            gdpGrowth > 0.0 -> EconomyState.GROWING
            gdpGrowth > -2.0 -> EconomyState.STAGNATING
            else -> EconomyState.RECESSION
        }
    }
}

enum class Season {
    SPRING, SUMMER, FALL, WINTER
}

enum class EconomyState(val displayName: String) {
    BOOMING("Booming"),
    GROWING("Growing"),
    STAGNATING("Stagnating"),
    RECESSION("Recession")
}

/**
 * Law and order tracking
 */
data class LawOrderState(
    val crimeRate: Int = 50,
    val lawEnforcementLevel: Int = 50,
    val corruptionLevel: Int = 30,
    val prisonPopulation: Int = 0,
    val prisonCapacity: Int = 1000,
    val activeInvestigations: Int = 0,
    val wantedLevels: Map<Long, Int> = emptyMap()  // characterId to wanted level
) {
    fun isPrisonOvercrowded(): Boolean = prisonPopulation > prisonCapacity
    
    fun getSafetyLevel(): SafetyLevel {
        return when {
            crimeRate < 20 -> SafetyLevel.VERY_SAFE
            crimeRate < 40 -> SafetyLevel.SAFE
            crimeRate < 60 -> SafetyLevel.MODERATE
            crimeRate < 80 -> SafetyLevel.DANGEROUS
            else -> SafetyLevel.VERY_DANGEROUS
        }
    }
}

enum class SafetyLevel(val displayName: String) {
    VERY_SAFE("Very Safe"),
    SAFE("Safe"),
    MODERATE("Moderate"),
    DANGEROUS("Dangerous"),
    VERY_DANGEROUS("Very Dangerous")
}

/**
 * Market data for economy simulation
 */
data class MarketData(
    val commodityPrices: Map<String, Double> = emptyMap(),
    val stockPrices: Map<String, Double> = emptyMap(),
    val realEstatePrices: Map<String, Double> = emptyMap(),
    val blackMarketPrices: Map<String, Double> = emptyMap(),
    val supplyDemand: Map<String, SupplyDemandState> = emptyMap(),
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun getPrice(itemId: String, isBlackMarket: Boolean = false): Double {
        return if (isBlackMarket) {
            blackMarketPrices[itemId] ?: 100.0
        } else {
            commodityPrices[itemId] ?: 100.0
        }
    }
    
    fun getSupplyDemand(itemId: String): SupplyDemandState {
        return supplyDemand[itemId] ?: SupplyDemandState.BALANCED
    }
}

enum class SupplyDemandState(val displayName: String, val priceModifier: Double) {
    SURPLUS("Surplus", 0.7),
    HIGH_SUPPLY("High Supply", 0.85),
    BALANCED("Balanced", 1.0),
    HIGH_DEMAND("High Demand", 1.25),
    SHORTAGE("Shortage", 1.5),
    CRITICAL_SHORTAGE("Critical Shortage", 2.0)
}
