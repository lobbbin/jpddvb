package com.popop.lifesimulator.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.TimeZone

/**
 * Manages game time progression and calendar system.
 * Supports configurable time scales, seasons, and special dates.
 * 
 * Time scale: 1 real second = 1 game minute (configurable)
 * 60 game minutes = 1 game hour
 * 24 game hours = 1 game day
 * 30 game days = 1 game month
 * 12 game months = 1 game year
 */
class TimeManager {
    
    // Time configuration
    var timeScaleSeconds: Int = 1 // Real seconds per game minute
        private set
    
    // Current game time
    private var gameYear: Int = 1
    private var gameMonth: Int = 1 // 1-12
    private var gameDay: Int = 1 // 1-30
    private var gameHour: Int = 8 // 0-23
    private var gameMinute: Int = 0 // 0-59
    
    // Time tracking
    private var totalMinutesPlayed: Long = 0
    private var totalDaysPlayed: Int = 0
    private var gameStartTime: Long = System.currentTimeMillis()
    private var lastUpdateTime: Long = System.currentTimeMillis()
    
    // Season tracking
    private val seasonMonths = mapOf(
        Season.SPRING to (1..3),
        Season.SUMMER to (4..6),
        Season.AUTUMN to (7..9),
        Season.WINTER to (10..12)
    )
    
    // State flow for UI updates
    private val _timeState = MutableStateFlow(TimeState())
    val timeState: StateFlow<TimeState> = _timeState.asStateFlow()
    
    // Special dates and events
    private val specialDates = mutableMapOf<String, SpecialDate>()
    
    /**
     * Initialize the time manager with starting date.
     */
    fun initialize(
        year: Int = 1,
        month: Int = 1,
        day: Int = 1,
        hour: Int = 8,
        timeScale: Int = 1
    ) {
        gameYear = year
        gameMonth = month.coerceIn(1, 12)
        gameDay = day.coerceIn(1, 30)
        gameHour = hour.coerceIn(0, 23)
        gameMinute = 0
        timeScaleSeconds = timeScale.coerceAtLeast(1)
        gameStartTime = System.currentTimeMillis()
        lastUpdateTime = System.currentTimeMillis()
        totalMinutesPlayed = 0
        totalDaysPlayed = 0
        
        updateTimeState()
    }
    
    /**
     * Advance game time by specified minutes.
     */
    fun advanceTime(minutes: Int) {
        var remainingMinutes = minutes
        
        // Add to total tracking
        totalMinutesPlayed += minutes
        
        // Advance minutes
        gameMinute += remainingMinutes
        
        // Handle minute overflow
        while (gameMinute >= 60) {
            gameMinute -= 60
            gameHour++
            
            // Handle hour overflow
            if (gameHour >= 24) {
                gameHour -= 24
                gameDay++
                totalDaysPlayed++
                
                // Handle day overflow
                if (gameDay > 30) {
                    gameDay -= 30
                    gameMonth++
                    
                    // Handle month overflow
                    if (gameMonth > 12) {
                        gameMonth -= 12
                        gameYear++
                    }
                    
                    // Check for month events
                    onMonthChanged()
                }
                
                // Check for day events
                onDayChanged()
            }
        }
        
        lastUpdateTime = System.currentTimeMillis()
        updateTimeState()
    }
    
    /**
     * Advance time based on real elapsed time.
     * Call this periodically (e.g., every second).
     */
    fun updateFromRealTime() {
        val currentTime = System.currentTimeMillis()
        val elapsedSeconds = (currentTime - lastUpdateTime) / 1000
        
        if (elapsedSeconds > 0) {
            val gameMinutesToAdd = (elapsedSeconds * timeScaleSeconds).toInt()
            advanceTime(gameMinutesToAdd)
        }
    }
    
    /**
     * Set time to a specific hour and minute.
     */
    fun setTime(hour: Int, minute: Int) {
        gameHour = hour.coerceIn(0, 23)
        gameMinute = minute.coerceIn(0, 59)
        updateTimeState()
    }
    
    /**
     * Set date to a specific day, month, year.
     */
    fun setDate(day: Int, month: Int, year: Int) {
        gameDay = day.coerceIn(1, 30)
        gameMonth = month.coerceIn(1, 12)
        gameYear = year.coerceAtLeast(1)
        updateTimeState()
    }
    
    /**
     * Get current season based on month.
     */
    fun getCurrentSeason(): Season {
        return seasonMonths.entries
            .find { it.value.contains(gameMonth) }
            ?.key ?: Season.SPRING
    }
    
    /**
     * Check if current date matches a special date.
     */
    fun checkSpecialDate(): SpecialDate? {
        return specialDates.values.find { it.matches(gameMonth, gameDay) }
    }
    
