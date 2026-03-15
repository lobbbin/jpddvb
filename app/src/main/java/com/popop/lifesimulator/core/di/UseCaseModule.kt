package com.popop.lifesimulator.core.di

import com.popop.lifesimulator.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Use case module for Hilt dependency injection.
 * Provides all use cases as singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideCreatePlayerUseCase(
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): CreatePlayerUseCase = CreatePlayerUseCase(playerRepository)
    
    @Provides
    @Singleton
    fun provideUpdatePlayerUseCase(
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): UpdatePlayerUseCase = UpdatePlayerUseCase(playerRepository)
    
    @Provides
    @Singleton
    fun provideGetPlayerUseCase(
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): GetPlayerUseCase = GetPlayerUseCase(playerRepository)
    
    @Provides
    @Singleton
    fun provideAdvanceTimeUseCase(
        timeManager: com.popop.lifesimulator.core.utils.TimeManager,
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): AdvanceTimeUseCase = AdvanceTimeUseCase(timeManager, playerRepository)
    
    @Provides
    @Singleton
    fun provideGenerateRandomEventUseCase(
        randomEventGenerator: com.popop.lifesimulator.core.utils.RandomEventGenerator
    ): GenerateRandomEventUseCase = GenerateRandomEventUseCase(randomEventGenerator)
    
    @Provides
    @Singleton
    fun provideCreateNpcUseCase(
        npcRepository: com.popop.lifesimulator.domain.repositories.NpcRepository
    ): CreateNpcUseCase = CreateNpcUseCase(npcRepository)
    
    @Provides
    @Singleton
    fun provideGetNpcUseCase(
        npcRepository: com.popop.lifesimulator.domain.repositories.NpcRepository
    ): GetNpcUseCase = GetNpcUseCase(npcRepository)
    
    @Provides
    @Singleton
    fun provideUpdateRelationshipUseCase(
        npcRepository: com.popop.lifesimulator.domain.repositories.NpcRepository
    ): UpdateRelationshipUseCase = UpdateRelationshipUseCase(npcRepository)
    
    @Provides
    @Singleton
    fun provideJoinFactionUseCase(
        factionRepository: com.popop.lifesimulator.domain.repositories.FactionRepository,
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): JoinFactionUseCase = JoinFactionUseCase(factionRepository, playerRepository)
    
    @Provides
    @Singleton
    fun provideTravelUseCase(
        locationRepository: com.popop.lifesimulator.domain.repositories.LocationRepository,
        playerRepository: com.popop.lifesimulator.domain.repositories.PlayerRepository
    ): TravelUseCase = TravelUseCase(locationRepository, playerRepository)
    
    @Provides
    @Singleton
    fun provideLogActionUseCase(
        logRepository: com.popop.lifesimulator.domain.repositories.LogRepository,
        gameLogger: com.popop.lifesimulator.core.utils.GameLogger
    ): LogActionUseCase = LogActionUseCase(logRepository, gameLogger)
}
