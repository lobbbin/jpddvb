# Ultimate Life Simulator - Comprehensive Project Context

## Project Overview

**Ultimate Life Simulator** is a feature-rich, text-based life simulation Android game built with Kotlin and Jetpack Compose. Players experience multiple life paths including royalty, politics, crime, business, and career gameplay with deep stat tracking, skill progression, trait systems, and relationship management.

### Repository
- **URL**: https://github.com/lobbbin/jpddvb
- **Branch**: master
- **Visibility**: Public (unlimited free GitHub Actions minutes)

### Key Statistics
| Feature | Count |
|---------|-------|
| Skills | 50+ across 9 categories |
| Traits | 30+ (positive, negative, neutral) |
| Events | 75+ predefined (3 implemented) |
| Locations | 50+ types |
| Factions | 16+ across 5 categories |
| Relationship Types | 40+ |
| Illnesses | 35+ conditions |
| Career Paths | 10 |
| Total Features | 700+ |

---

## Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| Language | Kotlin | 1.9.22 |
| Min SDK | Android | 26 (8.0) |
| Target SDK | Android | 34 (14) |
| Compile SDK | Android | 34 |
| UI Framework | Jetpack Compose | - |
| Design | Material 3 | - |
| Architecture | MVVM | - |
| DI | Hilt | 2.50 |
| Database | Room | 2.6.1 |
| Async | Coroutines + Flow | 1.7.3 |
| Navigation | Navigation Compose | 2.7.7 |
| Background | WorkManager | 2.9.0 |
| Build | Gradle | 8.2.2 |
| JDK | Java | 17 |
| KSP | Kotlin Symbol Processing | 1.9.22-1.0.17 |

---

## Project Structure

```
popop/
├── app/
│   ├── src/main/
│   │   ├── java/com/popop/lifesimulator/
│   │   │   ├── data/
│   │   │   │   ├── models/
│   │   │   │   │   ├── character/
│   │   │   │   │   │   ├── Character.kt          # Main player entity with 20+ stats
│   │   │   │   │   │   ├── Stats.kt              # PrimaryStats, SecondaryStats, ReputationTier
│   │   │   │   │   │   ├── Skills.kt             # 50+ skills, XP system, SkillRegistry
│   │   │   │   │   │   ├── Traits.kt             # 30+ traits with effects, conflicts, synergies
│   │   │   │   │   │   └── Health.kt             # Injuries, illnesses, mental health
│   │   │   │   │   ├── world/
│   │   │   │   │   │   ├── Location.kt           # 50+ location types
│   │   │   │   │   │   ├── Faction.kt            # 16+ factions
│   │   │   │   │   │   ├── GameState.kt          # Global state: time, economy, politics
│   │   │   │   │   │   └── GameLog.kt            # Event logging
│   │   │   │   │   ├── relationship/
│   │   │   │   │   │   ├── Relationship.kt       # 40+ relationship types
│   │   │   │   │   │   └── Npc.kt                # NPC generation
│   │   │   │   │   └── inventory/
│   │   │   │   │       └── Item.kt               # Items and assets
│   │   │   │   └── database/
│   │   │   │       ├── GameDatabase.kt           # Room database (9 DAOs)
│   │   │   │       ├── Converters.kt             # Type converters
│   │   │   │       ├── dao/                      # Data Access Objects
│   │   │   │       └── entity/                   # Room entities
│   │   │   ├── core/
│   │   │   │   ├── time/
│   │   │   │   │   └── TimeManager.kt            # Calendar, seasons, holidays, time progression
│   │   │   │   ├── events/
│   │   │   │   │   ├── RandomEventGenerator.kt   # Event generation with requirements
│   │   │   │   │   └── EventRegistry.kt          # 75+ predefined events
│   │   │   │   ├── lifepaths/
│   │   │   │   │   ├── RoyaltyManager.kt         # 89 royalty features
│   │   │   │   │   ├── PoliticsManager.kt        # 89 politics features
│   │   │   │   │   ├── CrimeManager.kt           # 99 crime/prison features
│   │   │   │   │   ├── BusinessManager.kt        # 150 business features
│   │   │   │   │   └── CareerManager.kt          # 250 career features
│   │   │   │   ├── utilities/
│   │   │   │   │   └── Logger.kt                 # Logging and flag management
│   │   │   │   ├── notifications/
│   │   │   │   │   └── GameNotificationManager.kt # Push notifications
│   │   │   │   └── background/
│   │   │   │       └── GameEventWorker.kt        # WorkManager background tasks
│   │   │   ├── di/
│   │   │   │   ├── AppModule.kt                  # Hilt modules
│   │   │   │   ├── ContextModule.kt
│   │   │   │   ├── DatabaseModule.kt
│   │   │   │   ├── DomainModule.kt
│   │   │   │   └── UseCaseModule.kt
│   │   │   └── ui/
│   │   │       ├── theme/
│   │   │       │   ├── Color.kt                  # Color scheme
│   │   │       │   ├── Type.kt                   # Typography
│   │   │       │   └── Theme.kt                  # Material 3 theme
│   │   │       ├── navigation/
│   │   │       │   ├── Navigation.kt             # Nav routes
│   │   │       │   └── Screen.kt                 # Screen definitions
│   │   │       ├── viewmodels/
│   │   │       │   ├── GameViewModel.kt          # Main game VM
│   │   │       │   └── NavigationViewModel.kt    # Navigation VM
│   │   │       ├── screens/
│   │   │       │   ├── splash/
│   │   │       │   ├── mainmenu/
│   │   │       │   ├── charactercreation/
│   │   │       │   ├── game/
│   │   │       │   │   ├── GameScreen.kt         # Main game UI (5 tabs)
│   │   │       │   │   └── CharacterSheetScreen.kt
│   │   │       │   ├── relationships/
│   │   │       │   ├── inventory/
│   │   │       │   ├── locations/
│   │   │       │   └── factions/
│   │   │       └── MainActivity.kt               # Entry point
│   │   ├── res/
│   │   │   ├── mipmap-*/         # Launcher icons (adaptive)
│   │   │   ├── drawable/
│   │   │   ├── values/           # strings.xml, colors.xml, themes.xml
│   │   │   └── xml/              # backup_rules.xml, data_extraction_rules.xml
│   │   └── AndroidManifest.xml
│   ├── src/test/
│   │   └── java/com/popop/lifesimulator/
│   │       ├── TimeManagerTest.kt
│   │       └── CharacterStatsTest.kt
│   └── build.gradle.kts
├── .github/workflows/
│   └── build-debug.yml           # CI/CD: builds debug APK on push
├── .gitignore                    # Android + IDE exclusions
├── build.gradle.kts              # Root build config
├── settings.gradle.kts           # Project settings
├── gradle.properties             # Gradle properties
├── README.md                     # User-facing documentation
├── DEVELOPMENT.md                # Developer guide
└── QWEN.md                       # This file
```

