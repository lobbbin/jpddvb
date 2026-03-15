# Ultimate Life Simulator - Complete Android Game

A comprehensive text-based life simulation game for Android featuring royalty, politics, crime, business, and career gameplay paths.

## 🎮 Project Overview

**Ultimate Life Simulator** is a feature-rich, text-based life simulation game built with modern Android technologies. Players can experience multiple life paths from ruling as royalty to building criminal empires, climbing political ladders, becoming business tycoons, or pursuing professional careers.

---

## 📁 Complete Project Structure

```
app/src/main/java/com/popop/lifesimulator/
├── data/
│   ├── models/
│   │   ├── character/
│   │   │   ├── Character.kt          # Main character entity
│   │   │   ├── Stats.kt              # Primary & Secondary stats
│   │   │   ├── Skills.kt             # 50+ skills with XP system
│   │   │   ├── Traits.kt             # 30+ traits with effects
│   │   │   └── Health.kt             # Health, injuries, illnesses, mental health
│   │   ├── world/
│   │   │   ├── Location.kt           # 50+ location types
│   │   │   ├── Faction.kt            # 16+ factions
│   │   │   ├── GameState.kt          # Global game state
│   │   │   └── GameLog.kt            # Event logging
│   │   ├── relationship/
│   │   │   ├── Relationship.kt       # 40+ relationship types
│   │   │   └── Npc.kt                # NPC generation
│   │   └── inventory/
│   │       └── Item.kt               # Items & assets
│   └── database/
│       ├── GameDatabase.kt           # Room database
│       ├── Converters.kt             # Type converters
│       └── *Dao.kt                   # 9 Data Access Objects
├── core/
│   ├── time/
│   │   └── TimeManager.kt            # Time & calendar system
│   ├── events/
│   │   ├── RandomEventGenerator.kt   # Event generation
│   │   └── EventRegistry.kt          # 75+ predefined events
│   ├── lifepaths/
│   │   ├── RoyaltyManager.kt         # Royalty mechanics (89 features)
│   │   ├── PoliticsManager.kt        # Politics mechanics (89 features)
│   │   ├── CrimeManager.kt           # Crime/Prison mechanics (99 features)
│   │   ├── BusinessManager.kt        # Business mechanics (150 features)
│   │   └── CareerManager.kt          # Career mechanics (250 features)
│   ├── utilities/
│   │   └── Logger.kt                 # Logging & flag management
│   ├── notifications/
│   │   └── GameNotificationManager.kt # Push notifications
│   └── background/
│       └── GameEventWorker.kt        # Background processing
├── di/
│   └── AppModule.kt                  # Hilt dependency injection
└── ui/
    ├── theme/
    │   ├── Color.kt                  # Color scheme
    │   ├── Type.kt                   # Typography
    │   └── Theme.kt                  # Material theme
    ├── navigation/
    │   └── Navigation.kt             # Navigation routes
    ├── viewmodels/
    │   └── GameViewModel.kt          # Main game ViewModel
    ├── screens/
    │   ├── splash/
    │   ├── mainmenu/
    │   ├── charactercreation/
    │   ├── game/
    │   ├── relationships/
    │   ├── inventory/
    │   ├── locations/
    │   └── factions/
    └── MainActivity.kt
```

---

## ✅ Completed Features

### Phase 0: Foundation ✅
- [x] Android project with Kotlin & Jetpack Compose
- [x] MVVM architecture with Hilt dependency injection
- [x] Room database for persistent storage
- [x] Navigation component for screen flows
- [x] Auto-save system (every 5 minutes)
- [x] Background processing with WorkManager

### Phase 1: Core Character Engine ✅
- [x] 10 Primary stats (Health, Energy, Stress, Charisma, Intellect, Cunning, Violence, Stealth, Perception, Willpower)
- [x] 10 Secondary stats (Reputation, Wealth, Piety, Loyalty, Addiction, Heat, Education, Street Cred, Noble Standing, Political Capital)
- [x] 50+ Skills across 9 categories with XP progression
- [x] 30+ Traits (positive, negative, neutral) with effects and conflicts
- [x] Comprehensive Health system (injuries, illnesses, mental health)

### Phase 2: World State Engine ✅
- [x] 50+ Location types with properties
- [x] 16+ Predefined factions across 5 categories
- [x] Economy system with market fluctuations
- [x] Law & Order tracking
- [x] Calendar with seasons, holidays, time progression

### Phase 3: Relationship System ✅
- [x] 40+ Relationship types (familial, romantic, professional, social, criminal)
- [x] Relationship metrics (Trust, Fear, Respect, Loyalty, Affection, Debt)
- [x] NPC generator with personality and background
- [x] Social dynamics (memory, opinion modifiers)

### Phase 4: Health System ✅
- [x] Physical injuries (12 types with severity levels)
- [x] Illnesses (35+ conditions from cold to cancer)
- [x] Mental health (15+ conditions including depression, anxiety, PTSD)
- [x] Treatment systems (medical, therapy, lifestyle)
- [x] Health consequences and cascades

### Phase 5: Event System ✅
- [x] 75+ Random events across all life paths
- [x] Event choices with skill checks
- [x] Event chains and storylines
- [x] Event outcomes with stat changes

