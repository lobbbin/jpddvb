package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.PlayerEntity
import com.popop.lifesimulator.data.database.entity.NpcEntity
import com.popop.lifesimulator.data.database.entity.RelationshipEntity
import com.popop.lifesimulator.data.database.entity.LocationEntity
import com.popop.lifesimulator.data.database.entity.FactionEntity
import com.popop.lifesimulator.data.database.entity.GameStateEntity
import com.popop.lifesimulator.data.database.entity.LogEntryEntity
import com.popop.lifesimulator.data.database.entity.QuestEntity
import com.popop.lifesimulator.data.database.entity.InventoryItemEntity
import com.popop.lifesimulator.data.database.entity.AssetEntity

@Database(
    entities = [
        PlayerEntity::class,
        NpcEntity::class,
        RelationshipEntity::class,
        LocationEntity::class,
        FactionEntity::class,
        GameStateEntity::class,
        LogEntryEntity::class,
        QuestEntity::class,
        InventoryItemEntity::class,
        AssetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun npcDao(): NpcDao
    abstract fun relationshipDao(): RelationshipDao
    abstract fun locationDao(): LocationDao
    abstract fun factionDao(): FactionDao
    abstract fun gameStateDao(): GameStateDao
    abstract fun logEntryDao(): LogEntryDao
    abstract fun questDao(): QuestDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun assetDao(): AssetDao

    companion object {
        const val DATABASE_NAME = "life_simulator_db"
    }
}
