package com.bear.englishlearning.ui.screens.realtimeconversation

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import com.bear.englishlearning.domain.conversation.ConversationEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val textZh: String = "",
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

sealed interface RealTimeConversationUiState {
    data object Idle : RealTimeConversationUiState
    data object Listening : RealTimeConversationUiState
    data object Processing : RealTimeConversationUiState
    data object Speaking : RealTimeConversationUiState
}

@HiltViewModel
class RealTimeConversationViewModel @Inject constructor(
    private val conversationEngine: ConversationEngine
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<RealTimeConversationUiState>(RealTimeConversationUiState.Idle)
    val uiState: StateFlow<RealTimeConversationUiState> = _uiState.asStateFlow()

    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText.asStateFlow()

    private val _currentTopic = MutableStateFlow(conversationEngine.getCurrentTopic())
    val currentTopic: StateFlow<ConversationEngine.ConversationTopic> = _currentTopic.asStateFlow()

    private val _showTranslation = MutableStateFlow(true)
    val showTranslation: StateFlow<Boolean> = _showTranslation.asStateFlow()

    fun getAllTopics() = conversationEngine.getAllTopics()

    fun selectTopic(topicId: String) {
        conversationEngine.setTopic(topicId)
        _currentTopic.value = conversationEngine.getCurrentTopic()
        startNewConversation()
    }

    fun startNewConversation() {
        val topic = conversationEngine.getCurrentTopic()
        _currentTopic.value = topic
        _messages.value = listOf(
            ChatMessage(
                text = topic.greeting,
                textZh = topic.greetingZh,
                isUser = false
            )
        )
        _uiState.value = RealTimeConversationUiState.Idle
        _partialText.value = ""
    }

    fun randomTopic() {
        val topic = conversationEngine.getRandomTopic()
        _currentTopic.value = topic
        startNewConversation()
    }

    fun toggleTranslation() {
        _showTranslation.value = !_showTranslation.value
    }

    fun setListening() {
        _uiState.value = RealTimeConversationUiState.Listening
        _partialText.value = ""
    }

    fun onSpeechResult(recognizedText: String) {
        if (recognizedText.isBlank()) {
            _uiState.value = RealTimeConversationUiState.Idle
            return
        }

        // Add user message
        val userMessage = ChatMessage(
            text = recognizedText,
            isUser = true
        )
        _messages.value = _messages.value + userMessage
        _partialText.value = ""

        // Generate reply
        _uiState.value = RealTimeConversationUiState.Processing
        val history = _messages.value.filter { !it.isUser }.map { it.text }
        val (replyEn, replyZh) = conversationEngine.generateReply(recognizedText, history)

        val replyMessage = ChatMessage(
            text = replyEn,
            textZh = replyZh,
            isUser = false
        )
        _messages.value = _messages.value + replyMessage
        _uiState.value = RealTimeConversationUiState.Speaking
    }

    fun onSpeakingDone() {
        _uiState.value = RealTimeConversationUiState.Idle
    }

    fun onListeningError() {
        _uiState.value = RealTimeConversationUiState.Idle
        _partialText.value = ""
    }

    fun clearConversation() {
        _messages.value = emptyList()
        _uiState.value = RealTimeConversationUiState.Idle
        _partialText.value = ""
    }

    fun createRecognizerIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    fun createRecognitionListener(
        onPartialResult: (String) -> Unit = {}
    ): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.value = RealTimeConversationUiState.Listening
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _uiState.value = RealTimeConversationUiState.Processing
            }

            override fun onError(error: Int) {
                onListeningError()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                onSpeechResult(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                _partialText.value = text
                onPartialResult(text)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
}
