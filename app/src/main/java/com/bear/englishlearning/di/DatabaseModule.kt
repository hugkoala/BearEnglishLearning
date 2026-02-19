package com.bear.englishlearning.di

import android.content.Context
import androidx.room.Room
import com.bear.englishlearning.data.local.AppDatabase
import com.bear.englishlearning.data.local.SeedDatabaseCallback
import com.bear.englishlearning.data.local.dao.CachedVideoDao
import com.bear.englishlearning.data.local.dao.DailyTaskDao
import com.bear.englishlearning.data.local.dao.MemoDao
import com.bear.englishlearning.data.local.dao.PracticeHistoryDao
import com.bear.englishlearning.data.local.dao.ScenarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: SeedDatabaseCallback
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bear_english.db"
        )
            .addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideScenarioDao(db: AppDatabase): ScenarioDao = db.scenarioDao()

    @Provides
    fun provideDailyTaskDao(db: AppDatabase): DailyTaskDao = db.dailyTaskDao()

    @Provides
    fun providePracticeHistoryDao(db: AppDatabase): PracticeHistoryDao = db.practiceHistoryDao()

    @Provides
    fun provideMemoDao(db: AppDatabase): MemoDao = db.memoDao()

    @Provides
    fun provideCachedVideoDao(db: AppDatabase): CachedVideoDao = db.cachedVideoDao()
}
