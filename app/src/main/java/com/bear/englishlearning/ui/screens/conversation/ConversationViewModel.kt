package com.bear.englishlearning.ui.screens.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.Conversation
import com.bear.englishlearning.data.local.entity.ConversationLine
import com.bear.englishlearning.data.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ConversationUiState {
    data object Loading : ConversationUiState
    data class Success(
        val conversation: Conversation,
        val lines: List<ConversationLine>,
        val revealedCount: Int = 0,
        val showTranslation: Boolean = false
    ) : ConversationUiState
    data class Error(val message: String) : ConversationUiState
}

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConversationUiState>(ConversationUiState.Loading)
    val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    init {
        loadRandomConversation()
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
                        showTranslation = false
                    )
                } else {
                    _uiState.value = ConversationUiState.Error("沒有可用的對話")
                }
            } catch (e: Exception) {
                _uiState.value = ConversationUiState.Error("載入失敗: ${e.message}")
            }
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
