package com.popop.lifesimulator.core.di

import android.content.Context
import androidx.room.Room
import com.popop.lifesimulator.data.database.GameDatabase
import com.popop.lifesimulator.data.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database module for Hilt dependency injection.
 * Provides Room database and all DAOs as singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    private const val DATABASE_NAME = "ultimate_life_simulator_db"
    
    @Provides
    @Singleton
    fun provideGameDatabase(
        @ApplicationContext context: Context
    ): GameDatabase = Room.databaseBuilder(
        context,
        GameDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration() // For alpha versions; use proper migrations in production
        .build()
    
    @Provides
    @Singleton
    fun providePlayerDao(database: GameDatabase): PlayerDao = database.playerDao()
    
    @Provides
    @Singleton
    fun provideNpcDao(database: GameDatabase): NpcDao = database.npcDao()
    
    @Provides
    @Singleton
    fun provideRelationshipDao(database: GameDatabase): RelationshipDao = database.relationshipDao()
    
    @Provides
    @Singleton
    fun provideLocationDao(database: GameDatabase): LocationDao = database.locationDao()
    
    @Provides
    @Singleton
    fun provideFactionDao(database: GameDatabase): FactionDao = database.factionDao()
    
    @Provides
    @Singleton
    fun provideInventoryDao(database: GameDatabase): InventoryDao = database.inventoryDao()
    
    @Provides
    @Singleton
    fun provideQuestDao(database: GameDatabase): QuestDao = database.questDao()
    
    @Provides
    @Singleton
    fun provideLogEntryDao(database: GameDatabase): LogEntryDao = database.logEntryDao()
    
    @Provides
    @Singleton
    fun provideGameStateDao(database: GameDatabase): GameStateDao = database.gameStateDao()
}
