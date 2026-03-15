package com.popop.lifesimulator.data.database.dao

import androidx.room.*
import com.popop.lifesimulator.data.database.entity.SocialCircleEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Social Circle entities.
 */
@Dao
interface SocialCircleDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(circle: SocialCircleEntity)
    
    @Update
    suspend fun update(circle: SocialCircleEntity)
    
    @Delete
    suspend fun delete(circle: SocialCircleEntity)
    
    @Query("SELECT * FROM social_circles WHERE id = :circleId")
    suspend fun getById(circleId: String): SocialCircleEntity?
    
    @Query("SELECT * FROM social_circles WHERE circleType = :type")
    suspend fun getByType(type: String): List<SocialCircleEntity>
    
    @Query("SELECT * FROM social_circles WHERE leaderId = :npcId")
    suspend fun getLedBy(npcId: String): List<SocialCircleEntity>
    
    @Query("SELECT * FROM social_circles WHERE memberIds LIKE '%' || :npcId || '%'")
    suspend fun getForMember(npcId: String): List<SocialCircleEntity>
    
    @Query("SELECT * FROM social_circles WHERE meetingLocationId = :locationId")
    suspend fun getByLocation(locationId: String): List<SocialCircleEntity>
    
    @Query("SELECT * FROM social_circles WHERE cohesion >= :minCohesion")
    suspend fun getCohesiveCircles(minCohesion: Int): List<SocialCircleEntity>
    
    @Query("UPDATE social_circles SET memberIds = :members WHERE id = :circleId")
    suspend fun updateMembers(circleId: String, members: String)
    
    @Query("UPDATE social_circles SET gossipPool = :gossip WHERE id = :circleId")
    suspend fun updateGossip(circleId: String, gossip: String)
    
    @Query("UPDATE social_circles SET lastUpdatedAt = :timestamp WHERE id = :circleId")
    suspend fun updateTimestamp(circleId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM social_circles WHERE id = :circleId")
    suspend fun deleteById(circleId: String)
    
    @Query("DELETE FROM social_circles")
    suspend fun deleteAll()
}
