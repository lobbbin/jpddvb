# Development Guide

## Getting Started

This guide helps you continue development of the Ultimate Life Simulator.

## Current Status

**Completed: Phases 0-4, Partial Phase 5-6**
- ✅ Core architecture and database
- ✅ Character stats, skills, traits
- ✅ World state (locations, factions, economy)
- ✅ Relationship system with NPC generator
- ✅ Time management and calendar
- ✅ Event system foundation
- ✅ UI foundation with Compose

**Remaining: Phases 5-14**
- ❌ Complete event implementation (75+ events)
- ❌ Royalty life path (89 features)
- ❌ Politics life path (89 features)
- ❌ Crime/Prison life path (99 features)
- ❌ Business/Tycoon life path (150 features)
- ❌ Career/Education life path (250 features)
- ❌ System integration
- ❌ Testing and polish

## File Organization

### Adding New Features

1. **Data Models** → `data/models/`
2. **Database** → `data/database/`
3. **Business Logic** → `core/` or `domain/`
4. **UI Screens** → `ui/screens/`
5. **ViewModels** → `ui/viewmodels/` (create this folder)

### Example: Adding a New Skill

```kotlin
// In data/models/character/Skills.kt
enum class SkillType {
    // ... existing skills ...
    NEW_SKILL("New Skill", SkillCategory.INTELLECTUAL)
}
```

### Example: Adding a New Event

```kotlin
// In core/events/RandomEventGenerator.kt
object EventTemplates {
    val NEW_EVENT = GameEvent(
        id = "new_event",
        title = "Event Title",
        description = "Description",
        category = EventCategory.GENERAL,
        narrativeText = "Story text...",
        choices = listOf(
            EventChoice(
                id = "choice1",
                text = "Choice text",
                successOutcome = EventOutcome(
                    narrativeText = "Result text...",
                    statChanges = mapOf("health" to 10)
                )
            )
        )
    )
}
```

## Key Systems to Implement Next

### 1. Health System (Phase 4)

Location: `data/models/character/Health.kt` (create)

```kotlin
data class HealthState(
    val injuries: List<Injury>,
    val illnesses: List<Illness>,
    val mentalHealth: MentalHealthState,
    val conditions: List<Condition>
)
```

### 2. Complete Event System

Add events to `EventTemplates.ALL_EVENTS` list and register in `RandomEventGenerator`.

### 3. Life Path Mechanics

Create separate managers for each life path:
- `core/royalty/RoyaltyManager.kt`
- `core/politics/PoliticsManager.kt`
- `core/crime/CrimeManager.kt`
- `core/business/BusinessManager.kt`
- `core/career/CareerManager.kt`

### 4. ViewModels

Create ViewModels for each screen:
- `ui/viewmodels/GameViewModel.kt`
- `ui/viewmodels/CharacterViewModel.kt`
- `ui/viewmodels/RelationshipViewModel.kt`

## Database Migrations

When modifying Room entities:

```kotlin
@Database(
    entities = [...],
    version = 2,  // Increment version
)
abstract class GameDatabase : RoomDatabase() {
    // Add migration
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE characters ADD COLUMN new_column TEXT")
            }
        }
    }
}
```

## Testing

### Unit Tests (app/src/test/)

```kotlin
class TimeManagerTest {
    @Test
    fun `advance day should increment day`() {
        val timeManager = TimeManager()
        timeManager.advanceDay()
        // Assert day changed
    }
}
```

### UI Tests (app/src/androidTest/)

```kotlin
@RunWith(AndroidJUnit4::class)
class GameScreenTest {
    @Test
    fun `display character stats`() {
        // Compose UI test
    }
}
```

## Common Tasks

### Adding a New Location Type

```kotlin
// In data/models/world/Location.kt
enum class LocationType {
    // ... existing ...
    NEW_LOCATION("New Location")
}
```

### Adding a New Faction

```kotlin
// In data/models/world/Faction.kt
object FactionRegistry {
    val NEW_FACTION = Faction(
        name = "Faction Name",
        description = "Description",
        category = FactionCategory.CRIMINAL,
        powerLevel = 50
    )
}
```

### Adding a New Trait

```kotlin
// In data/models/character/Traits.kt
object TraitRegistry {
    val NEW_TRAIT = Trait(
        id = "new_trait",
        displayName = "New Trait",
        description = "Description",
        category = TraitCategory.POSITIVE,
        effects = listOf(
            TraitEffect.StatModifier("charisma", 10)
        )
    )
}
```

## Performance Tips

1. **Use Flow** for reactive UI updates
2. **Lazy load** large lists
3. **Cache** frequently accessed data
4. **Use Room** @Query for filtered data
5. **Background threads** for heavy operations

## Code Style

- Follow Kotlin conventions
- Use meaningful names
- Add KDoc for public APIs
- Keep functions small and focused
- Use sealed classes for state

## Debug Tools

### Enable Logging

```kotlin
// In BuildConfig
buildConfigField "Boolean", "ENABLE_LOGGING", "true"
```

### Debug Menu

Create a debug screen for:
- Adding/removing stats
- Unlocking all locations
- Triggering events
- Modifying relationships

## Resources

- [Android Developers](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt](https://dagger.dev/hilt/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

## Next Sprint Priorities

1. **Health System** - Implement injuries, illnesses, mental health
2. **Event Content** - Write remaining 70+ events
3. **Game Loop** - Create main game tick/update cycle
4. **Save/Load** - Implement full game state serialization
5. **UI Polish** - Improve screens with animations

## Contact

For questions or collaboration, refer to the project documentation.
