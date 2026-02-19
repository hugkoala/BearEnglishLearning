# CLAUDE.md — BearEnglishLearning

This file provides guidance for AI assistants working on this project.

## Project Overview

**BearEnglishLearning** is a native Android app for personal English learning. It includes daily scenario-based sentence practice, conversation simulation, YouTube listening quizzes, daily vocabulary sheets, a memo/notebook system, and speech recognition comparison.

## Tech Stack

- **Language**: Kotlin 2.0.0, JDK 17
- **UI**: Jetpack Compose with Material3 (BOM 2024.06.00)
- **DI**: Hilt 2.51.1
- **DB**: Room 2.6.1 (version 5, `fallbackToDestructiveMigration()`)
- **Networking**: Retrofit 2.11.0 + OkHttp 4.12.0 + Moshi 1.15.1
- **Navigation**: Navigation Compose 2.7.7
- **Preferences**: DataStore 1.1.1
- **Background**: WorkManager 2.9.1
- **Image Loading**: Coil 2.6.0
- **Build**: AGP 8.4.2, Gradle 8.7
- **Min SDK**: 26 (Android 8.0), **Target/Compile SDK**: 35

## Architecture

```
MVVM + Repository Pattern + Clean Architecture
```

### Layer Structure

```
app/src/main/java/com/bear/englishlearning/
├── data/           # Data layer: Room entities, DAOs, repositories, API, preferences
├── di/             # Hilt dependency injection modules
├── domain/         # Business logic: generators, models, speech engine
├── ui/             # Presentation: screens, ViewModels, components, navigation, theme
└── worker/         # WorkManager background tasks
```

## Key Conventions

### Code Style
- All ViewModels use `@HiltViewModel` with `@Inject constructor`
- UI state follows sealed interface pattern: `Loading`, `Success(data)`, `Error(message)`
- State exposed via `StateFlow` + `collectAsStateWithLifecycle()`
- Generators use `@Singleton` with `@Inject constructor()`
- Date-based seed pattern: `LocalDate.now().toEpochDay()` for daily-consistent random generation

### Navigation
- Bottom navigation with **5 tabs**: 每日任務, 模擬對話, 聽力測驗, 單字表, 備忘錄
- Defined in `AppNavigation.kt` using `Screen` sealed class
- Sub-pages: MemoCreate, Review, Settings (no bottom bar)

### Database
- Room DB name: `bear_english.db`, version 5
- 8 tables: scenarios, sentences, daily_tasks, practice_history, memos, cached_videos, conversations, conversation_lines
- Seed data loaded via `SeedDatabaseCallback` on first install
- Uses `fallbackToDestructiveMigration()` — bump version freely, data resets

### Auto-Generation Pattern
- `DailyVocabularyGenerator`: 200+ words, date-seeded, returns 10/day
- `RandomConversationGenerator`: 10 template scenarios × 8 slots × 3-4 variations = 10,000+ combos
- `DailyScenarioGenerator`: 20 scenario templates × 10 sentences, date-seeded daily rotation
- `GeneratedScenario` / `GeneratedSentence`: Domain models for generated content (not Room entities)

### YouTube Integration
- API Key in `BuildConfig.YOUTUBE_API_KEY`
- Videos cached 7 days in Room (`cached_videos` table)
- Displays thumbnail (Coil) + title + channel; tap opens YouTube app/browser
- No embedded WebView player — avoids all embedding restriction issues

## Build & Deploy

```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Upload to Google Drive
python3 upload_to_drive.py

# APK output
app/build/outputs/apk/debug/BearEnglishLearning-debug-v1.0-{timestamp}.apk
```

### Git
- Remote: `git@github.com:hugkoala/BearEnglishLearning.git`
- Branch: `main`
- Commit style: `feat:`, `fix:`, `docs:`, `refactor:`

## Common Tasks

### Adding a new screen
1. Create `ui/screens/{feature}/{Feature}Screen.kt` + `{Feature}ViewModel.kt`
2. Add `Screen.{Feature}` to sealed class in `AppNavigation.kt`
3. Add `BottomNavItem` if it's a main tab (update `showBottomBar` list too)
4. Add `composable(Screen.{Feature}.route)` to `NavHost`

### Adding a new generator
1. Create domain model in `domain/{feature}/`
2. Create `@Singleton class {Feature}Generator @Inject constructor()`
3. Use `LocalDate.now().toEpochDay()` as random seed for daily consistency
4. Inject into ViewModel

### Adding a new Room entity
1. Create entity in `data/local/entity/`
2. Create DAO in `data/local/dao/`
3. Register in `AppDatabase.kt` entities list
4. Provide DAO in `DatabaseModule.kt`
5. **Bump DB version** (currently 5)
6. Create repository in `data/repository/`

## File Reference

| File | Purpose |
|------|---------|
| `BearApp.kt` | Application class (Hilt, notification channel, WorkManager) |
| `AppNavigation.kt` | Navigation graph, bottom bar, route definitions |
| `SeedData.kt` | 30 scenarios × 10 sentences + 30 conversations × 8 lines |
| `DailyVocabularyGenerator.kt` | 200+ English words, 14 categories, daily generation |
| `RandomConversationGenerator.kt` | 10 scenario templates, random dialogue assembly |
| `WordDiffEngine.kt` | LCS word-level diff for speech comparison |
| `TextNormalizer.kt` | English text normalization (50+ contractions) |
| `DailyScenarioGenerator.kt` | 20 scenario templates × 10 sentences, daily rotation |
| `DailyReviewReminderWorker.kt` | 9 AM push notification for memo review |
| `upload_to_drive.py` | Auto-upload APK to Google Drive |
