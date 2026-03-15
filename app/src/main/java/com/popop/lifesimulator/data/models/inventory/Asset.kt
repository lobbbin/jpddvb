package com.popop.lifesimulator.data.models.inventory

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Asset entity for properties, vehicles, and other valuable possessions
 */
@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val ownerId: Long,
    val name: String,
    val type: AssetType,
    val description: String = "",
    val value: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyUpkeep: Double = 0.0,
    val isOwned: Boolean = true,
    val isMortgaged: Boolean = false,
    val mortgageBalance: Double = 0.0,
    val mortgageRate: Double = 0.0,
    val acquiredAt: Long = System.currentTimeMillis(),
    val location: String = "",
    val metadata: String = ""  // JSON for additional properties
)

enum class AssetType(val displayName: String) {
    REAL_ESTATE("Real Estate"),
    VEHICLE("Vehicle"),
    BUSINESS("Business"),
    INVESTMENT("Investment"),
    COLLECTIBLE("Collectible"),
    INTELLECTUAL_PROPERTY("Intellectual Property"),
    OTHER("Other")
}
