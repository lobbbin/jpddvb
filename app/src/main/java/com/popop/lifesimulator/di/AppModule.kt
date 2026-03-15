package com.popop.lifesimulator.di

import android.content.Context
import androidx.room.Room
import com.popop.lifesimulator.data.database.GameDatabase
import com.popop.lifesimulator.core.time.TimeManager
import com.popop.lifesimulator.core.utilities.FlagManager
import com.popop.lifesimulator.core.events.RandomEventGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGameDatabase(@ApplicationContext context: Context): GameDatabase {
        return Room.databaseBuilder(
            context,
            GameDatabase::class.java,
            GameDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun provideTimeManager(): TimeManager = TimeManager()

    @Provides
    @Singleton
    fun provideFlagManager(): FlagManager = FlagManager()

    @Provides
    @Singleton
    fun provideEventGenerator(): RandomEventGenerator = RandomEventGenerator()
}
