# BearEnglishLearning — Project Summary

> Last updated: 2026-02-22 | Version: **1.7.0** | Commits: 31 | Kotlin files: 80 | ~12,000 lines

## Overview

**BearEnglishLearning** is a native Android app for personal English learning built with Kotlin + Jetpack Compose + Material3. It includes daily scenario-based sentence practice, real-time AI conversation, YouTube listening quizzes, vocabulary sheets, a memo/notebook system, translation tools, and a learning progress calendar.

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin 2.0.0, JDK 17 |
| UI | Jetpack Compose + Material3 (BOM 2024.06.00) |
| DI | Hilt 2.51.1 |
| Database | Room 2.6.1 (v7, 9 tables) |
| Network | Retrofit 2.11.0 + OkHttp 4.12.0 + Moshi 1.15.1 |
| Navigation | Navigation Compose 2.7.7 |
| Build | AGP 8.4.2, Gradle 8.7 |
| SDK | Min 26 / Target 35 |
| Tests | 85 unit tests (JUnit + coroutines-test) |

## Architecture

```
MVVM + Repository Pattern + Clean Architecture

app/src/main/java/com/bear/englishlearning/
├── data/           # Room entities (9), DAOs, repositories, API, preferences
├── di/             # Hilt modules (AppModule, DatabaseModule, NetworkModule)
├── domain/         # Generators, ConversationEngine, WordDiffEngine, TextNormalizer
├── ui/             # 8 screens, ViewModels, components, navigation, theme
└── worker/         # DailyReviewReminderWorker (9 AM push)
```

## Features & Screens

### 1. 每日任務 (Daily Task) — Main Tab
- Preset scenarios (30 scenarios × 10 sentences from seed data)
- Auto-generated scenarios (20 templates × 10 sentences, date-seeded)
- TTS playback for each sentence
- Task completion tracking
- Quick-access cards for vocabulary, listening, and conversation

### 2. 模擬對話 (Conversation) — Tab
- Preset conversations (30 × 8 lines from seed data)
- Random generated conversations (10 templates × 8 slots × 3-4 variations)
- Line-by-line reveal with Chinese translation toggle
- TTS for each line

### 3. 即時對話 (Real-Time Conversation)
- Speech recognition (Google Speech API) with silence detection tuning
- AI reply generation via ConversationEngine (10 topic scenarios)
- TTS auto-playback of replies
- Chat bubble UI with Chinese translation toggle
- Topic selection and random topic
- Crash-hardened: AtomicLong IDs, availability checks, permission handling

### 4. 聽力測驗 (Listening Quiz) — Tab
- YouTube video search + cached thumbnails (7-day cache)
- Sentence dictation with speech recognition
- Word-level diff comparison (WordDiffEngine + TextNormalizer)
- Accuracy scoring + practice history saved to Room

### 5. 單字表 (Vocabulary) — Tab
- Daily vocabulary: 200+ words across 14 categories, 10/day (date-seeded)
- Custom word board: add/delete/TTS for user-created words
- Bookmark daily words to "My Words" board
- Expandable cards with phonetic, meaning, example sentence
- TTS pronunciation

### 6. 翻譯 (Translation)
- 15 languages supported (MyMemory API)
- Alternative meanings + example sentences
- Translation history (last 20)
- Source/target language swap
- TTS pronunciation of results
- Copy to clipboard

### 7. 備忘錄 (Memo) — Tab
- Create/delete memos with optional scenario context
- Review reminder system (next-day 9 AM push notification via WorkManager)
- Mark as reviewed functionality

### 8. 學習日曆 (Calendar) — NEW in v1.7.0
- Monthly calendar view with activity heatmap (4 intensity levels)
- Tracks 7 activity types: sentences, vocabulary, conversation turns, listening quizzes, memos, translations, study minutes
- Cumulative study days + streak counter (🔥)
- Day detail card with per-activity breakdown
- Accessible via calendar icon on Daily Task screen

## Version History

| Version | Commit | Description |
|---------|--------|-------------|
| **1.7.0** | `f4d1279` | Calendar for daily learning progress tracking |
| 1.6.0 | `385b748` | Translation: alternative meanings + example sentences |
| 1.6.0 | `a040514` | Translation sheet with 15 languages, TTS, copy, history |
| 1.5.0 | `4d2e356` | Bookmark daily words to My Words board |
| 1.5.0 | `040bf1c` | Custom word board with add/delete/TTS |
| 1.4.1 | `cef20ee` | Version config via version.properties |
| 1.4.1 | `af9944d` | Fix bottom nav cross-tab switching |
| 1.4.0 | `0e9abea` | Real-time conversation with speech recognition |
| 1.3.0 | `4749abc` | Quick-access cards for vocabulary/listening/conversation |
| 1.2.0 | `dc496f1` | TTS for vocabulary screen |
| 1.1.0 | `fe8fa4d` | YouTube thumbnail + link (replace WebView) |
| 1.1.0 | `e88872e` | Auto-generated daily scenarios (20 templates) |
| 1.0.x | various | YouTube fixes, vocabulary sheet, random conversations, seed data expansion, settings, initial release |

## Bug Fixes

- **Speech recognition cut-off** (`d3ccd55`): Added `EXTRA_SPEECH_INPUT_*` silence/minimum duration parameters
- **Real-time conversation crashes** (`ed1d007`): AtomicLong message IDs, SpeechRecognizer availability check, runtime permission request, single UtteranceProgressListener, main thread handler, TTS stop before shutdown
- **Bottom nav switching** (`af9944d`): Fix cross-tab navigation not returning to correct tab
- **YouTube embed errors** (`8afb198`, `fd65a68`): Handle error codes 150/152/4, replaced WebView with thumbnail+link

## Database Schema (Room v7)

9 tables: `scenarios`, `sentences`, `daily_tasks`, `practice_history`, `memos`, `cached_videos`, `conversations`, `conversation_lines`, `daily_progress`

Uses `fallbackToDestructiveMigration()` — bump version freely, data resets on schema change.

## Auto-Generation System

| Generator | Output |
|-----------|--------|
| `DailyScenarioGenerator` | 20 templates × 10 sentences, date-seeded daily rotation |
| `DailyVocabularyGenerator` | 200+ words, 14 categories, 10/day date-seeded |
| `RandomConversationGenerator` | 10 templates × 8 slots × 3-4 variations = 10,000+ combos |
| `ConversationEngine` | 10 topic scenarios with reply generation for real-time chat |

## Build & Deploy

```bash
./gradlew assembleDebug    # Build debug APK
./gradlew test             # Run 85 unit tests
python3 upload_to_drive.py # Upload APK to Google Drive
```

**Repository:** `git@github.com:hugkoala/BearEnglishLearning.git` (branch: `main`)
