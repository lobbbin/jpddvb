package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.LogEntryDao
import com.popop.lifesimulator.data.database.entity.LogEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for game log data
 */
class LogRepository(private val logEntryDao: LogEntryDao) {
    val allLogs: Flow<List<LogEntryEntity>> = logEntryDao.getAll()

    suspend fun getLogById(id: Long): LogEntryEntity? = logEntryDao.getById(id)

    suspend fun insert(log: LogEntryEntity) = logEntryDao.insert(log)

    suspend fun update(log: LogEntryEntity) = logEntryDao.update(log)

    suspend fun delete(log: LogEntryEntity) = logEntryDao.delete(log)
}
