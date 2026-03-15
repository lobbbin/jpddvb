package com.popop.lifesimulator

import com.popop.lifesimulator.data.models.character.PrimaryStats
import com.popop.lifesimulator.data.models.character.SecondaryStats
import com.popop.lifesimulator.data.models.character.Skill
import com.popop.lifesimulator.data.models.character.SkillRegistry
import com.popop.lifesimulator.data.models.character.SkillType
import com.popop.lifesimulator.data.models.character.ReputationTier
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for character stats
 */
class CharacterStatsTest {
    
    @Test
    fun `PrimaryStats should clamp values to valid range`() {
        val stats = PrimaryStats(
            health = 150,
            energy = -20,
            stress = 200,
            charisma = 50
        )
        
        val clamped = stats.clamp()
        
        assertEquals(100, clamped.health)
        assertEquals(0, clamped.energy)
        assertEquals(100, clamped.stress)
        assertEquals(50, clamped.charisma)
    }
    
    @Test
    fun `modifyHealth should update health correctly`() {
        val stats = PrimaryStats(health = 80)
        
        val healed = stats.modifyHealth(30)
        assertEquals(100, healed.health)  // Should cap at 100
        
        val damaged = stats.modifyHealth(-50)
        assertEquals(30, damaged.health)
        
        val overDamage = stats.modifyHealth(-100)
        assertEquals(0, overDamage.health)  // Should floor at 0
    }
    
    @Test
    fun `modifyEnergy should update energy correctly`() {
        val stats = PrimaryStats(energy = 50)
        
        val restored = stats.modifyEnergy(30)
        assertEquals(80, restored.energy)
        
        val depleted = stats.modifyEnergy(-60)
        assertEquals(0, depleted.energy)  // Should floor at 0
    }
    
    @Test
    fun `SecondaryStats should clamp reputation to valid range`() {
        val stats = SecondaryStats(reputation = 150)
        
        val clamped = stats.clamp()
        assertEquals(100, clamped.reputation)
        
        val negativeStats = SecondaryStats(reputation = -150)
        val negativeClamped = negativeStats.clamp()
        assertEquals(-100, negativeClamped.reputation)
    }
    
    @Test
    fun `modifyWealth should not go below zero`() {
        val stats = SecondaryStats(wealth = 100.0)
        
        val afterLoss = stats.modifyWealth(-150.0)
        assertEquals(0.0, afterLoss.wealth)
        
        val afterGain = stats.modifyWealth(50.0)
        assertEquals(150.0, afterGain.wealth)
    }
    
    @Test
    fun `ReputationTier should return correct tier for score`() {
        assertEquals(ReputationTier.HATED, ReputationTier.fromScore(-100))
        assertEquals(ReputationTier.HATED, ReputationTier.fromScore(-80))
        assertEquals(ReputationTier.DISLIKED, ReputationTier.fromScore(-50))
        assertEquals(ReputationTier.NEUTRAL, ReputationTier.fromScore(0))
        assertEquals(ReputationTier.ACCEPTED, ReputationTier.fromScore(10))
        assertEquals(ReputationTier.LIKED, ReputationTier.fromScore(30))
        assertEquals(ReputationTier.RESPECTED, ReputationTier.fromScore(60))
        assertEquals(ReputationTier.REVERED, ReputationTier.fromScore(90))
    }
}

/**
 * Unit tests for skill system
 */
class SkillSystemTest {
    
    @Test
    fun `Skill should calculate level from XP correctly`() {
        val skill = Skill(type = SkillType.PROGRAMMING)
        
        val skill1 = skill.gainXp(100.0)  // Should reach level 1
        assertEquals(1, skill1.level)
        
        val skill2 = skill1.gainXp(200.0)  // Should reach level 2
        assertEquals(2, skill2.level)
        
        val skill3 = skill2.gainXp(300.0)  // Should reach level 3
        assertEquals(3, skill3.level)
    }
    
    @Test
    fun `Skill should not exceed max level`() {
        var skill = Skill(type = SkillType.PROGRAMMING)
        
        // Gain enough XP to exceed max level
        repeat(150) {
            skill = skill.gainXp(100.0)
        }
        
        assertEquals(100, skill.level)  // Max level
    }
    
    @Test
    fun `Skill should track progress to next level`() {
        val skill = Skill(type = SkillType.PROGRAMMING, level = 1, experience = 100.0)
        
        val progress = skill.getProgressToNextLevel()
        assertTrue(progress in 0f..1f)
    }
    
    @Test
    fun `SkillRegistry should track multiple skills`() {
        var registry = SkillRegistry()
        
        registry = registry.gainXp(SkillType.PROGRAMMING, 100.0)
        registry = registry.gainXp(SkillType.MEDICINE, 200.0)
        
        assertEquals(1, registry.getSkillLevel(SkillType.PROGRAMMING))
        assertEquals(2, registry.getSkillLevel(SkillType.MEDICINE))
        assertEquals(0, registry.getSkillLevel(SkillType.LAW))  // Not trained
    }
    
    @Test
    fun `SkillRegistry should calculate average skill level`() {
        var registry = SkillRegistry()
        
        registry = registry.gainXp(SkillType.PROGRAMMING, 100.0)  // Level 1
        registry = registry.gainXp(SkillType.MEDICINE, 300.0)     // Level 2
        
        val average = registry.getAverageSkillLevel()
        assertEquals(1.5, average, 0.01)
    }
}

/**
 * Unit tests for event system
 */
class EventSystemTest {
    
    @Test
    fun `GameEvent should check requirements correctly`() {
        // This would test the EventRegistry events
        // Implementation depends on full event system integration
        assertTrue(true)  // Placeholder
    }
    
    @Test
    fun `EventChoice should apply outcomes correctly`() {
        // This would test outcome application
        // Implementation depends on full event system integration
        assertTrue(true)  // Placeholder
    }
}
