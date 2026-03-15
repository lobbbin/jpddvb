package com.popop.lifesimulator.data.models.world

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Location types for different places in the game world
 */
enum class LocationType(val displayName: String) {
    // Royalty
    PALACE("Palace"),
    THRONE_ROOM("Throne Room"),
    ROYAL_COURT("Royal Court"),
    NOBLE_ESTATE("Noble Estate"),
    
    // Government/Politics
    GOVERNMENT_BUILDING("Government Building"),
    CAPITOL("Capitol"),
    CITY_HALL("City Hall"),
    COURTHOUSE("Courthouse"),
    LAW_FIRM("Law Firm"),
    EMBASSY("Embassy"),
    
    // Law Enforcement
    POLICE_STATION("Police Station"),
    PRISON("Prison/Jail"),
    FIRE_STATION("Fire Station"),
    
    // Health & Education
    HOSPITAL("Hospital/Clinic"),
    UNIVERSITY("University/School"),
    LIBRARY("Library"),
    SCHOOL("School"),
    DAYCARE("Daycare"),
    NURSING_HOME("Nursing Home"),
    
    // Religion
    CHURCH("Church/Temple"),
    MONASTERY("Monastery"),
    HOLY_SITE("Holy Site"),
    
    // Commerce
    MARKETPLACE("Marketplace"),
    SHOP("Shop/Store"),
    BANK("Bank"),
    RESTAURANT("Restaurant"),
    HOTEL("Hotel"),
    NIGHTCLUB("Nightclub"),
    CASINO("Casino"),
    FACTORY("Factory"),
    OFFICE_BUILDING("Office Building"),
    
    // Recreation
    GYM("Gym"),
    PARK("Park"),
    CEMETERY("Cemetery"),
    
    // Transportation
    HARBOR("Harbor/Port"),
    AIRPORT("Airport"),
    TRAIN_STATION("Train Station"),
    
    // Military
    MILITARY_BASE("Military Base"),
    
    // Residential
    SLUMS("Slums"),
    APARTMENT("Apartment"),
    HOUSE("House"),
    MANSION("Mansion"),
    
    // Other
    CONSTRUCTION_SITE("Construction Site"),
    FARM("Farm/Ranch"),
    LABORATORY("Laboratory"),
    HOMELESS_SHELTER("Homeless Shelter"),
    FOOD_BANK("Food Bank"),
    NONPROFIT_OFFICE("Nonprofit Office"),
    CUSTOM("Custom")
}

/**
 * Location entity representing places in the game world
 */
@Entity(tableName = "locations")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
    val type: LocationType,
    val regionId: Long? = null,
    val parentId: Long? = null,  // For nested locations (rooms in buildings)
    
    // Properties
    val isUnlocked: Boolean = false,
    val isAccessible: Boolean = true,
    val securityLevel: Int = 0,  // 0-100
    val quality: Int = 50,  // 0-100
    val capacity: Int = 100,
    
    // Economic
    val entryCost: Double = 0.0,
    val value: Double = 0.0,
    
    // Associated factions
    val controllingFactionId: Long? = null,
    
    // Coordinates (for map display)
    val mapX: Double = 0.0,
    val mapY: Double = 0.0,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun isOwnedByPlayer(): Boolean = value > 0 && controllingFactionId == null
    
    fun getAccessibilityString(): String = when {
        !isAccessible -> "Inaccessible"
        securityLevel > 75 -> "High Security"
        securityLevel > 50 -> "Moderate Security"
        securityLevel > 25 -> "Low Security"
        else -> "Open Access"
    }
}

/**
 * Region containing multiple locations
 */
data class Region(
    val id: Long,
    val name: String,
    val description: String,
    val type: RegionType,
    val population: Int = 0,
    val wealthLevel: Int = 50,  // 0-100
    val crimeRate: Int = 0,  // 0-100
    val lawEnforcementLevel: Int = 50,  // 0-100
    val locations: List<Location> = emptyList()
)

enum class RegionType(val displayName: String) {
    CAPITAL("Capital Region"),
    PROVINCE("Province"),
    STATE("State"),
    COUNTY("County"),
    CITY("City"),
    TOWN("Town"),
    VILLAGE("Village"),
    RURAL("Rural Area"),
    WILDERNESS("Wilderness")
}
