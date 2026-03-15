# Ultimate Life Simulator - Project Context

## Project Overview

**Ultimate Life Simulator** is a comprehensive text-based life simulation Android game built with Kotlin and Jetpack Compose. Players experience multiple life paths including royalty, politics, crime, business, and career gameplay.

### Key Statistics
- **700+ features** across 5 major life paths
- **50+ skills**, **30+ traits**, **75+ events**
- **50+ locations**, **16+ factions**, **40+ relationship types**
- Built with **MVVM architecture**, **Hilt DI**, **Room database**

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin 1.9.22 |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| DI | Hilt 2.50 |
| Database | Room 2.6.1 |
| Async | Coroutines, Flow |
| Navigation | Navigation Compose 2.7.7 |
| Background | WorkManager 2.9.0 |
| Build | Gradle 8.2.2, JDK 17 |

## Project Structure

```
popop/
├── app/
│   ├── src/main/
│   │   ├── java/com/popop/lifesimulator/
│   │   │   ├── data/           # Data layer
│   │   │   │   ├── models/     # Character, World, Relationship models
│   │   │   │   └── database/   # Room DAOs and entities
│   │   │   ├── core/           # Business logic
│   │   │   │   ├── time/       # TimeManager, calendar system
│   │   │   │   ├── events/     # Random event generation
│   │   │   │   ├── lifepaths/  # Royalty, Politics, Crime, Business, Career managers
│   │   │   │   └── utilities/  # Logger, notifications
│   │   │   ├── di/             # Hilt dependency injection modules
│   │   │   └── ui/             # UI layer
│   │   │       ├── theme/      # Colors, Typography, Theme
│   │   │       ├── navigation/ # Navigation routes
│   │   │       ├── viewmodels/ # ViewModels
│   │   │       ├── screens/    # Compose screens
│   │   │       └── MainActivity.kt
│   │   ├── res/                # Android resources
│   │   └── AndroidManifest.xml
│   ├── src/test/               # Unit tests
│   └── build.gradle.kts
├── .github/workflows/          # GitHub Actions CI/CD
├── build.gradle.kts            # Root build config
├── settings.gradle.kts         # Project settings
├── gradle.properties           # Gradle properties
└── README.md, DEVELOPMENT.md   # Documentation
```

## Building and Running

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean build
```

### Debug APK Output
- Location: `app/build/outputs/apk/debug/app-debug.apk`
- CI/CD: GitHub Actions automatically builds and uploads as artifact on push

## CI/CD

GitHub Actions workflow (`.github/workflows/build-debug.yml`):
- Triggers: push/PR to master/main, or manual dispatch
- Builds debug APK on Ubuntu runner with JDK 17
- Uploads APK as `debug-apk` artifact

## Key Systems

### Character System
- **10 Primary Stats**: Health, Energy, Stress, Charisma, Intellect, Cunning, Violence, Stealth, Perception, Willpower
- **10 Secondary Stats**: Reputation, Wealth, Piety, Loyalty, Addiction, Heat, Education, Street Cred, Noble Standing, Political Capital
- **50+ Skills**: 9 categories with XP progression
- **30+ Traits**: Positive, negative, neutral with effects

### World System
- **TimeManager**: Real-time progression, seasons, holidays
- **50+ Locations**: With properties and access rules
- **16+ Factions**: 5 categories (criminal, political, corporate, etc.)
- **Economy**: Market fluctuations, supply/demand

### Life Paths
1. **Royalty** (89 features): Court, realm, diplomacy, military, succession
2. **Politics** (89 features): Campaigns, elections, legislation, media
3. **Crime** (99 features): Criminal enterprise, heists, prison mechanics
4. **Business** (150 features): Company management, investments, IPO
5. **Career** (250 features): 10 career paths with progression

### Database
Room database with 9 DAOs:
- CharacterDao, LocationDao, FactionDao, NpcDao, RelationshipDao
- GameStateDao, GameLogDao, InventoryDao, AssetDao

## Development Conventions

### Code Style
- Kotlin idiomatic style
- MVVM architecture
- Hilt for dependency injection
- Coroutines + Flow for async operations
- KDoc for public APIs

### File Organization
- **Data models** → `data/models/`
- **Database** → `data/database/`
- **Business logic** → `core/`
- **UI screens** → `ui/screens/`
- **ViewModels** → `ui/viewmodels/`

### Testing
- Unit tests in `app/src/test/`
- JUnit for testing
- Existing tests: `TimeManagerTest.kt`, `CharacterStatsTest.kt`

## Current Status

**Completed**: Phases 0-6 (Foundation through UI/UX)
- Core architecture, database, DI
- Character stats, skills, traits
- World state, relationships, events
- UI screens with Compose
- Auto-save, background processing

**In Progress**: Life path implementations, event content

## GitHub Repository

- **URL**: https://github.com/lobbbin/jpddvb
- **Branch**: master
- **Visibility**: Public (unlimited free CI/CD minutes)

## Common Tasks

### Add New Skill
Edit `data/models/character/Skills.kt`

### Add New Event
Edit `core/events/RandomEventGenerator.kt`

### Add New Location
Edit `data/models/world/Location.kt`

### Database Migration
Increment `version` in `GameDatabase.kt` and add `Migration` object

## Important Files

| File | Purpose |
|------|---------|
| `app/build.gradle.kts` | App dependencies and config |
| `core/time/TimeManager.kt` | Game time progression |
| `data/database/GameDatabase.kt` | Room database definition |
| `ui/MainActivity.kt` | Entry point |
| `.github/workflows/build-debug.yml` | CI/CD pipeline |
