package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.LogEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Log Entry entities.
 */
@Dao
interface LogEntryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LogEntryEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(entry: LogEntryEntity): Long
    
    @Update
    suspend fun update(entry: LogEntryEntity)
    
    @Delete
    suspend fun delete(entry: LogEntryEntity)
    
    @Query("SELECT * FROM log_entries WHERE id = :entryId")
    suspend fun getById(entryId: String): LogEntryEntity?
    
    @Query("SELECT * FROM log_entries WHERE category = :category ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByCategory(category: String, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE playerId = :playerId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByPlayer(playerId: String, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE playerId = :playerId AND category = :category ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByPlayerAndCategory(playerId: String, category: String, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getByTimeRange(startTime: Long, endTime: Long): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE logType = :type ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByType(type: String, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE importance >= :minImportance ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByImportance(minImportance: Int, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries WHERE tags LIKE '%' || :tag || '%' ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getByTag(tag: String, limit: Int = 50): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<LogEntryEntity>
    
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentFlow(limit: Int = 100): Flow<List<LogEntryEntity>>
    
    @Query("SELECT COUNT(*) FROM log_entries WHERE playerId = :playerId")
    suspend fun getCountForPlayer(playerId: String): Int
    
    @Query("DELETE FROM log_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: String)
    
    @Query("DELETE FROM log_entries WHERE timestamp < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)
    
    @Query("DELETE FROM log_entries")
    suspend fun deleteAll()
}
