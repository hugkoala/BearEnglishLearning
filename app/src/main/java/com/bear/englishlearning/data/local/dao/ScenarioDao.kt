package com.bear.englishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence

@Dao
interface ScenarioDao {

    @Query("SELECT * FROM scenarios ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomScenario(): Scenario?

    @Query("""
        SELECT * FROM scenarios 
        WHERE scenarioId NOT IN (SELECT scenarioId FROM daily_tasks WHERE date = :today) 
        ORDER BY RANDOM() LIMIT 1
    """)
    suspend fun getRandomUnusedScenario(today: String): Scenario?

    @Query("SELECT * FROM scenarios WHERE scenarioId = :id")
    suspend fun getScenarioById(id: Long): Scenario?

    @Query("SELECT * FROM sentences WHERE scenarioId = :scenarioId ORDER BY orderIndex")
    suspend fun getSentencesForScenario(scenarioId: Long): List<Sentence>

    @Query("SELECT * FROM sentences WHERE scenarioId = :scenarioId ORDER BY orderIndex LIMIT :limit")
    suspend fun getSentencesForScenarioLimited(scenarioId: Long, limit: Int): List<Sentence>

    @Query("SELECT * FROM sentences WHERE sentenceId = :id")
    suspend fun getSentenceById(id: Long): Sentence?

    @Query("SELECT * FROM scenarios")
    suspend fun getAllScenarios(): List<Scenario>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllScenarios(scenarios: List<Scenario>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSentences(sentences: List<Sentence>)
}
