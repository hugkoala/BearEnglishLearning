package com.bear.englishlearning.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bear.englishlearning.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SeedDatabaseCallback @Inject constructor(
    private val database: Provider<AppDatabase>,
    @ApplicationScope private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        seedDatabase()
    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        seedDatabase()
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // Safety net: if tables are empty (e.g. after destructive migration), re-seed
        scope.launch {
            val scenarioDao = database.get().scenarioDao()
            val scenarios = scenarioDao.getAllScenarios()
            if (scenarios.isEmpty()) {
                scenarioDao.insertAllScenarios(SeedData.getScenarios())
                scenarioDao.insertAllSentences(SeedData.getSentences())
            }
            val conversationDao = database.get().conversationDao()
            val conversations = conversationDao.getAllConversations()
            if (conversations.isEmpty()) {
                conversationDao.insertAllConversations(SeedData.getConversations())
                conversationDao.insertAllLines(SeedData.getConversationLines())
            }
        }
    }

    private fun seedDatabase() {
        scope.launch {
            val scenarioDao = database.get().scenarioDao()
            scenarioDao.insertAllScenarios(SeedData.getScenarios())
            scenarioDao.insertAllSentences(SeedData.getSentences())
            val conversationDao = database.get().conversationDao()
            conversationDao.insertAllConversations(SeedData.getConversations())
            conversationDao.insertAllLines(SeedData.getConversationLines())
        }
    }
}
