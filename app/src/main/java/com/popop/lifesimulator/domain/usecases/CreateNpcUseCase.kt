package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.data.models.relationship.Npc
import com.popop.lifesimulator.domain.repositories.NpcRepository

/**
 * Use case for creating a new NPC
 */
class CreateNpcUseCase(private val repository: NpcRepository) {
    suspend fun execute(npc: Npc) {
        repository.insertNpc(npc)
    }
}
