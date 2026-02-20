package com.bear.englishlearning.ui.screens.dailytask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.DailyTask
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence
import com.bear.englishlearning.data.preferences.AppPreferences
import com.bear.englishlearning.data.repository.DailyTaskRepository
import com.bear.englishlearning.data.repository.ScenarioRepository
import com.bear.englishlearning.domain.conversation.GeneratedConversation
import com.bear.englishlearning.domain.conversation.RandomConversationGenerator
import com.bear.englishlearning.domain.scenario.DailyScenarioGenerator
import com.bear.englishlearning.domain.scenario.GeneratedScenario
import com.bear.englishlearning.domain.vocabulary.DailyVocabularyGenerator
import com.bear.englishlearning.domain.vocabulary.VocabularyWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

enum class DailyTaskMode {
    PRESET,    // From database seed data
    GENERATED  // Auto-generated daily scenarios
}

sealed interface DailyTaskUiState {
    data object Loading : DailyTaskUiState
    data class Success(
        val task: DailyTask,
        val scenario: Scenario,
        val sentences: List<Sentence>,
        val sentenceCount: Int = 5,
        val mode: DailyTaskMode = DailyTaskMode.PRESET
    ) : DailyTaskUiState
    data class GeneratedSuccess(
        val generatedScenario: GeneratedScenario,
        val sentenceCount: Int = 5,
        val mode: DailyTaskMode = DailyTaskMode.GENERATED
    ) : DailyTaskUiState
    data class Error(val message: String) : DailyTaskUiState
}

@HiltViewModel
class DailyTaskViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository,
    private val dailyTaskRepository: DailyTaskRepository,
    private val appPreferences: AppPreferences,
    private val dailyScenarioGenerator: DailyScenarioGenerator,
    private val dailyVocabularyGenerator: DailyVocabularyGenerator,
    private val randomConversationGenerator: RandomConversationGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow<DailyTaskUiState>(DailyTaskUiState.Loading)
    val uiState: StateFlow<DailyTaskUiState> = _uiState.asStateFlow()

    private val _mode = MutableStateFlow(DailyTaskMode.PRESET)
    val mode: StateFlow<DailyTaskMode> = _mode.asStateFlow()

    // Preview data for quick-access sections
    private val _vocabularyPreview = MutableStateFlow<List<VocabularyWord>>(emptyList())
    val vocabularyPreview: StateFlow<List<VocabularyWord>> = _vocabularyPreview.asStateFlow()

    private val _conversationPreview = MutableStateFlow<GeneratedConversation?>(null)
    val conversationPreview: StateFlow<GeneratedConversation?> = _conversationPreview.asStateFlow()

    init {
        loadTodayTask()
        observeTaskCountChanges()
        loadPreviewData()
    }

    private fun loadPreviewData() {
        // Load vocabulary preview (first 3 words of today's daily vocabulary)
        _vocabularyPreview.value = dailyVocabularyGenerator.generateForToday().take(3)
        // Load conversation preview
        _conversationPreview.value = randomConversationGenerator.generate()
    }

    fun switchMode(newMode: DailyTaskMode) {
        _mode.value = newMode
        when (newMode) {
            DailyTaskMode.PRESET -> loadTodayTask()
            DailyTaskMode.GENERATED -> loadGeneratedScenario()
        }
    }

    private fun observeTaskCountChanges() {
        viewModelScope.launch {
            appPreferences.dailyTaskCount.collectLatest { newCount ->
                val current = _uiState.value
                if (current is DailyTaskUiState.Success && current.sentenceCount != newCount) {
                    val sentences = scenarioRepository.getSentencesForScenarioLimited(
                        current.scenario.scenarioId, newCount
                    )
                    _uiState.value = current.copy(sentences = sentences, sentenceCount = newCount)
                } else if (current is DailyTaskUiState.GeneratedSuccess && current.sentenceCount != newCount) {
                    _uiState.value = current.copy(sentenceCount = newCount)
                }
            }
        }
    }

    private fun loadTodayTask() {
        viewModelScope.launch {
            _uiState.value = DailyTaskUiState.Loading
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
                    val taskCount = appPreferences.dailyTaskCount.first()
                    val scenario = scenarioRepository.getScenarioById(task.scenarioId)
                    val sentences = scenarioRepository.getSentencesForScenarioLimited(
                        task.scenarioId, taskCount
                    )

                    if (scenario != null) {
                        _uiState.value = DailyTaskUiState.Success(
                            task, scenario, sentences, taskCount, DailyTaskMode.PRESET
                        )
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

    private fun loadGeneratedScenario() {
        _uiState.value = DailyTaskUiState.Loading
        try {
            val generated = dailyScenarioGenerator.generateForToday()
            val taskCount = 10 // Generated scenarios always have 10 sentences
            _uiState.value = DailyTaskUiState.GeneratedSuccess(
                generatedScenario = generated,
                sentenceCount = taskCount,
                mode = DailyTaskMode.GENERATED
            )
        } catch (e: Exception) {
            _uiState.value = DailyTaskUiState.Error("生成場景失敗: ${e.message}")
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
