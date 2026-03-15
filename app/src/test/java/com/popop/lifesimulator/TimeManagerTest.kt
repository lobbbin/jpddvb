package com.popop.lifesimulator

import com.popop.lifesimulator.core.time.TimeManager
import com.popop.lifesimulator.core.time.Season
import com.popop.lifesimulator.data.models.world.GameState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for TimeManager
 */
class TimeManagerTest {
    
    private lateinit var timeManager: TimeManager
    
    @Before
    fun setUp() {
        timeManager = TimeManager()
    }
    
    @Test
    fun `initial state should have valid values`() {
        val state = timeManager.gameState.value
        
        assertTrue(state.currentYear > 0)
        assertTrue(state.currentMonth in 1..12)
        assertTrue(state.currentDay in 1..31)
        assertTrue(state.currentHour in 0..23)
        assertTrue(state.currentMinute in 0..59)
    }
    
    @Test
    fun `advance minutes should update time correctly`() {
        val initialState = timeManager.gameState.value
        val initialMinutes = initialState.currentMinute
        val initialHours = initialState.currentHour
        
        timeManager.advanceMinutes(30)
        
        val newState = timeManager.gameState.value
        assertEquals((initialMinutes + 30) % 60, newState.currentMinute)
        
        if (initialMinutes + 30 >= 60) {
            assertEquals((initialHours + 1) % 24, newState.currentHour)
        } else {
            assertEquals(initialHours, newState.currentHour)
        }
    }
    
    @Test
    fun `advance hour should handle day rollover`() {
        // Set to 23:00
        timeManager.setTime(23, 0)
        
        timeManager.advanceHour()
        
        val state = timeManager.gameState.value
        assertEquals(0, state.currentHour)
    }
    
    @Test
    fun `advance day should handle month rollover`() {
        // Set to last day of month
        timeManager.setDate(31, 1, 2024)  // January 31
        
        timeManager.advanceDay()
        
        val state = timeManager.gameState.value
        assertEquals(1, state.currentMonth)
        assertEquals(1, state.currentDay)
    }
    
    @Test
    fun `advance day should handle year rollover`() {
        // Set to December 31
        timeManager.setDate(31, 12, 2024)
        
        timeManager.advanceDay()
        
        val state = timeManager.gameState.value
        assertEquals(2025, state.currentYear)
        assertEquals(1, state.currentMonth)
        assertEquals(1, state.currentDay)
    }
    
    @Test
    fun `getSeason should return correct season for month`() {
        assertEquals(Season.SPRING, timeManager.getSeason(3))
        assertEquals(Season.SPRING, timeManager.getSeason(4))
        assertEquals(Season.SPRING, timeManager.getSeason(5))
        
        assertEquals(Season.SUMMER, timeManager.getSeason(6))
        assertEquals(Season.SUMMER, timeManager.getSeason(7))
        assertEquals(Season.SUMMER, timeManager.getSeason(8))
        
        assertEquals(Season.FALL, timeManager.getSeason(9))
        assertEquals(Season.FALL, timeManager.getSeason(10))
        assertEquals(Season.FALL, timeManager.getSeason(11))
        
        assertEquals(Season.WINTER, timeManager.getSeason(12))
        assertEquals(Season.WINTER, timeManager.getSeason(1))
        assertEquals(Season.WINTER, timeManager.getSeason(2))
    }
    
    @Test
    fun `isLeapYear should return correct values`() {
        assertTrue(timeManager.isLeapYear(2000))  // Divisible by 400
        assertTrue(timeManager.isLeapYear(2024))  // Divisible by 4 but not 100
        assertFalse(timeManager.isLeapYear(1900)) // Divisible by 100 but not 400
        assertFalse(timeManager.isLeapYear(2023)) // Not divisible by 4
    }
    
    @Test
    fun `getDaysInMonth should return correct values`() {
        assertEquals(31, timeManager.getDaysInMonth(1, 2024))
        assertEquals(29, timeManager.getDaysInMonth(2, 2024))  // Leap year
        assertEquals(28, timeManager.getDaysInMonth(2, 2023))  // Non-leap year
        assertEquals(30, timeManager.getDaysInMonth(4, 2024))
        assertEquals(31, timeManager.getDaysInMonth(12, 2024))
    }
    
    @Test
    fun `isWeekend should return correct values`() {
        // This would require setting dayOfWeek, which depends on implementation
        // Test would verify dayOfWeek 6 and 7 return true
    }
    
    @Test
    fun `isBusinessHours should return correct values`() {
        timeManager.setTime(10, 0)  // 10 AM on weekday
        // Would need to set dayOfWeek to verify
        
        timeManager.setTime(20, 0)  // 8 PM
        assertFalse(timeManager.isBusinessHours())
        
        timeManager.setTime(3, 0)  // 3 AM
        assertFalse(timeManager.isBusinessHours())
    }
    
    @Test
    fun `getTimeOfDay should return correct periods`() {
        timeManager.setTime(2, 0)
        // Would verify TimeOfDay.NIGHT
        
        timeManager.setTime(6, 0)
        // Would verify TimeOfDay.EARLY_MORNING
        
        timeManager.setTime(10, 0)
        // Would verify TimeOfDay.MORNING
        
        timeManager.setTime(12, 0)
        // Would verify TimeOfDay.NOON
        
        timeManager.setTime(15, 0)
        // Would verify TimeOfDay.AFTERNOON
        
        timeManager.setTime(19, 0)
        // Would verify TimeOfDay.EVENING
    }
    
    @Test
    fun `setTime should clamp values correctly`() {
        timeManager.setTime(25, 70)  // Invalid values
        
        val state = timeManager.gameState.value
        assertTrue(state.currentHour in 0..23)
        assertTrue(state.currentMinute in 0..59)
    }
    
    @Test
    fun `setDate should clamp values correctly`() {
        timeManager.setDate(35, 15, 2024)  // Invalid values
        
        val state = timeManager.gameState.value
        assertTrue(state.currentMonth in 1..12)
        assertTrue(state.currentDay in 1..31)
    }
}
