package com.bear.englishlearning.ui.screens.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.repository.ExampleSentence
import com.bear.englishlearning.data.repository.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Language(
    val code: String,
    val name: String,
    val flag: String
)

val supportedLanguages = listOf(
    Language("en", "English", "\uD83C\uDDFA\uD83C\uDDF8"),
    Language("zh-TW", "Chinese (Traditional)", "\uD83C\uDDF9\uD83C\uDDFC"),
    Language("zh-CN", "Chinese (Simplified)", "\uD83C\uDDE8\uD83C\uDDF3"),
    Language("ja", "Japanese", "\uD83C\uDDEF\uD83C\uDDF5"),
    Language("ko", "Korean", "\uD83C\uDDF0\uD83C\uDDF7"),
    Language("es", "Spanish", "\uD83C\uDDEA\uD83C\uDDF8"),
    Language("fr", "French", "\uD83C\uDDEB\uD83C\uDDF7"),
    Language("de", "German", "\uD83C\uDDE9\uD83C\uDDEA"),
    Language("pt", "Portuguese", "\uD83C\uDDE7\uD83C\uDDF7"),
    Language("it", "Italian", "\uD83C\uDDEE\uD83C\uDDF9"),
    Language("th", "Thai", "\uD83C\uDDF9\uD83C\uDDED"),
    Language("vi", "Vietnamese", "\uD83C\uDDFB\uD83C\uDDF3"),
    Language("ru", "Russian", "\uD83C\uDDF7\uD83C\uDDFA"),
    Language("ar", "Arabic", "\uD83C\uDDF8\uD83C\uDDE6"),
    Language("hi", "Hindi", "\uD83C\uDDEE\uD83C\uDDF3")
)

sealed interface TranslationUiState {
    data object Idle : TranslationUiState
    data object Loading : TranslationUiState
    data class Success(
        val result: String,
        val alternativeMeanings: List<String> = emptyList(),
        val exampleSentences: List<ExampleSentence> = emptyList()
    ) : TranslationUiState
    data class Error(val message: String) : TranslationUiState
}

data class TranslationScreenState(
    val inputText: String = "",
    val sourceLang: Language = supportedLanguages[0],
    val targetLang: Language = supportedLanguages[1],
    val uiState: TranslationUiState = TranslationUiState.Idle,
    val history: List<TranslationHistoryItem> = emptyList()
)

data class TranslationHistoryItem(
    val sourceText: String,
    val translatedText: String,
    val sourceLang: Language,
    val targetLang: Language
)

@HiltViewModel
class TranslationViewModel @Inject constructor(
    private val repository: TranslationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TranslationScreenState())
    val state: StateFlow<TranslationScreenState> = _state.asStateFlow()

    private var translateJob: Job? = null

    fun updateInputText(text: String) {
        _state.value = _state.value.copy(inputText = text)
    }

    fun setSourceLang(lang: Language) {
        _state.value = _state.value.copy(sourceLang = lang)
    }

    fun setTargetLang(lang: Language) {
        _state.value = _state.value.copy(targetLang = lang)
    }

    fun swapLanguages() {
        val current = _state.value
        _state.value = current.copy(
            sourceLang = current.targetLang,
            targetLang = current.sourceLang,
            inputText = if (current.uiState is TranslationUiState.Success) {
                (current.uiState as TranslationUiState.Success).result
            } else current.inputText,
            uiState = TranslationUiState.Idle
        )
    }

    fun translate() {
        val current = _state.value
        if (current.inputText.isBlank()) return

        translateJob?.cancel()
        translateJob = viewModelScope.launch {
            _state.value = current.copy(uiState = TranslationUiState.Loading)
            val result = repository.translate(
                text = current.inputText,
                sourceLang = current.sourceLang.code,
                targetLang = current.targetLang.code
            )
            result.fold(
                onSuccess = { translationResult ->
                    val historyItem = TranslationHistoryItem(
                        sourceText = current.inputText,
                        translatedText = translationResult.translatedText,
                        sourceLang = current.sourceLang,
                        targetLang = current.targetLang
                    )
                    _state.value = _state.value.copy(
                        uiState = TranslationUiState.Success(
                            result = translationResult.translatedText,
                            alternativeMeanings = translationResult.alternativeMeanings,
                            exampleSentences = translationResult.exampleSentences
                        ),
                        history = listOf(historyItem) + _state.value.history.take(19)
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        uiState = TranslationUiState.Error(
                            error.message ?: "Translation failed"
                        )
                    )
                }
            )
        }
    }

    fun clearInput() {
        _state.value = _state.value.copy(
            inputText = "",
            uiState = TranslationUiState.Idle
        )
    }

    fun loadFromHistory(item: TranslationHistoryItem) {
        _state.value = _state.value.copy(
            inputText = item.sourceText,
            sourceLang = item.sourceLang,
            targetLang = item.targetLang,
            uiState = TranslationUiState.Success(item.translatedText)
        )
    }
}
