package com.popop.lifesimulator.data.models.inventory

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Item categories
 */
enum class ItemCategory(val displayName: String) {
    CURRENCY("Currency/Money"),
    FOOD_DRINK("Food/Drink"),
    CLOTHING("Clothing"),
    WEAPONS("Weapons"),
    TOOLS("Tools"),
    BOOKS("Books"),
    DOCUMENTS("Documents"),
    DRUGS("Drugs/Alcohol"),
    MEDICAL("Medical Supplies"),
    HYGIENE("Hygiene Products"),
    ELECTRONICS("Electronics"),
    FURNITURE("Furniture"),
    VEHICLES("Vehicles"),
    REAL_ESTATE("Real Estate"),
    ART_COLLECTIBLES("Art/Collectibles"),
    JEWELRY("Jewelry"),
    GIFTS("Gifts"),
    CONTRABAND("Contraband"),
    KEYS("Keys/Passcards"),
    MISC("Miscellaneous")
}

/**
 * Item condition
 */
enum class ItemCondition(val displayName: String, val valueModifier: Double) {
    PRISTINE("Pristine", 1.5),
    EXCELLENT("Excellent", 1.2),
    GOOD("Good", 1.0),
    WORN("Worn", 0.8),
    DAMAGED("Damaged", 0.5),
    BROKEN("Broken", 0.2),
    DESTROYED("Destroyed", 0.0)
}

/**
 * Base item template
 */
data class ItemTemplate(
    val id: String,
    val name: String,
    val description: String,
    val category: ItemCategory,
    val baseValue: Double,
    val weight: Double = 1.0,
    val isConsumable: Boolean = false,
    val isStackable: Boolean = true,
    val maxStack: Int = 99,
    val effects: List<ItemEffect> = emptyList(),
    val requirements: List<ItemRequirement> = emptyList()
)

sealed class ItemEffect {
    data class RestoreStat(val stat: String, val amount: Int) : ItemEffect()
    data class ModifyStat(val stat: String, val amount: Int, val duration: Long) : ItemEffect()
    data class LearnSkill(val skillType: String, val xpAmount: Double) : ItemEffect()
    data class UnlockLocation(val locationId: Long) : ItemEffect()
    object QuestItem : ItemEffect()
}

data class ItemRequirement(
    val type: RequirementType,
    val value: Int
)

enum class RequirementType {
    MIN_STAT, MIN_SKILL, MIN_LEVEL, LOCATION, TIME
}

/**
 * Inventory item instance
 */
@Entity(tableName = "inventoryitems")
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val ownerId: Long,  // Character or location ID
    val itemId: String,  // Template ID
    val name: String,
    val description: String,
    val category: ItemCategory,
    
    // State
    val quantity: Int = 1,
    val condition: ItemCondition = ItemCondition.GOOD,
    val currentValue: Double = 0.0,
    
    // Metadata
    val acquiredDate: Long = System.currentTimeMillis(),
    val acquiredFrom: String = "",  // Source of the item
    val isEquipped: Boolean = false,
    val isFavorite: Boolean = false,
    val customData: String = "",  // JSON for additional data
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getTotalValue(): Double = currentValue * quantity
    
    fun canStackWith(other: InventoryItem): Boolean {
        return itemId == other.itemId && 
               condition == other.condition &&
               quantity < 99
    }
    
    fun consume(amount: Int = 1): InventoryItem? {
        return if (quantity <= amount) null
        else copy(quantity = quantity - amount, updatedAt = System.currentTimeMillis())
    }
}

/**
 * Asset - property that generates income or appreciates
 */
@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val ownerId: Long,
    val assetType: AssetType,
    val name: String,
    val description: String,
    
    // Financial
    val purchasePrice: Double,
    val currentValue: Double,
    val incomeGenerated: Double = 0.0,  // Monthly income
    val maintenanceCost: Double = 0.0,  // Monthly cost
    
    // State
    val condition: Int = 100,  // 0-100
    val isMortgaged: Boolean = false,
    val mortgageBalance: Double = 0.0,
    val mortgageRate: Double = 0.0,
    val isRented: Boolean = false,
    val rentalIncome: Double = 0.0,
    
    // Location
    val locationId: Long? = null,
    val address: String = "",
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getNetMonthlyValue(): Double = incomeGenerated - maintenanceCost
    
    fun getEquity(): Double = if (isMortgaged) currentValue - mortgageBalance else currentValue
    
    fun getReturnOnInvestment(): Double {
        return if (purchasePrice > 0) {
            ((currentValue - purchasePrice) / purchasePrice) * 100
        } else 0.0
    }
}

enum class AssetType(val displayName: String) {
    REAL_ESTATE("Real Estate"),
    BUSINESS("Business"),
    STOCKS("Stocks"),
    BONDS("Bonds"),
    VEHICLES("Vehicles"),
    ART("Art"),
    JEWELRY("Jewelry"),
    PRECIOUS_METALS("Precious Metals"),
    CRYPTOCURRENCY("Cryptocurrency"),
    INTELLECTUAL_PROPERTY("Intellectual Property"),
    RETIREMENT_ACCOUNT("Retirement Account")
}

/**
 * Item registry with predefined items
 */
object ItemRegistry {
    
    val MONEY = ItemTemplate(
        id = "money",
        name = "Money",
        description = "Currency",
        category = ItemCategory.CURRENCY,
        baseValue = 1.0,
        isStackable = true,
        maxStack = Int.MAX_VALUE
    )
    
    val HEALTH_POTION = ItemTemplate(
        id = "health_potion",
        name = "Health Potion",
        description = "Restores 25 health",
        category = ItemCategory.MEDICAL,
        baseValue = 50.0,
        isConsumable = true,
        effects = listOf(ItemEffect.RestoreStat("health", 25))
    )
    
    val ENERGY_DRINK = ItemTemplate(
        id = "energy_drink",
        name = "Energy Drink",
        description = "Restores 15 energy",
        category = ItemCategory.FOOD_DRINK,
        baseValue = 5.0,
        weight = 0.5,
        isConsumable = true,
        effects = listOf(ItemEffect.RestoreStat("energy", 15))
    )
    
    val BOOK_SKILL = ItemTemplate(
        id = "book_skill",
        name = "Skill Book",
        description = "Grants skill experience",
        category = ItemCategory.BOOKS,
        baseValue = 30.0,
        weight = 1.0,
        isConsumable = true,
        effects = listOf(ItemEffect.LearnSkill("random", 50.0))
    )
    
    val LOCKPICK = ItemTemplate(
        id = "lockpick",
        name = "Lockpick Set",
        description = "Used for picking locks",
        category = ItemCategory.TOOLS,
        baseValue = 25.0,
        weight = 0.5,
        requirements = listOf(ItemRequirement(RequirementType.MIN_SKILL, 20))
    )
    
    val SMARTPHONE = ItemTemplate(
        id = "smartphone",
        name = "Smartphone",
        description = "Mobile communication device",
        category = ItemCategory.ELECTRONICS,
        baseValue = 500.0,
        weight = 0.3
    )
    
    val ALL_ITEMS = listOf(
        MONEY, HEALTH_POTION, ENERGY_DRINK, BOOK_SKILL, LOCKPICK, SMARTPHONE
    )
    
    fun getItemById(id: String): ItemTemplate? = ALL_ITEMS.find { it.id == id }
}
