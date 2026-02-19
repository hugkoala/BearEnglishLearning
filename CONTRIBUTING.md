# Contributing to BearEnglishLearning

## Development Setup

### Prerequisites
- JDK 17
- Android SDK (API 35)
- Android Build Tools 35.0.0

### Getting Started

```bash
git clone git@github.com:hugkoala/BearEnglishLearning.git
cd BearEnglishLearning

# Set Android SDK path
echo "sdk.dir=/path/to/your/Android/Sdk" > local.properties

# Build
./gradlew assembleDebug

# Run tests
./gradlew test
```

## Project Structure

```
app/src/main/java/com/bear/englishlearning/
├── data/          → Room DB, API, repositories, preferences
├── di/            → Hilt modules (Database, Network, ApplicationScope)
├── domain/        → Generators, models, speech engine
├── ui/            → Compose screens, ViewModels, components, navigation
└── worker/        → WorkManager daily reminder
```

## Code Conventions

### Architecture
- **MVVM + Repository + Clean Architecture**
- ViewModels: `@HiltViewModel`, expose `StateFlow<UiState>`
- UI State: sealed interface with `Loading`, `Success`, `Error`
- Repositories: abstract data source access for ViewModels

### Kotlin / Compose
- Compose Material3 throughout
- `collectAsStateWithLifecycle()` for state observation
- `hiltViewModel()` for ViewModel injection in composables
- `@OptIn(ExperimentalMaterial3Api::class)` where needed

### Naming
- Screens: `{Feature}Screen.kt`
- ViewModels: `{Feature}ViewModel.kt`
- Entities: singular noun (e.g., `Scenario`, `Sentence`)
- DAOs: `{Entity}Dao.kt`
- Repositories: `{Feature}Repository.kt`
- Generators: `{Feature}Generator.kt`

### Commit Messages
Use conventional commits:
- `feat:` — new feature
- `fix:` — bug fix
- `docs:` — documentation
- `refactor:` — code restructure
- `test:` — tests
- `chore:` — build/config changes

## Testing

```bash
# Unit tests
./gradlew test

# Tests are in:
# app/src/test/java/com/bear/englishlearning/domain/speech/
#   - TextNormalizerTest.kt
#   - WordDiffEngineTest.kt
```

## Deployment

```bash
# Build APK
./gradlew assembleDebug

# Upload to Google Drive
python3 upload_to_drive.py

# APK format: BearEnglishLearning-debug-v1.0-{yyyyMMdd_HHmmss}.apk
```
