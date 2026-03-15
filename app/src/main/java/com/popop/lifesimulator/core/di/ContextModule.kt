package com.popop.lifesimulator.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Context provider module for Hilt dependency injection.
 * Provides application context throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    
    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ): Context = context
}

/**
 * DataStore provider for preferences.
 * Uses delegated property for DataStore instance.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_preferences")

/**
 * DataStore module for Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore
}
