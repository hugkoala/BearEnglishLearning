package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.local.dao.ScenarioDao
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScenarioRepository @Inject constructor(
    private val scenarioDao: ScenarioDao
) {
    suspend fun getRandomScenario(): Scenario? = scenarioDao.getRandomScenario()

    suspend fun getRandomUnusedScenario(today: String): Scenario? =
        scenarioDao.getRandomUnusedScenario(today)

    suspend fun getScenarioById(id: Long): Scenario? = scenarioDao.getScenarioById(id)

    suspend fun getSentencesForScenario(scenarioId: Long): List<Sentence> =
        scenarioDao.getSentencesForScenario(scenarioId)

    suspend fun getAllScenarios(): List<Scenario> = scenarioDao.getAllScenarios()
}
