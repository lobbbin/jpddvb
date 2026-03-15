package com.popop.lifesimulator.data.database

import androidx.room.*
import com.popop.lifesimulator.data.models.world.GameState
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDao {
    
    @Query("SELECT * FROM gamestate WHERE id = 1")
    suspend fun getCurrent(): GameState?
    
    @Query("SELECT * FROM gamestate WHERE id = 1")
    fun getCurrentFlow(): Flow<GameState?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameState: GameState): Long
    
    @Update
    suspend fun update(gameState: GameState)
    
    @Query("DELETE FROM gamestate")
    suspend fun deleteAll()
}
