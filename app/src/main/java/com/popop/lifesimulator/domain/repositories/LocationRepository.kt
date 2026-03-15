package com.popop.lifesimulator.domain.repositories

import com.popop.lifesimulator.data.database.dao.LocationDao
import com.popop.lifesimulator.data.models.world.Location
import kotlinx.coroutines.flow.Flow

/**
 * Repository for location data
 */
class LocationRepository(private val locationDao: LocationDao) {
    val allLocations: Flow<List<Location>> = locationDao.getAll()

    suspend fun getLocationById(id: Long): Location? = locationDao.getById(id)

    suspend fun insert(location: Location) = locationDao.insert(location)

    suspend fun update(location: Location) = locationDao.update(location)
}
