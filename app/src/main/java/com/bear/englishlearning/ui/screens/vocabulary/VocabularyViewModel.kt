package com.bear.englishlearning.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.CustomWord
import com.bear.englishlearning.data.repository.CustomWordRepository
import com.bear.englishlearning.domain.vocabulary.DailyVocabularyGenerator
import com.bear.englishlearning.domain.vocabulary.VocabularyWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

enum class VocabularyTab { DAILY, MY_WORDS }

data class VocabularyUiState(
    val words: List<VocabularyWord> = emptyList(),
    val customWords: List<CustomWord> = emptyList(),
    val dateLabel: String = "",
    val expandedIndex: Int? = null,
    val currentTab: VocabularyTab = VocabularyTab.DAILY,
    val showAddDialog: Boolean = false
)

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val generator: DailyVocabularyGenerator,
    private val customWordRepository: CustomWordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VocabularyUiState())
    val uiState: StateFlow<VocabularyUiState> = _uiState.asStateFlow()

    init {
        loadTodayWords()
        loadCustomWords()
    }

    fun loadTodayWords() {
        val today = LocalDate.now()
        val words = generator.generateForDate(today)
        val label = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd (E)"))
        _uiState.value = _uiState.value.copy(
            words = words,
            dateLabel = label,
            expandedIndex = null
        )
    }

    private fun loadCustomWords() {
        viewModelScope.launch {
            customWordRepository.getAllWords().collect { words ->
                _uiState.value = _uiState.value.copy(customWords = words)
            }
        }
    }

    fun switchTab(tab: VocabularyTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab, expandedIndex = null)
    }

    fun toggleExpanded(index: Int) {
        val current = _uiState.value
        _uiState.value = current.copy(
            expandedIndex = if (current.expandedIndex == index) null else index
        )
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun addCustomWord(
        word: String,
        meaningEn: String,
        meaningZh: String,
        partOfSpeech: String = "",
        exampleSentence: String = "",
        exampleZh: String = ""
    ) {
        if (word.isBlank()) return
        viewModelScope.launch {
            customWordRepository.addWord(
                CustomWord(
                    word = word.trim(),
                    meaningEn = meaningEn.trim(),
                    meaningZh = meaningZh.trim(),
                    partOfSpeech = partOfSpeech.trim(),
                    exampleSentence = exampleSentence.trim(),
                    exampleZh = exampleZh.trim()
                )
            )
            _uiState.value = _uiState.value.copy(showAddDialog = false)
        }
    }

    fun deleteCustomWord(word: CustomWord) {
        viewModelScope.launch {
            customWordRepository.deleteWord(word)
        }
    }

    fun refresh() {
        loadTodayWords()
    }
}
