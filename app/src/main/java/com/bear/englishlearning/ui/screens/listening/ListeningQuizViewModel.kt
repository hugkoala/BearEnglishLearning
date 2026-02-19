package com.bear.englishlearning.ui.screens.listening

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.PracticeHistory
import com.bear.englishlearning.data.local.entity.Scenario
import com.bear.englishlearning.data.local.entity.Sentence
import com.bear.englishlearning.data.repository.DailyTaskRepository
import com.bear.englishlearning.data.repository.PracticeRepository
import com.bear.englishlearning.data.repository.Resource
import com.bear.englishlearning.data.repository.ScenarioRepository
import com.bear.englishlearning.data.repository.YouTubeRepository
import com.bear.englishlearning.domain.model.SpeechDiffResult
import com.bear.englishlearning.domain.model.VideoResult
import com.bear.englishlearning.domain.speech.WordDiffEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ListeningQuizUiState(
    val scenario: Scenario? = null,
    val sentences: List<Sentence> = emptyList(),
    val videos: List<VideoResult> = emptyList(),
    val currentVideoIndex: Int = 0,
    val selectedSentenceIndex: Int = 0,
    val isLoadingVideos: Boolean = false,
    val videoError: String? = null,
    val videoPlayerError: Boolean = false,
    val isListening: Boolean = false,
    val recognizedText: String = "",
    val diffResult: SpeechDiffResult? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ListeningQuizViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository,
    private val dailyTaskRepository: DailyTaskRepository,
    private val youTubeRepository: YouTubeRepository,
    private val practiceRepository: PracticeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListeningQuizUiState())
    val uiState: StateFlow<ListeningQuizUiState> = _uiState.asStateFlow()

    init {
        loadScenario()
    }

    private fun loadScenario() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now().toString()
                val task = dailyTaskRepository.getTaskByDate(today)
                val scenario = if (task != null) {
                    scenarioRepository.getScenarioById(task.scenarioId)
                } else {
                    scenarioRepository.getRandomScenario()
                }

                if (scenario != null) {
                    val sentences = scenarioRepository.getSentencesForScenario(scenario.scenarioId)
                    _uiState.update {
                        it.copy(
                            scenario = scenario,
                            sentences = sentences,
                            isLoading = false
                        )
                    }
                    searchYouTubeVideos(scenario.youtubeQuery)
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "沒有可用的場景") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "載入失敗: ${e.message}") }
            }
        }
    }

    private fun searchYouTubeVideos(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingVideos = true) }
            when (val result = youTubeRepository.searchVideos(query)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(videos = result.data, isLoadingVideos = false, videoError = null)
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoadingVideos = false, videoError = result.message)
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun selectSentence(index: Int) {
        _uiState.update { it.copy(selectedSentenceIndex = index, diffResult = null, recognizedText = "") }
    }

    fun onVideoError() {
        val state = _uiState.value
        val nextIndex = state.currentVideoIndex + 1
        if (nextIndex < state.videos.size) {
            _uiState.update {
                it.copy(currentVideoIndex = nextIndex, videoPlayerError = false)
            }
        } else {
            _uiState.update {
                it.copy(videoPlayerError = true)
            }
        }
    }

    fun skipToNextVideo() {
        val state = _uiState.value
        val nextIndex = state.currentVideoIndex + 1
        if (nextIndex < state.videos.size) {
            _uiState.update {
                it.copy(currentVideoIndex = nextIndex, videoPlayerError = false)
            }
        }
    }

    fun skipToPreviousVideo() {
        val state = _uiState.value
        if (state.currentVideoIndex > 0) {
            _uiState.update {
                it.copy(currentVideoIndex = state.currentVideoIndex - 1, videoPlayerError = false)
            }
        }
    }

    fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.update { it.copy(isListening = true, recognizedText = "", diffResult = null) }
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _uiState.update { it.copy(isListening = false) }
            }

            override fun onError(error: Int) {
                _uiState.update { it.copy(isListening = false) }
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spokenText = matches?.firstOrNull() ?: ""

                val currentState = _uiState.value
                val targetSentence = currentState.sentences.getOrNull(currentState.selectedSentenceIndex)

                if (targetSentence != null && spokenText.isNotEmpty()) {
                    val diff = WordDiffEngine.compare(targetSentence.englishText, spokenText)
                    _uiState.update {
                        it.copy(recognizedText = spokenText, diffResult = diff, isListening = false)
                    }

                    // Save practice history
                    viewModelScope.launch {
                        practiceRepository.savePractice(
                            PracticeHistory(
                                sentenceId = targetSentence.sentenceId,
                                date = LocalDate.now().toString(),
                                userTranscription = spokenText,
                                accuracyScore = diff.accuracy,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(isListening = false) }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                partial?.firstOrNull()?.let { text ->
                    _uiState.update { it.copy(recognizedText = text) }
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    fun createRecognizerIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    fun clearResult() {
        _uiState.update { it.copy(diffResult = null, recognizedText = "") }
    }
}
