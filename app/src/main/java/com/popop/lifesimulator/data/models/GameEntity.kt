package com.popop.lifesimulator.data.models

/**
 * Base interface for all game entities that need persistence
 */
interface GameEntity {
    val id: Long
    val createdAt: Long
    val updatedAt: Long
}

/**
 * Base class for entities with auto-generated IDs
 */
abstract class BaseGameEntity(
    override val id: Long = 0L,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis()
) : GameEntity
