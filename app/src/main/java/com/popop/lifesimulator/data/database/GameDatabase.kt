package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.data.models.world.Location
import com.popop.lifesimulator.data.models.world.Faction
import com.popop.lifesimulator.data.models.relationship.Npc
import com.popop.lifesimulator.data.models.relationship.Relationship
import com.popop.lifesimulator.data.models.world.GameState

@Database(
    entities = [
        Character::class,
        Location::class,
        Faction::class,
        Npc::class,
        Relationship::class,
        GameState::class,
        GameLog::class,
        InventoryItem::class,
        Asset::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun factionDao(): FactionDao
    abstract fun npcDao(): NpcDao
    abstract fun relationshipDao(): RelationshipDao
    abstract fun gameStateDao(): GameStateDao
    abstract fun gameLogDao(): GameLogDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun assetDao(): AssetDao
    
    companion object {
        const val DATABASE_NAME = "life_simulator_db"
    }
}
