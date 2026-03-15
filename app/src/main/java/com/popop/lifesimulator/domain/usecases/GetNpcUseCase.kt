package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.domain.repositories.NpcRepository

/**
 * Use case for getting an NPC by ID
 */
class GetNpcUseCase(private val repository: NpcRepository) {
    suspend fun execute(id: Long) = repository.getNpcById(id)
}
