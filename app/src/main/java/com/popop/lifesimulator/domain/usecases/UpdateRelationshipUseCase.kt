package com.popop.lifesimulator.domain.usecases

import com.popop.lifesimulator.data.models.relationship.Relationship
import com.popop.lifesimulator.domain.repositories.NpcRepository

/**
 * Use case for updating a relationship
 */
class UpdateRelationshipUseCase(private val repository: NpcRepository) {
    suspend fun execute(relationship: Relationship) {
        repository.updateRelationship(relationship)
    }
}
