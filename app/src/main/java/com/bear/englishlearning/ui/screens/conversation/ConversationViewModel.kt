package com.bear.englishlearning.ui.screens.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine
import com.bear.englishlearning.data.repository.ConversationRepository
import com.bear.englishlearning.domain.conversation.RandomConversationGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ConversationMode {
    PRESET,   // From database seed data
    RANDOM    // Auto-generated random conversations
}

sealed interface ConversationUiState {
    data object Loading : ConversationUiState
    data class Success(
        val conversation: Conversation,
        val lines: List<ConversationLine>,
        val revealedCount: Int = 0,
        val showTranslation: Boolean = false,
        val mode: ConversationMode = ConversationMode.PRESET
    ) : ConversationUiState
    data class Error(val message: String) : ConversationUiState
}

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val randomGenerator: RandomConversationGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConversationUiState>(ConversationUiState.Loading)
    val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    private val _mode = MutableStateFlow(ConversationMode.PRESET)
    val mode: StateFlow<ConversationMode> = _mode.asStateFlow()

    init {
        loadRandomConversation()
    }

    fun switchMode(newMode: ConversationMode) {
        _mode.value = newMode
        when (newMode) {
            ConversationMode.PRESET -> loadRandomConversation()
            ConversationMode.RANDOM -> loadGeneratedConversation()
        }
    }

    fun loadRandomConversation() {
        viewModelScope.launch {
            _uiState.value = ConversationUiState.Loading
            try {
                val conversation = conversationRepository.getRandomConversation()
                if (conversation != null) {
                    val lines = conversationRepository.getLinesForConversation(conversation.conversationId)
                    _uiState.value = ConversationUiState.Success(
                        conversation = conversation,
                        lines = lines,
                        revealedCount = 0,
                        showTranslation = false,
                        mode = ConversationMode.PRESET
                    )
                } else {
                    _uiState.value = ConversationUiState.Error("沒有可用的對話")
                }
            } catch (e: Exception) {
                _uiState.value = ConversationUiState.Error("載入失敗: ${e.message}")
            }
        }
    }

    fun loadGeneratedConversation() {
        _uiState.value = ConversationUiState.Loading
        try {
            val generated = randomGenerator.generate()
            _uiState.value = ConversationUiState.Success(
                conversation = generated.conversation,
                lines = generated.lines,
                revealedCount = 0,
                showTranslation = false,
                mode = ConversationMode.RANDOM
            )
        } catch (e: Exception) {
            _uiState.value = ConversationUiState.Error("生成對話失敗: ${e.message}")
        }
    }

    fun loadNext() {
        when (_mode.value) {
            ConversationMode.PRESET -> loadRandomConversation()
            ConversationMode.RANDOM -> loadGeneratedConversation()
        }
    }

    fun revealNextLine() {
        val current = _uiState.value
        if (current is ConversationUiState.Success) {
            if (current.revealedCount < current.lines.size) {
                _uiState.value = current.copy(revealedCount = current.revealedCount + 1)
            }
        }
    }

    fun toggleTranslation() {
        val current = _uiState.value
        if (current is ConversationUiState.Success) {
            _uiState.value = current.copy(showTranslation = !current.showTranslation)
        }
    }

    fun resetConversation() {
        val current = _uiState.value
        if (current is ConversationUiState.Success) {
            _uiState.value = current.copy(revealedCount = 0, showTranslation = false)
        }
    }
}