---

## Building and Running

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean build

# Install on device
./gradlew installDebug
```

### CI/CD (GitHub Actions)

Workflow: `.github/workflows/build-debug.yml`

**Triggers:**
- Push to `master` or `main`
- Pull requests
- Manual dispatch (workflow_dispatch)

**Steps:**
1. Checkout code
2. Setup JDK 17 (Temurin)
3. Setup Gradle
4. Grant gradlew execute permission
5. Build debug APK
6. Upload APK as artifact (`debug-apk`)

**Cost:** Free (unlimited minutes for public repos)

---

## Core Systems Deep Dive

### 1. Character System

#### Primary Stats (10 attributes, 0-100 scale)
```kotlin
data class PrimaryStats(
    val health: Int = 100,      // Life force, 0 = death
    val energy: Int = 100,      // Action points
    val stress: Int = 0,        // Mental strain
    val charisma: Int = 50,     // Social influence
    val intellect: Int = 50,    // Mental acuity
    val cunning: Int = 50,      // Deception ability
    val violence: Int = 50,     // Combat prowess
    val stealth: Int = 50,      // Sneak ability
    val perception: Int = 50,   // Awareness
    val willpower: Int = 50     // Mental fortitude
)
```

#### Secondary Stats (10 derived attributes)
```kotlin
data class SecondaryStats(
    val reputation: Int = 0,        // -100 to +100, affects NPC interactions
    val wealth: Double = 0.0,       // Money
    val piety: Int = 50,            // Religious devotion
    val loyaltyTendency: Int = 50,  // Faithfulness to others
    val addictionLevel: Int = 0,    // Substance dependency
    val heat: Int = 0,              // Law enforcement attention
    val educationLevel: Int = 0,    // Academic achievement
    val streetCred: Int = 0,        // Criminal reputation
    val nobleStanding: Int = 0,     // Aristocratic status
    val politicalCapital: Int = 0   // Political influence
)
```

#### Reputation Tiers
```
HATED (-100) → DISLIKED (-50) → NEUTRAL (-20) → ACCEPTED (0) → 
LIKED (20) → RESPECTED (50) → REVERED (80)
```

### 2. Skill System

**9 Categories, 50+ Skills:**

| Category | Skills |
|----------|--------|
| Social | Public Speaking, Debate, Fundraising, Persuasion, Negotiation, Leadership, Teaching |
| Intellectual | Law, Medicine, Programming, Writing, Mathematics, History, Research, Grant Writing, Diagnosis, Surgery, Trial Advocacy, Legal Research, Contract Drafting |
| Criminal | Criminal Skill, Drug Crafting, Forgery, Lockpicking, Hacking, Stealth |
| Combat | Melee Combat, Ranged Combat, Martial |
| Physical | Fitness, Athletics, Yoga, Meditation |
| Trade | Cooking, Cleaning, Gardening, Repair, Welding, Electrical Work, Plumbing, Carpentry, Masonry, HVAC |
| Creative | Painting, Music |
| Business | Stewardship, Intrigue, Accounting, Marketing, Sales |
| Other | Driving/Piloting, Sports |

**XP System:**
- Base XP per level: `100 × (level + 1)`
- Max level: 100
- Progress tracking with `getProgressToNextLevel()`

### 3. Trait System

**30+ Traits with Effects, Conflicts, and Synergies:**

#### Positive Traits (11)
- **Ambitious**: +10 stress, +15 willpower
- **Charismatic**: +20 charisma, +10 Public Speaking, +10 Persuasion
- **Lucky** (Legendary): 1.5× positive event chance
- **Connected**: +15 political capital
- **Resilient**: +15 willpower, +10 health
- **Just**: +10 reputation
- **Brave**: +10 violence, +10 willpower
- **Diligent**: +5 stress, +10 willpower
- **Athletic**: +15 health, +15 Fitness
- **Bookworm**: +15 intellect, +10 History, +10 Writing
- **Leader**: +10 charisma, +20 Leadership

#### Negative Traits (9)
- **Greedy**: -10 reputation, conflicts with Just/Humble
- **Paranoid**: +15 stress, +10 perception
- **Addict**: -15 health, -10 willpower
- **Hot-Headed**: +10 violence, -5 charisma
- **Cowardly**: -15 violence, +10 stealth
- **Dishonest**: +15 cunning, -10 reputation
- **Sickly**: -20 health, -10 energy
- **Lazy**: -5 stress, -10 willpower
- **Cruel**: +10 violence, -20 reputation

#### Neutral/Quirk Traits (10)
- **Religious**: +20 piety, -5 stress
- **Cynical**: +10 perception, -5 charisma
- **Romantic**: +5 charisma
- **Solitary**: -5 stress, -5 charisma
- **Night Owl**: Nocturnal preference
- **Early Bird**: +5 energy
- **Superstitious**: Believes in omens
- **Pragmatic**: +5 intellect
- **Idealistic**: +5 reputation

**Trait Effects:**
```kotlin
sealed class TraitEffect {
    data class StatModifier(val stat: String, val amount: Int)
    data class StatMultiplier(val stat: String, val multiplier: Double)
    data class SkillBonus(val skillType: SkillType, val bonus: Int)
    data class UnlockAbility(val abilityId: String)
    data class ModifyEventChance(val eventType: String, val modifier: Double)
    object BlockTrait
}
```

### 4. Event System

**GameEvent Structure:**
```kotlin
data class GameEvent(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory,
    val priority: EventPriority,
    
    // Requirements
    val minAge: Int,
    val maxAge: Int,
    val requiredLifePath: String?,
    val requiredLocation: String?,
    val requiredFaction: String?,
    val requiredFlags: Map<String, Any>,
    val forbiddenFlags: List<String>,
    val statRequirements: Map<String, Int>,
    val skillRequirements: Map<SkillType, Int>,
    
    // Content
    val narrativeText: String,
    val choices: List<EventChoice>,
    
    // Mechanics
    val baseChance: Double,
    val cooldown: Long,
    val isRepeatable: Boolean,
    val chainId: String?,
    val chainPosition: Int
)
```

**EventChoice with Skill Checks:**
```kotlin
data class EventChoice(
    val id: String,
    val text: String,
    val skillCheck: SkillCheck?,      // Skill-based challenge
    val statCheck: StatCheck?,        // Stat-based challenge
    val cost: Double,                 // Money cost
    val successOutcome: EventOutcome,
    val failureOutcome: EventOutcome?,
    val autoSuccess: Boolean
)
```

**EventOutcome:**
```kotlin
data class EventOutcome(
    val narrativeText: String,
    val statChanges: Map<String, Int>,
    val skillXp: Map<SkillType, Double>,
    val itemChanges: Map<String, Int>,
    val relationshipChanges: Map<Long, Map<String, Int>>,
    val factionChanges: Map<Long, Int>,
    val flagChanges: Map<String, Any>,
    val nextEventId: String?  // For event chains
)
```

**Implemented Events:**
1. `FOUND_MONEY` - Find wallet (keep/return/donate)
2. `STRANGE_OFFER` - Mysterious job offer
3. `SUDDEN_ILLNESS` - Get sick (rest/doctor/push through)

### 5. Time System

**TimeManager Features:**
- Real-time progression with adjustable scale
- Calendar with seasons (Spring, Summer, Fall, Winter)
- Day/night cycle
- Weekdays/weekends
- Holiday calendar

**Time Scales:**
```kotlin
enum class TimeScale(val minutesPerRealSecond: Int) {
    PAUSED(0),
    SLOW(1),
    NORMAL(10),
    FAST(60),
    ULTRA_FAST(360)
}
```

**Time of Day:**
```kotlin
enum class TimeOfDay {
    NIGHT(0-4), EARLY_MORNING(5-7), MORNING(8-11),
    NOON(12-13), AFTERNOON(14-17), EVENING(18-20), NIGHT(21-23)
}
```

**Holidays:**
- New Year's Day, Valentine's Day, St. Patrick's Day
- Independence Day, Halloween, Christmas, New Year's Eve

### 6. Business Life Path (150 Features)

**Company Management:**
- Company types: Sole Proprietorship, Partnership, LLC, Corporation, Public
- Industries: Tech, Retail, Manufacturing, Finance, Healthcare, Entertainment, Real Estate, Energy, Food, Transportation
- Company sizes: Solo (1), Small (10), Medium (50), Large (200), Enterprise (∞)

**Core Operations:**
```kotlin
// Product Development
fun developProduct(name: String, category: ProductCategory, budget: Double): ProductResult

