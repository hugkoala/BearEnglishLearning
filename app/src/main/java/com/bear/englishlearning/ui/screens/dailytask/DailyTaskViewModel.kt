package com.bear.englishlearning.ui.screens.dailytask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.DailyTask
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence
import com.bear.englishlearning.data.repository.DailyTaskRepository
import com.bear.englishlearning.data.repository.ScenarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface DailyTaskUiState {
    data object Loading : DailyTaskUiState
    data class Success(
        val task: DailyTask,
        val scenario: Scenario,
        val sentences: List<Sentence>
    ) : DailyTaskUiState
    data class Error(val message: String) : DailyTaskUiState
}

@HiltViewModel
class DailyTaskViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository,
    private val dailyTaskRepository: DailyTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DailyTaskUiState>(DailyTaskUiState.Loading)
    val uiState: StateFlow<DailyTaskUiState> = _uiState.asStateFlow()

    init {
        loadTodayTask()
    }

    private fun loadTodayTask() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now().toString()
                var task = dailyTaskRepository.getTaskByDate(today)

                if (task == null) {
                    val scenario = scenarioRepository.getRandomUnusedScenario(today)
                        ?: scenarioRepository.getRandomScenario()

                    if (scenario == null) {
                        _uiState.value = DailyTaskUiState.Error("沒有可用的場景")
                        return@launch
                    }

                    val newTask = DailyTask(date = today, scenarioId = scenario.scenarioId)
                    dailyTaskRepository.insertTask(newTask)
                    task = dailyTaskRepository.getTaskByDate(today)
                }

                if (task != null) {
                    val scenario = scenarioRepository.getScenarioById(task.scenarioId)
                    val sentences = scenarioRepository.getSentencesForScenario(task.scenarioId)

                    if (scenario != null) {
                        _uiState.value = DailyTaskUiState.Success(task, scenario, sentences)
                    } else {
                        _uiState.value = DailyTaskUiState.Error("找不到場景資料")
                    }
                } else {
                    _uiState.value = DailyTaskUiState.Error("無法建立今日任務")
                }
            } catch (e: Exception) {
                _uiState.value = DailyTaskUiState.Error("載入失敗: ${e.message}")
            }
        }
    }

    fun completeTask() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state is DailyTaskUiState.Success && !state.task.isCompleted) {
                dailyTaskRepository.completeTask(state.task.taskId)
                _uiState.value = state.copy(
                    task = state.task.copy(isCompleted = true, completedAt = System.currentTimeMillis())
                )
            }
        }
    }
}
