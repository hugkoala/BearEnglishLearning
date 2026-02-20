# Changelog

All notable changes to BearEnglishLearning are documented here.

## [Unreleased]

## [1.4.0] — 2026-02-20

### Added
- **即時對話 (Real-Time Conversation)**: Voice-based English conversation practice
  - Speak into microphone → phone replies with TTS → chat-style UI
  - 8 conversation topics: cafe, hotel, shopping, restaurant, directions, doctor, airport, daily
  - `ConversationEngine`: keyword-matching reply system with Chinese translations
  - Real-time speech recognition with partial results display
  - Chat bubble UI with pulse animation on mic button
  - Tap any message to hear it spoken aloud
  - Translation toggle (show/hide Chinese)
  - Accessible via "🗣️ 即時對話" chip in Conversation screen

## [1.3.0] — 2026-02-20

### Added
- **每日任務 Quick Access**: Vocabulary, listening, and conversation preview cards on Daily Task screen
  - Vocabulary preview: 3 daily words with TTS playback
  - Listening quick card: one-tap navigation to listening quiz
  - Conversation preview: today's topic with 2 sample dialogue lines

## [1.2.0] — 2026-02-19

### Changed
- **聽力測驗**: Replaced embedded YouTube WebView player with **thumbnail + link**
  - Shows video thumbnail (via Coil) with ▶️ overlay
  - Tap to open video in YouTube app or browser
  - Displays video title and channel name
  - Eliminates all embedding restriction errors (150/152)
- Replaced `android-youtube-player` dependency with `Coil 2.6.0` for image loading

### Removed
- `YouTubePlayerComposable.kt` (embedded IFrame WebView player)
- Video blacklisting and auto-skip error handling (no longer needed)

## [1.1.0] — 2026-02-19

### Added
- **Auto-Generated Daily Scenarios**: `DailyScenarioGenerator` with 20 scenario templates × 10 sentences each
  - Categories: bakery, pet store, laundromat, car repair, dentist, farmer's market, bookstore, moving, dry cleaner, eye doctor, playground, electronics store, food delivery, swimming pool, furniture store, music store, immigration, museum, amusement park, yoga class
  - Date-based seed for daily variety (same scenario all day)
- **Daily Task mode toggle**: FilterChip selector (📖 預設場景 / 🎲 隨機生成)
- `GeneratedScenario` / `GeneratedSentence` domain models
- `DailyTaskMode` enum (PRESET / GENERATED) in ViewModel
- `GeneratedSuccess` UI state for generated scenarios

## [1.0.0] — 2026-02-19

### Added
- **每日任務 (Daily Task)**: 30 scenarios × 10 sentences with TTS playback (slow/normal)
- **模擬對話 (Conversation Simulation)**: 30 preset conversations + random generator (10,000+ combinations)
- **聽力測驗 (Listening Quiz)**: YouTube video search with speech recognition and Word-Level Diff comparison
- **每日單字表 (Daily Vocabulary)**: 200+ words across 14 categories, auto-generated daily (10 words/day)
- **備忘錄 (Memo)**: Note-taking with daily 9 AM push notification review reminders
- **設定 (Settings)**: Daily task count slider (1-10), difficulty settings
- **Onboarding**: 5-page setup wizard (TTS, speech, mic, notification permissions)
- Bear icon (🐻) throughout the app
- APK auto-upload to Google Drive via `upload_to_drive.py`

### Technical
- MVVM + Repository + Clean Architecture
- Kotlin 2.0.0 + Jetpack Compose Material3
- Room database v5 with 8 tables
- Hilt dependency injection
- YouTube Data API v3 with 7-day cache
- LCS-based word-level speech diff engine
- WorkManager for daily review notifications
- DataStore for user preferences
