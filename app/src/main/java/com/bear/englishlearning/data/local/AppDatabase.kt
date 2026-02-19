package com.bear.englishlearning.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bear.englishlearning.data.local.dao.CachedVideoDao
import com.bear.englishlearning.data.local.dao.ConversationDao
import com.bear.englishlearning.data.local.dao.DailyTaskDao
import com.bear.englishlearning.data.local.dao.MemoDao
import com.bear.englishlearning.data.local.dao.PracticeHistoryDao
import com.bear.englishlearning.data.local.dao.ScenarioDao
import com.bear.englishlearning.data.local.entity.CachedVideo
import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine
import com.bear.englishlearning.data.local.entity.DailyTask
import com.bear.englishlearning.data.local.entity.Memo
import com.bear.englishlearning.data.local.entity.PracticeHistory
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence

@Database(
    entities = [
        Scenario::class,
        Sentence::class,
        DailyTask::class,
        PracticeHistory::class,
        Memo::class,
        CachedVideo::class,
        Conversation::class,
        ConversationLine::class
    ],
    version = 5,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun practiceHistoryDao(): PracticeHistoryDao
    abstract fun memoDao(): MemoDao
    abstract fun cachedVideoDao(): CachedVideoDao
    abstract fun conversationDao(): ConversationDao
}
