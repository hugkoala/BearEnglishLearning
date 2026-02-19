# Changelog

All notable changes to BearEnglishLearning are documented here.

## [Unreleased]

### Added
- `GeneratedScenario` / `GeneratedSentence` domain models for auto-generated scenarios

## [1.0.0] — 2026-02-19

### Added
- **每日任務 (Daily Task)**: 30 scenarios × 10 sentences with TTS playback (slow/normal)
- **模擬對話 (Conversation Simulation)**: 30 preset conversations + random generator (10,000+ combinations)
- **聽力測驗 (Listening Quiz)**: YouTube video playback with speech recognition and Word-Level Diff comparison
- **每日單字表 (Daily Vocabulary)**: 200+ words across 14 categories, auto-generated daily (10 words/day)
- **備忘錄 (Memo)**: Note-taking with daily 9 AM push notification review reminders
- **設定 (Settings)**: Daily task count slider (1-10), difficulty settings
- **Onboarding**: 5-page setup wizard (TTS, speech, mic, notification permissions)
- Bear icon (🐻) throughout the app
- YouTube video error handling: fast skip for embedding errors (150/152), video blacklisting
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
