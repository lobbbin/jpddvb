package com.popop.lifesimulator.domain.models

/**
 * Sealed class representing all location types in the game.
 */
sealed class LocationType(val value: String, val displayName: String, val icon: String = "📍") {
    
    // Residential
    object Home : LocationType("home", "Home", "🏠")
    object Apartment : LocationType("apartment", "Apartment", "🏢")
    object Mansion : LocationType("mansion", "Mansion", "🏰")
    object Cottage : LocationType("cottage", "Cottage", "🏡")
    object Dormitory : LocationType("dormitory", "Dormitory", "🏫")
    
    // Royal and Government
    object Palace : LocationType("palace", "Palace", "👑")
    object Castle : LocationType("castle", "Castle", "🏰")
    object ThroneRoom : LocationType("throne_room", "Throne Room", "🪑")
    object GovernmentBuilding : LocationType("government", "Government Building", "🏛️")
    object Courthouse : LocationType("courthouse", "Courthouse", "⚖️")
    object Embassy : LocationType("embassy", "Embassy", "🏳️")
    
    // Commercial
    object Shop : LocationType("shop", "Shop", "🏪")
    object Market : LocationType("market", "Market", "🏛️")
    object Bank : LocationType("bank", "Bank", "🏦")
    object Restaurant : LocationType("restaurant", "Restaurant", "🍽️")
    object Tavern : LocationType("tavern", "Tavern", "🍺")
    object Hotel : LocationType("hotel", "Hotel", "🏨")
    
    // Industrial
    object Factory : LocationType("factory", "Factory", "🏭")
    object Warehouse : LocationType("warehouse", "Warehouse", "📦")
    object Mine : LocationType("mine", "Mine", "⛏️")
    object Farm : LocationType("farm", "Farm", "🚜")
    
    // Services
    object Hospital : LocationType("hospital", "Hospital", "🏥")
    object Clinic : LocationType("clinic", "Clinic", "💊")
    object School : LocationType("school", "School", "📚")
    object University : LocationType("university", "University", "🎓")
    object Library : LocationType("library", "Library", "📖")
    object Church : LocationType("church", "Church", "⛪")
    object Temple : LocationType("temple", "Temple", "🛕")
    
    // Criminal
    object Prison : LocationType("prison", "Prison", "🔒")
    object Jail : LocationType("jail", "Jail", "🚔")
    object Hideout : LocationType("hideout", "Hideout", "🦹")
    object BlackMarket : LocationType("black_market", "Black Market", "🌑")
    object Casino : LocationType("casino", "Casino", "🎰")
    object Brothel : LocationType("brothel", "Brothel", "🌙")
    
    // Entertainment
    object Theater : LocationType("theater", "Theater", "🎭")
    object Arena : LocationType("arena", "Arena", "🏟️")
    object Park : LocationType("park", "Park", "🌳")
    object Club : LocationType("club", "Club", "💃")
    object Stadium : LocationType("stadium", "Stadium", "🏈")
    
    // Transportation
    object Port : LocationType("port", "Port", "⚓")
    object Airport : LocationType("airport", "Airport", "✈️")
    object Station : LocationType("station", "Station", "🚂")
    object Dock : LocationType("dock", "Dock", "🚢")
    
    // Military
    object Barracks : LocationType("barracks", "Barracks", "🎖️")
    object Fort : LocationType("fort", "Fort", "🏯")
    object TrainingGround : LocationType("training_ground", "Training Ground", "🎯")
    
    // Natural
    object Wilderness : LocationType("wilderness", "Wilderness", "🌲")
    object Cave : LocationType("cave", "Cave", "🕳️")
    object Beach : LocationType("beach", "Beach", "🏖️")
    object Mountain : LocationType("mountain", "Mountain", "⛰️")
    object Forest : LocationType("forest", "Forest", "🌳")
    object Lake : LocationType("lake", "Lake", "🏞️")
    
    // Special
    object SecretBase : LocationType("secret_base", "Secret Base", "🔐")
    object Laboratory : LocationType("laboratory", "Laboratory", "🔬")
    object Ruins : LocationType("ruins", "Ruins", "🏺")
    object Dungeon : LocationType("dungeon", "Dungeon", "💀")
    object Generic : LocationType("generic", "Location", "📍")
    
    companion object {
        fun fromValue(value: String): LocationType = when (value) {
            "home" -> Home
            "apartment" -> Apartment
            "mansion" -> Mansion
            "cottage" -> Cottage
            "dormitory" -> Dormitory
            "palace" -> Palace
            "castle" -> Castle
            "throne_room" -> ThroneRoom
            "government" -> GovernmentBuilding
            "courthouse" -> Courthouse
            "embassy" -> Embassy
            "shop" -> Shop
            "market" -> Market
            "bank" -> Bank
            "restaurant" -> Restaurant
            "tavern" -> Tavern
            "hotel" -> Hotel
            "factory" -> Factory
            "warehouse" -> Warehouse
            "mine" -> Mine
            "farm" -> Farm
            "hospital" -> Hospital
            "clinic" -> Clinic
            "school" -> School
            "university" -> University
            "library" -> Library
            "church" -> Church
            "temple" -> Temple
            "prison" -> Prison
            "jail" -> Jail
            "hideout" -> Hideout
            "black_market" -> BlackMarket
            "casino" -> Casino
            "brothel" -> Brothel
            "theater" -> Theater
            "arena" -> Arena
            "park" -> Park
            "club" -> Club
            "stadium" -> Stadium
            "port" -> Port
            "airport" -> Airport
            "station" -> Station
            "dock" -> Dock
            "barracks" -> Barracks
            "fort" -> Fort
            "training_ground" -> TrainingGround
            "wilderness" -> Wilderness
            "cave" -> Cave
            "beach" -> Beach
            "mountain" -> Mountain
            "forest" -> Forest
            "lake" -> Lake
            "secret_base" -> SecretBase
            "laboratory" -> Laboratory
            "ruins" -> Ruins
            "dungeon" -> Dungeon
            else -> Generic
        }
    }
}
