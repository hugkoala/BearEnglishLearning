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
        scope.launch {
            val scenarioDao = database.get().scenarioDao()
            scenarioDao.insertAllScenarios(SeedData.getScenarios())
            scenarioDao.insertAllSentences(SeedData.getSentences())
        }
    }
}