    /**
     * Register a special date (holiday, event, etc.).
     */
    fun registerSpecialDate(name: String, month: Int, day: Int, description: String = "") {
        specialDates[name] = SpecialDate(name, month, day, description)
    }
    
    /**
     * Get time of day description.
     */
    fun getTimeOfDay(): TimeOfDay {
        return when (gameHour) {
            in 0..4 -> TimeOfDay.NIGHT
            in 5..6 -> TimeOfDay.DAWN
            in 7..11 -> TimeOfDay.MORNING
            12 -> TimeOfDay.NOON
            in 13..16 -> TimeOfDay.AFTERNOON
            in 17..19 -> TimeOfDay.EVENING
            in 20..23 -> TimeOfDay.NIGHT
            else -> TimeOfDay.MORNING
        }
    }
    
    /**
     * Get formatted date string.
     */
    fun getFormattedDate(): String {
        return "Year $gameYear, ${getMonthName(gameMonth)} $gameDay"
    }
    
    /**
     * Get formatted time string.
     */
    fun getFormattedTime(): String {
        return String.format("%02d:%02d", gameHour, gameMinute)
    }
    
    /**
     * Get full datetime string.
     */
    fun getFormattedDateTime(): String {
        return "${getFormattedDate()} - ${getFormattedTime()}"
    }
    
    /**
     * Get timestamp as Unix-like value for save files.
     */
    fun getTimestamp(): Long {
        return ((gameYear.toLong() * 12 + gameMonth) * 30 + gameDay) * 24 * 60 + gameHour * 60 + gameMinute
    }
    
    /**
     * Set time from timestamp.
     */
    fun setFromTimestamp(timestamp: Long) {
        var remaining = timestamp
        
        gameMinute = (remaining % 60).toInt()
        remaining /= 60
        
        gameHour = (remaining % 24).toInt()
        remaining /= 24
        
        gameDay = (remaining % 30).toInt() + 1
        remaining /= 30
        
        gameMonth = (remaining % 12).toInt() + 1
        gameYear = (remaining / 12).toInt()
        
        updateTimeState()
    }
    
    /**
     * Calculate days until a target date.
     */
    fun daysUntil(targetMonth: Int, targetDay: Int): Int {
        val currentTotal = gameMonth * 30 + gameDay
        var targetTotal = targetMonth * 30 + targetDay
        
        if (targetTotal < currentTotal) {
            targetTotal += 360 // Add a year
        }
        
        return targetTotal - currentTotal
    }
    
    /**
     * Check if it's currently night time.
     */
    fun isNight(): Boolean = gameHour < 6 || gameHour >= 20
    
    /**
     * Check if it's currently business hours.
     */
    fun isBusinessHours(): Boolean = gameHour in 9..17
    
    /**
     * Get the current month name.
     */
    private fun getMonthName(month: Int): String {
        val names = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return names.getOrNull(month - 1) ?: "Unknown"
    }
    
    private fun updateTimeState() {
        _timeState.value = TimeState(
            year = gameYear,
            month = gameMonth,
            day = gameDay,
            hour = gameHour,
            minute = gameMinute,
            season = getCurrentSeason(),
            timeOfDay = getTimeOfDay(),
            totalDaysPlayed = totalDaysPlayed,
            formattedDate = getFormattedDate(),
            formattedTime = getFormattedTime()
        )
    }
    
    private fun onDayChanged() {
        // Can be extended to trigger daily events
    }
    
    private fun onMonthChanged() {
        // Can be extended to trigger monthly events
    }
    
    // Data classes
    data class TimeState(
        val year: Int = 1,
        val month: Int = 1,
        val day: Int = 1,
        val hour: Int = 0,
        val minute: Int = 0,
        val season: Season = Season.SPRING,
        val timeOfDay: TimeOfDay = TimeOfDay.MORNING,
        val totalDaysPlayed: Int = 0,
        val formattedDate: String = "",
        val formattedTime: String = ""
    )
    
    data class SpecialDate(
        val name: String,
        val month: Int,
        val day: Int,
        val description: String = ""
    ) {
        fun matches(month: Int, day: Int): Boolean = this.month == month && this.day == day
    }
}

/**
 * Enum for seasons.
 */
enum class Season(val displayName: String, val icon: String) {
    SPRING("Spring", "🌸"),
    SUMMER("Summer", "☀️"),
    AUTUMN("Autumn", "🍂"),
    WINTER("Winter", "❄️")
}

/**
 * Enum for time of day.
 */
enum class TimeOfDay(val displayName: String, val icon: String) {
    DAWN("Dawn", "🌅"),
    MORNING("Morning", "🌄"),
    NOON("Noon", "☀️"),
    AFTERNOON("Afternoon", "🌤️"),
    EVENING("Evening", "🌆"),
    NIGHT("Night", "🌙")
}