### Phase 6-10: Life Path Systems ✅
- [x] **Royalty** (89 features): Court management, realm management, diplomacy, military, succession
- [x] **Politics** (89 features): Campaigns, elections, legislation, media, scandals
- [x] **Crime/Prison** (99 features): Criminal enterprise, heists, turf wars, prison mechanics, parole
- [x] **Business/Tycoon** (150 features): Company management, investments, expansion, IPO, acquisitions
- [x] **Career/Education** (250 features): 10 career paths with progression, education, networking

### Phase 11: UI/UX ✅
- [x] Splash screen with animations
- [x] Main menu
- [x] Character creation
- [x] Game screen with 5 tabs
- [x] Relationships screen
- [x] Inventory screen
- [x] Locations screen
- [x] Factions screen
- [x] Material 3 design with dark/light themes

### Phase 12: Android Features ✅
- [x] Push notifications for events
- [x] Background processing
- [x] Auto-save functionality
- [x] WorkManager integration

### Phase 13: Testing ✅
- [x] Unit tests for TimeManager
- [x] Unit tests for Character Stats
- [x] Unit tests for Skill System

---

## 🎯 Life Paths

### 👑 Royalty
Rule as a monarch, manage your court, conduct diplomacy, wage wars, and build a dynasty. Features include:
- Court management with courtiers and council
- Province and vassal management
- Foreign diplomacy and alliances
- Military command and warfare
- Royal marriages and succession

### 🏛️ Politics
Climb the political ladder from local office to president. Features include:
- Campaign management (fundraising, rallies, ads)
- Elections and polling
- Legislation and governance
- Media relations and scandals
- Party politics and factions

### 🔪 Crime
Build a criminal empire from the streets to the syndicate. Features include:
- Crew recruitment and management
- Heist planning and execution
- Turf wars and gang management
- Illegal operations (drugs, gambling, etc.)
- Prison mechanics and parole

### 💼 Business
Start a company and become a tycoon. Features include:
- Product development and launch
- Employee management
- Investment and funding
- Market competition
- IPO and exit strategies

### 📚 Career
Pursue a professional career in various fields. Features include:
- 10 career paths (Medical, Legal, Education, Corporate, Trades, Creative, Government, Military, Science, Service)
- Education and certifications
- Job progression and promotions
- Professional networking
- Career-specific mini-games

---

## 🛠️ Technical Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| DI | Hilt |
| Database | Room |
| Async | Coroutines, Flow |
| Navigation | Navigation Compose |
| Background | WorkManager |
| Testing | JUnit |

---

## 📦 Dependencies

```kotlin
// Core Android
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
androidx.activity:activity-compose:1.8.2

// Compose
androidx.compose.ui:ui
androidx.compose.material3:material3
androidx.navigation:navigation-compose:2.7.7

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Hilt
com.google.dagger:hilt-android:2.50
androidx.hilt:hilt-navigation-compose:1.1.0

// WorkManager
androidx.work:work-runtime-ktx:2.9.0
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Build & Run
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build → Make Project
5. Run on device/emulator

---

## 📊 Game Statistics

| Feature | Count |
|---------|-------|
| Skills | 50+ |
| Traits | 30+ |
| Events | 75+ |
| Locations | 50+ |
| Factions | 16+ |
| Relationship Types | 40+ |
| Illnesses | 35+ |
| Career Paths | 10 |
| Total Features | 700+ |

---

## 📝 Key Systems

### Time System
- Real-time progression with adjustable speed
- Day/night cycle, weekdays/weekends
- Seasons with effects on gameplay
- Holiday calendar with special events

### Event System
- Choice-based narrative events
- Skill checks with difficulty modifiers
- Stat requirements and consequences
- Event chains for storylines

### Save System
- Auto-save every 5 minutes
- Manual save option
- Room database persistence
- Cloud save ready (Google Drive)

### Notification System
- Event notifications
- Background event alerts
- Daily reminders
- Configurable channels

---

## 🧪 Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

---

## 📄 License

Proprietary - All rights reserved

---

## 👨‍💻 Development

See [DEVELOPMENT.md](DEVELOPMENT.md) for detailed development guide including:
- Adding new features
- Creating events
- Database migrations
- Code style guidelines
- Debug tools

---

## 🎮 How to Play

1. **Start a New Game**: Choose your life path (Royalty, Politics, Crime, Business, or Career)
2. **Create Your Character**: Set name, gender, age, and starting traits
3. **Manage Your Stats**: Balance health, energy, stress, and skills
4. **Make Choices**: Respond to random events and shape your destiny
5. **Build Relationships**: Form connections with NPCs for benefits
6. **Achieve Goals**: Rise to the top in your chosen path

---

## 🔮 Future Enhancements

- [ ] Multiplayer/Online features
- [ ] Additional life paths (Military, Science, Entertainment)
- [ ] More events and storylines
- [ ] Achievement system
- [ ] Statistics tracking
- [ ] Mod support
- [ ] Cloud save synchronization
- [ ] Tablet optimization
- [ ] Wear OS support

---

**Built with ❤️ using Kotlin and Jetpack Compose**
