package com.bear.englishlearning.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

enum class Difficulty(val label: String, val labelZh: String, val sentenceCount: Int) {
    BEGINNER("Beginner", "初級", 5),
    INTERMEDIATE("Intermediate", "中級", 10),
    ADVANCED("Advanced", "進階", 20);

    companion object {
        fun fromName(name: String): Difficulty =
            entries.find { it.name == name } ?: BEGINNER
    }
}

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DIFFICULTY = stringPreferencesKey("difficulty")
        val DAILY_TASK_COUNT = intPreferencesKey("daily_task_count")
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[Keys.ONBOARDING_COMPLETED] ?: false }

    val difficulty: Flow<Difficulty> = context.dataStore.data
        .map { preferences ->
            val name = preferences[Keys.DIFFICULTY] ?: Difficulty.BEGINNER.name
            Difficulty.fromName(name)
        }

    val dailyTaskCount: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[Keys.DAILY_TASK_COUNT] ?: 5 }

    suspend fun setDailyTaskCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DAILY_TASK_COUNT] = count.coerceIn(1, 20)
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DIFFICULTY] = difficulty.name
        }
    }
}
