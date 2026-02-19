package com.bear.englishlearning.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import com.bear.englishlearning.domain.vocabulary.DailyVocabularyGenerator
import com.bear.englishlearning.domain.vocabulary.VocabularyWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class VocabularyUiState(
    val words: List<VocabularyWord> = emptyList(),
    val dateLabel: String = "",
    val expandedIndex: Int? = null
)

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val generator: DailyVocabularyGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(VocabularyUiState())
    val uiState: StateFlow<VocabularyUiState> = _uiState.asStateFlow()

    init {
        loadTodayWords()
    }

    fun loadTodayWords() {
        val today = LocalDate.now()
        val words = generator.generateForDate(today)
        val label = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd (E)"))
        _uiState.value = VocabularyUiState(
            words = words,
            dateLabel = label,
            expandedIndex = null
        )
    }

    fun toggleExpanded(index: Int) {
        val current = _uiState.value
        _uiState.value = current.copy(
            expandedIndex = if (current.expandedIndex == index) null else index
        )
    }

    fun refresh() {
        loadTodayWords()
    }
}