// Employee Management
fun hireEmployee(employee: Employee): HireResult
fun fireEmployee(employeeId: String): FireResult
fun promoteEmployee(employeeId: String): PromoteResult

// Finance
fun seekInvestment(investorType: InvestorType, equityOffered: Float): InvestmentResult
fun applyForLoan(amount: Double, term: Int): LoanResult
fun quarterlyReport(): FinancialReport

// Expansion
fun openLocation(location: BusinessLocation): LocationResult
fun acquireCompany(target: Competitor, offerAmount: Double): AcquisitionResult

// Marketing
fun marketingCampaign(type: MarketingType, budget: Double): MarketingResult
fun priceWar(competitorId: String, priceCut: Float): PriceWarResult

// Exit
fun sellCompany(buyer: String, offerAmount: Double): SaleResult
fun ipo(): IPOResult
```

### 7. Database Schema

**Room Database (Version 1):**

**Entities (9 tables):**
1. `Character` - Player character with all stats
2. `Location` - World locations
3. `Faction` - Factions and organizations
4. `Npc` - Non-player characters
5. `Relationship` - Character-NPC relationships
6. `GameState` - Global game state (singleton)
7. `GameLog` - Event history
8. `InventoryItem` - Player items
9. `Asset` - Player properties/vehicles

**DAOs:**
- `CharacterDao`, `LocationDao`, `FactionDao`, `NpcDao`, `RelationshipDao`
- `GameStateDao`, `GameLogDao`, `InventoryDao`, `AssetDao`

---

## UI Architecture

### Screens

| Screen | Purpose |
|--------|---------|
| `SplashScreen` | App loading animation |
| `MainMenuScreen` | New game, continue, settings |
| `CharacterCreationScreen` | Character customization |
| `GameScreen` | Main gameplay (5 tabs) |
| `RelationshipsScreen` | NPC relationships |
| `InventoryScreen` | Items and assets |
| `LocationsScreen` | Travel destinations |
| `FactionsScreen` | Faction standings |

### GameScreen Tabs

1. **Status** - Character stats, wealth, location
2. **Actions** - Quick actions, travel options
3. **Relationships** - NPC list
4. **Assets** - Inventory preview
5. **More** - Character sheet, locations, factions, save

### Theme

**Material 3 with custom colors:**
- Primary, Secondary, Tertiary color schemes
- Light and Dark theme support
- Custom typography scale

---

## Development Conventions

### Code Style
- **Language**: Kotlin idiomatic
- **Architecture**: MVVM pattern
- **DI**: Hilt annotations (`@HiltAndroidApp`, `@ViewModelInject`)
- **Async**: Coroutines with Flow
- **Documentation**: KDoc for public APIs

### File Organization
```
data/     → Models, database entities
core/     → Business logic, managers
di/       → Dependency injection modules
ui/       → Compose UI, ViewModels
```

### Testing
- **Location**: `app/src/test/` for unit tests
- **Framework**: JUnit 4
- **Existing Tests**:
  - `TimeManagerTest.kt` - Time progression
  - `CharacterStatsTest.kt` - Stat calculations

### Git Workflow
- **Main branch**: `master`
- **Commits**: Conventional commits recommended
- **CI**: Auto-builds on push to master

---

## Current Development Status

### Completed (Phases 0-6)
- ✅ Core architecture (MVVM, Hilt, Room)
- ✅ Character stats, skills, traits systems
- ✅ World state (locations, factions, economy)
- ✅ Relationship system with NPC generator
- ✅ Time management and calendar
- ✅ Event system foundation (3 events implemented)
- ✅ UI screens with Compose
- ✅ Auto-save, background processing
- ✅ CI/CD pipeline

### In Progress / TODO
- ⏳ Implement remaining 70+ events
- ⏳ Health system (injuries, illnesses, mental health)
- ⏳ Royalty life path (89 features)
- ⏳ Politics life path (89 features)
- ⏳ Crime/Prison life path (99 features)
- ⏳ Business life path integration (150 features)
- ⏳ Career life path (250 features, 10 career paths)
- ⏳ System integration and polish
- ⏳ Additional testing

---

## Common Development Tasks

### Add New Skill
Edit `data/models/character/Skills.kt`:
```kotlin
enum class SkillType {
    NEW_SKILL("New Skill", SkillCategory.INTELLECTUAL)
}
```

### Add New Event
Edit `core/events/RandomEventGenerator.kt`:
```kotlin
val NEW_EVENT = GameEvent(
    id = "new_event",
    title = "Event Title",
    narrativeText = "Story text...",
    choices = listOf(/* ... */),
    baseChance = 0.1
)
```

### Add New Trait
Edit `data/models/character/Traits.kt`:
```kotlin
val NEW_TRAIT = Trait(
    id = "new_trait",
    displayName = "New Trait",
    category = TraitCategory.POSITIVE,
    effects = listOf(TraitEffect.StatModifier("charisma", 10))
)
```

### Add New Location
Edit `data/models/world/Location.kt`:
```kotlin
enum class LocationType {
    NEW_LOCATION("New Location")
}
```

### Database Migration
Edit `data/database/GameDatabase.kt`:
```kotlin
@Database(entities = [...], version = 2)
abstract class GameDatabase : RoomDatabase() {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE characters ADD COLUMN new_column TEXT")
            }
        }
    }
}
```

---

## Key Files Reference

| File | Purpose |
|------|---------|
| `app/build.gradle.kts` | Dependencies, SDK config |
| `data/models/character/Character.kt` | Player entity |
| `data/models/character/Stats.kt` | Primary/Secondary stats |
| `data/models/character/Skills.kt` | Skill system |
| `data/models/character/Traits.kt` | Trait definitions |
| `core/time/TimeManager.kt` | Time progression |
| `core/events/RandomEventGenerator.kt` | Event system |
| `core/lifepaths/BusinessManager.kt` | Business mechanics |
| `data/database/GameDatabase.kt` | Room database |
| `ui/screens/game/GameScreen.kt` | Main gameplay UI |
| `ui/MainActivity.kt` | Entry point |
| `.github/workflows/build-debug.yml` | CI/CD pipeline |

---

## Performance Tips

1. **Use Flow** for reactive UI updates
2. **Lazy load** large lists in Compose
3. **Cache** frequently accessed data in ViewModels
4. **Use Room @Query** for filtered data
5. **Background threads** for heavy operations (WorkManager)
6. **Avoid state hoisting** too deep in component trees

---

## Debug Tools

### Enable Logging
Add to `BuildConfig`:
```kotlin
buildConfigField "Boolean", "ENABLE_LOGGING", "true"
```

### Debug Menu Ideas
- Add/remove stats
- Unlock all locations
- Trigger specific events
- Modify relationships
- Set time/date

---

## External Resources

- [Android Developers](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt](https://dagger.dev/hilt/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Material 3](https://m3.material.io/)
