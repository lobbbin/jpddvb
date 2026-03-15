package com.popop.lifesimulator.core.time

import com.popop.lifesimulator.data.models.world.GameState
import com.popop.lifesimulator.data.models.world.Season
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages game time progression and calendar systems
 */
class TimeManager {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    // Time scales
    var timeScale: TimeScale = TimeScale.NORMAL
        private set
    
    // Callbacks
    var onTimePassed: ((GameState) -> Unit)? = null
    var onDayChanged: ((GameState) -> Unit)? = null
    var onMonthChanged: ((GameState) -> Unit)? = null
    var onYearChanged: ((GameState) -> Unit)? = null
    var onSeasonChanged: ((GameState) -> Unit)? = null
    
    /**
     * Initialize time manager with saved state
     */
    fun initialize(savedState: GameState?) {
        savedState?.let { _gameState.value = it }
    }
    
    /**
     * Advance time by specified minutes
     */
    fun advanceMinutes(minutes: Int) {
        val state = _gameState.value
        var newMinutes = state.currentMinute + minutes
        var newHours = state.currentHour
        var daysToAdd = 0
        
        // Handle minute overflow
        while (newMinutes >= 60) {
            newMinutes -= 60
            newHours++
        }
        
        // Handle hour overflow
        while (newHours >= 24) {
            newHours -= 24
            daysToAdd++
        }
        
        // Update state
        var newState = state.copy(
            currentMinute = newMinutes,
            currentHour = newHours,
            updatedAt = System.currentTimeMillis(),
            lastTickAt = System.currentTimeMillis()
        )
        
        // Advance days if needed
        if (daysToAdd > 0) {
            repeat(daysToAdd) {
                newState = advanceDayInternal(newState)
            }
        }
        
        _gameState.value = newState
        onTimePassed?.invoke(newState)
    }
    
    /**
     * Advance time by one hour
     */
    fun advanceHour() {
        advanceMinutes(60)
    }
    
    /**
     * Advance time by one day
     */
    fun advanceDay() {
        val state = _gameState.value
        val newState = advanceDayInternal(state)
        _gameState.value = newState
        onTimePassed?.invoke(newState)
    }
    
    private fun advanceDayInternal(state: GameState): GameState {
        var newDay = state.currentDay + 1
        var newMonth = state.currentMonth
        var newYear = state.currentYear
        var newDayOfWeek = state.dayOfWeek + 1
        var monthChanged = false
        var yearChanged = false
        var seasonChanged = false
        
        // Handle day overflow
        val daysInMonth = getDaysInMonth(state.currentMonth, state.currentYear)
        if (newDay > daysInMonth) {
            newDay = 1
            newMonth++
            monthChanged = true
            
            if (newMonth > 12) {
                newMonth = 1
                newYear++
                yearChanged = true
            }
        }
        
        // Handle day of week overflow
        if (newDayOfWeek > 7) {
            newDayOfWeek = 1
        }
        
        // Calculate new season
        val newSeason = getSeason(newMonth)
        if (newSeason != state.season) {
            seasonChanged = true
        }
        
        var newState = state.copy(
            currentDay = newDay,
            currentMonth = newMonth,
            currentYear = newYear,
            dayOfWeek = newDayOfWeek,
            season = newSeason,
            updatedAt = System.currentTimeMillis()
        )
        
        // Trigger callbacks
        if (monthChanged) onMonthChanged?.invoke(newState)
        if (yearChanged) onYearChanged?.invoke(newState)
        if (seasonChanged) onSeasonChanged?.invoke(newState)
        onDayChanged?.invoke(newState)
        
        return newState
    }
    
    /**
     * Advance time by one week
     */
    fun advanceWeek() {
        repeat(7) { advanceDay() }
    }
    
    /**
     * Advance time by one month
     */
    fun advanceMonth() {
        val days = getDaysInMonth(_gameState.value.currentMonth, _gameState.value.currentYear)
        repeat(days) { advanceDay() }
    }
    
