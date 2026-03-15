package com.popop.lifesimulator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "life_simulator_db"
    }
}
