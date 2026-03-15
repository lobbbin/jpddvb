package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.StatModifierEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Stat Modifier entities.
 */
@Dao
interface StatModifierDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(modifier: StatModifierEntity)
    
    @Update
    suspend fun update(modifier: StatModifierEntity)
    
    @Delete
    suspend fun delete(modifier: StatModifierEntity)
    
    @Query("SELECT * FROM stat_modifiers WHERE id = :modifierId")
    suspend fun getById(modifierId: String): StatModifierEntity?
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND targetType = :targetType")
    suspend fun getByTarget(targetId: String, targetType: String = "player"): List<StatModifierEntity>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND targetType = :targetType")
    fun getByTargetFlow(targetId: String, targetType: String = "player"): Flow<List<StatModifierEntity>>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND modifierType = :type")
    suspend fun getByType(targetId: String, type: String): List<StatModifierEntity>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND expiresAt IS NOT NULL AND expiresAt > :currentTime")
    suspend fun getActiveTimedModifiers(targetId: String, currentTime: Long = System.currentTimeMillis()): List<StatModifierEntity>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND (expiresAt IS NULL OR expiresAt > :currentTime)")
    suspend fun getActiveModifiers(targetId: String, currentTime: Long = System.currentTimeMillis()): List<StatModifierEntity>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND expiresAt IS NOT NULL AND expiresAt <= :currentTime")
    suspend fun getExpiredModifiers(targetId: String, currentTime: Long = System.currentTimeMillis()): List<StatModifierEntity>
    
    @Query("SELECT * FROM stat_modifiers WHERE targetId = :targetId AND isVisible = 1 AND (expiresAt IS NULL OR expiresAt > :currentTime)")
    suspend fun getVisibleModifiers(targetId: String, currentTime: Long = System.currentTimeMillis()): List<StatModifierEntity>
    
    @Query("UPDATE stat_modifiers SET currentStacks = :stacks WHERE id = :modifierId")
    suspend fun updateStacks(modifierId: String, stacks: Int)
    
    @Query("UPDATE stat_modifiers SET remainingTime = :time WHERE id = :modifierId")
    suspend fun updateRemainingTime(modifierId: String, time: Long)
    
    @Query("DELETE FROM stat_modifiers WHERE id = :modifierId")
    suspend fun deleteById(modifierId: String)
    
    @Query("DELETE FROM stat_modifiers WHERE targetId = :targetId AND targetType = :targetType")
    suspend fun deleteAllForTarget(targetId: String, targetType: String = "player")
    
    @Query("DELETE FROM stat_modifiers WHERE expiresAt IS NOT NULL AND expiresAt <= :currentTime")
    suspend fun deleteExpired(currentTime: Long = System.currentTimeMillis())
}
