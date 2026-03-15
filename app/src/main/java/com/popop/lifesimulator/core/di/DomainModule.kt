package com.popop.lifesimulator.core.di

import com.popop.lifesimulator.core.utils.FlagManager
import com.popop.lifesimulator.core.utils.GameLogger
import com.popop.lifesimulator.core.utils.RandomEventGenerator
import com.popop.lifesimulator.core.utils.TimeManager
import com.popop.lifesimulator.domain.repositories.*
import com.popop.lifesimulator.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Domain layer module for Hilt dependency injection.
 * Provides repositories and use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    
    @Provides
    @Singleton
    fun provideTimeManager(): TimeManager = TimeManager()
    
    @Provides
    @Singleton
    fun provideRandomEventGenerator(): RandomEventGenerator = RandomEventGenerator()
    
    @Provides
    @Singleton
    fun provideFlagManager(): FlagManager = FlagManager()
    
    @Provides
    @Singleton
    fun provideGameLogger(): GameLogger = GameLogger()
    
    @Provides
    @Singleton
    fun providePlayerRepository(
        playerDao: com.popop.lifesimulator.data.database.dao.PlayerDao
    ): PlayerRepository = PlayerRepository(playerDao)
    
    @Provides
    @Singleton
    fun provideNpcRepository(
        npcDao: com.popop.lifesimulator.data.database.dao.NpcDao,
        relationshipDao: com.popop.lifesimulator.data.database.dao.RelationshipDao
    ): NpcRepository = NpcRepository(npcDao, relationshipDao)
    
    @Provides
    @Singleton
    fun provideLocationRepository(
        locationDao: com.popop.lifesimulator.data.database.dao.LocationDao
    ): LocationRepository = LocationRepository(locationDao)
    
    @Provides
    @Singleton
    fun provideFactionRepository(
        factionDao: com.popop.lifesimulator.data.database.dao.FactionDao
    ): FactionRepository = FactionRepository(factionDao)
    
    @Provides
    @Singleton
    fun provideInventoryRepository(
        inventoryDao: com.popop.lifesimulator.data.database.dao.InventoryDao
    ): InventoryRepository = InventoryRepository(inventoryDao)
    
    @Provides
    @Singleton
    fun provideQuestRepository(
        questDao: com.popop.lifesimulator.data.database.dao.QuestDao
    ): QuestRepository = QuestRepository(questDao)
    
    @Provides
    @Singleton
    fun provideLogRepository(
        logEntryDao: com.popop.lifesimulator.data.database.dao.LogEntryDao
    ): LogRepository = LogRepository(logEntryDao)
    
    @Provides
    @Singleton
    fun provideGameStateRepository(
        gameStateDao: com.popop.lifesimulator.data.database.dao.GameStateDao
    ): GameStateRepository = GameStateRepository(gameStateDao)
}