    /**
     * Set time to specific hour and minute
     */
    fun setTime(hour: Int, minute: Int) {
        val state = _gameState.value
        _gameState.value = state.copy(
            currentHour = hour.coerceIn(0, 23),
            currentMinute = minute.coerceIn(0, 59),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    /**
     * Set date to specific day, month, year
     */
    fun setDate(day: Int, month: Int, year: Int) {
        val state = _gameState.value
        val maxDay = getDaysInMonth(month, year)
        _gameState.value = state.copy(
            currentDay = day.coerceIn(1, maxDay),
            currentMonth = month.coerceIn(1, 12),
            currentYear = year,
            season = getSeason(month),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    /**
     * Get days in a specific month
     */
    fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }
    
    /**
     * Check if year is a leap year
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
    
    /**
     * Get season from month
     */
    fun getSeason(month: Int): Season {
        return when (month) {
            3, 4, 5 -> Season.SPRING
            6, 7, 8 -> Season.SUMMER
            9, 10, 11 -> Season.FALL
            else -> Season.WINTER
        }
    }
    
    /**
     * Get current season name
     */
    fun getCurrentSeasonName(): String = _gameState.value.getSeasonString()
    
    /**
     * Check if current time is in business hours
     */
    fun isBusinessHours(): Boolean = _gameState.value.isBusinessHours()
    
    /**
     * Check if current day is weekend
     */
    fun isWeekend(): Boolean = _gameState.value.isWeekend()
    
    /**
     * Get time of day description
     */
    fun getTimeOfDay(): TimeOfDay {
        val hour = _gameState.value.currentHour
        return when (hour) {
            in 0..4 -> TimeOfDay.NIGHT
            in 5..7 -> TimeOfDay.EARLY_MORNING
            in 8..11 -> TimeOfDay.MORNING
            12, 13 -> TimeOfDay.NOON
            in 14..17 -> TimeOfDay.AFTERNOON
            in 18..20 -> TimeOfDay.EVENING
            else -> TimeOfDay.NIGHT
        }
    }
    
    /**
     * Set time scale for time progression
     */
    fun setTimeScale(scale: TimeScale) {
        timeScale = scale
    }
    
    /**
     * Get formatted date string
     */
    fun getFormattedDate(): String = _gameState.value.getDateString()
    
    /**
     * Get formatted date time string
     */
    fun getFormattedDateTime(): String = _gameState.value.getDateTimeString()
    
    /**
     * Calculate age from birth timestamp
     */
    fun calculateAge(birthTimestamp: Long): Int {
        val birthYear = java.util.Date(birthTimestamp).year + 1900
        return _gameState.value.currentYear - birthYear
    }
    
    /**
     * Get time until specific hour
     */
    fun getMinutesUntil(targetHour: Int, targetMinute: Int = 0): Int {
        val state = _gameState.value
        val currentMinutes = state.currentHour * 60 + state.currentMinute
        val targetMinutes = targetHour * 60 + targetMinute
        
        return if (targetMinutes > currentMinutes) {
            targetMinutes - currentMinutes
        } else {
            (24 * 60 - currentMinutes) + targetMinutes
        }
    }
}

enum class TimeScale(val minutesPerRealSecond: Int) {
    PAUSED(0),
    SLOW(1),
    NORMAL(10),
    FAST(60),
    ULTRA_FAST(360)
}

enum class TimeOfDay(val displayName: String) {
    NIGHT("Night"),
    EARLY_MORNING("Early Morning"),
    MORNING("Morning"),
    NOON("Noon"),
    AFTERNOON("Afternoon"),
    EVENING("Evening")
}

/**
 * Special dates and holidays
 */
object HolidayCalendar {
    
    data class Holiday(
        val name: String,
        val month: Int,
        val day: Int,
        val isFixed: Boolean = true,
        val description: String = ""
    )
    
    val HOLIDAYS = listOf(
        Holiday("New Year's Day", 1, 1, description = "Start of a new year"),
        Holiday("Valentine's Day", 2, 14, description = "Day of love and romance"),
        Holiday("St. Patrick's Day", 3, 17, description = "Irish cultural celebration"),
        Holiday("Independence Day", 7, 4, description = "National independence celebration"),
        Holiday("Halloween", 10, 31, description = "Spooky costumes and treats"),
        Holiday("Christmas", 12, 25, description = "Winter holiday celebration"),
        Holiday("New Year's Eve", 12, 31, description = "End of year celebration")
    )
    
    fun isHoliday(month: Int, day: Int): Holiday? {
        return HOLIDAYS.find { it.month == month && it.day == day }
    }
    
    fun getUpcomingHolidays(currentMonth: Int, currentDay: Int, count: Int = 3): List<Holiday> {
        val currentDayOfYear = currentMonth * 30 + currentDay  // Simplified
        return HOLIDAYS
            .map { it to (it.month * 30 + it.day) }
            .filter { it.second >= currentDayOfYear }
            .sortedBy { it.second }
            .take(count)
            .map { it.first }
    }
}
