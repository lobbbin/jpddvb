package com.popop.lifesimulator.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Asset entity representing player-owned properties and valuable possessions.
 */
@Entity(
    tableName = "assets",
    indices = [Index(value = ["ownerId"])]
)
data class AssetEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    // Asset identification
    val assetType: String, // real_estate, vehicle, business, investment, etc.
    val name: String,
    val description: String = "",

    // Ownership
    val ownerId: String, // Player ID

    // Financial properties
    val value: Long = 0,
    val monthlyIncome: Long = 0,
    val monthlyUpkeep: Long = 0,

    // Mortgage/loan properties
    val isMortgaged: Boolean = false,
    val mortgageBalance: Long = 0,
    val mortgageRate: Float = 0f,
    val mortgageTermMonths: Int = 0,

    // Asset-specific properties
    val location: String = "", // Address or location name
    val size: Float = 0f, // Square footage or size
    val condition: String = "good", // poor, fair, good, excellent
    val isRented: Boolean = false,
    val rentalIncome: Long = 0,

    // Business-specific properties
    val businessType: String = "", // retail, manufacturing, service, etc.
    val employeeCount: Int = 0,
    val monthlyRevenue: Long = 0,
    val monthlyExpenses: Long = 0,

    // Vehicle-specific properties
    val make: String = "",
    val model: String = "",
    val year: Int = 0,
    val mileage: Long = 0,
    val isOperational: Boolean = true,

    // Metadata
    val metadata: String = "", // JSON for additional properties
    val isOwned: Boolean = true,
    val isLocked: Boolean = false, // Can't be sold/destroyed

    // Timestamps
    val acquiredAt: Long = System.currentTimeMillis(),
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
