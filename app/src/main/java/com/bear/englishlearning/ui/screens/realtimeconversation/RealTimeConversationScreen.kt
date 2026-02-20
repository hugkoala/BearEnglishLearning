package com.bear.englishlearning.ui.screens.realtimeconversation

import android.Manifest
import android.os.Handler
import android.os.Looper
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bear.englishlearning.ui.components.BearIcon
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealTimeConversationScreen(
    onBack: () -> Unit = {},
    viewModel: RealTimeConversationViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val partialText by viewModel.partialText.collectAsStateWithLifecycle()
    val currentTopic by viewModel.currentTopic.collectAsStateWithLifecycle()
    val showTranslation by viewModel.showTranslation.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // TTS setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    // SpeechRecognizer setup
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var speechAvailable by remember { mutableStateOf(false) }
    var showTopicMenu by remember { mutableStateOf(false) }
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
        if (!granted) {
            Toast.makeText(context, "需要麥克風權限才能對話", Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.setLanguage(Locale.US)
                tts?.setSpeechRate(0.9f)
                // Set up UtteranceProgressListener ONCE here
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        mainHandler.post { viewModel.onSpeakingDone() }
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        mainHandler.post { viewModel.onSpeakingDone() }
                    }
                })
                ttsReady = true
            }
        }
        speechAvailable = SpeechRecognizer.isRecognitionAvailable(context)
        if (speechAvailable) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }

    // Start conversation on first load
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            viewModel.startNewConversation()
        }
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Auto-speak bot messages
    LaunchedEffect(uiState) {
        if (uiState is RealTimeConversationUiState.Speaking && ttsReady) {
            val lastBotMessage = messages.lastOrNull { !it.isUser }
            if (lastBotMessage != null) {
                tts?.speak(lastBotMessage.text, TextToSpeech.QUEUE_FLUSH, null, "reply_${lastBotMessage.id}")
            }
        }
    }

    // Speak greeting when conversation starts
    LaunchedEffect(messages) {
        if (messages.size == 1 && !messages[0].isUser && ttsReady) {
            tts?.speak(messages[0].text, TextToSpeech.QUEUE_FLUSH, null, "greeting")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("🗣️ 即時對話", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${currentTopic.titleZh}（${currentTopic.title}）",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        tts?.stop()
                        speechRecognizer?.stopListening()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleTranslation() }) {
                        Icon(
                            Icons.Default.Translate,
                            contentDescription = "切換翻譯",
                            tint = if (showTranslation) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box {
                        IconButton(onClick = { showTopicMenu = true }) {
                            Icon(Icons.Default.Refresh, contentDescription = "換話題")
                        }
                        DropdownMenu(
                            expanded = showTopicMenu,
                            onDismissRequest = { showTopicMenu = false }
                        ) {
                            viewModel.getAllTopics().forEach { topic ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${topic.titleZh}（${topic.title}）")
                                    },
                                    onClick = {
                                        tts?.stop()
                                        viewModel.selectTopic(topic.id)
                                        showTopicMenu = false
                                    }
                                )
                            }
                        }
                    }
                    IconButton(onClick = {
                        tts?.stop()
                        viewModel.clearConversation()
                        viewModel.randomTopic()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "清除對話")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Chat messages area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Topic header
                item {
                    TopicHeader(topic = currentTopic)
                }

                items(messages, key = { it.id }) { message ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        ChatBubble(
                            message = message,
                            showTranslation = showTranslation,
                            onTapToSpeak = { text ->
                                if (ttsReady) {
                                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tap_${System.currentTimeMillis()}")
                                }
                            }
                        )
                    }
                }

                // Show partial recognition text
                if (partialText.isNotBlank() && uiState is RealTimeConversationUiState.Listening) {
                    item {
                        PartialTextBubble(text = partialText)
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // Status bar
            StatusBar(uiState = uiState)

            // Microphone button area
            MicrophoneArea(
                uiState = uiState,
                onStartListening = {
                    if (!hasMicPermission) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        return@MicrophoneArea
                    }
                    if (!speechAvailable) {
                        Toast.makeText(context, "此裝置不支援語音辨識", Toast.LENGTH_SHORT).show()
                        return@MicrophoneArea
                    }
                    tts?.stop()
                    viewModel.setListening()
                    speechRecognizer?.setRecognitionListener(
                        viewModel.createRecognitionListener()
                    )
                    speechRecognizer?.startListening(viewModel.createRecognizerIntent())
                },
                onStopListening = {
                    speechRecognizer?.stopListening()
                }
            )
        }
    }
}

@Composable
private fun TopicHeader(topic: com.bear.englishlearning.domain.conversation.ConversationEngine.ConversationTopic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BearIcon(size = 48.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🎭 ${topic.titleZh}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = topic.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "點擊麥克風開始對話 🎤",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    showTranslation: Boolean,
    onTapToSpeak: (String) -> Unit
) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor = if (message.isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val shape = if (message.isUser) {
        RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        // Speaker label
        Text(
            text = if (message.isUser) "🧑 You" else "🐻 Bear",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )

        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(shape)
                .background(bgColor)
                .clickable { onTapToSpeak(message.text) }
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.onTertiaryContainer
                )
                if (showTranslation && message.textZh.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.textZh,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                               else MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PartialTextBubble(text: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "🧑 You",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                .padding(12.dp)
        ) {
            Text(
                text = "$text ...",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun StatusBar(uiState: RealTimeConversationUiState) {
    val statusText = when (uiState) {
        is RealTimeConversationUiState.Idle -> "準備就緒 — 按住麥克風說話"
        is RealTimeConversationUiState.Listening -> "🎤 正在聆聽..."
        is RealTimeConversationUiState.Processing -> "🤔 思考中..."
        is RealTimeConversationUiState.Speaking -> "🔊 正在說話..."
    }

    val bgColor = when (uiState) {
        is RealTimeConversationUiState.Listening -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        is RealTimeConversationUiState.Speaking -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        is RealTimeConversationUiState.Processing -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MicrophoneArea(
    uiState: RealTimeConversationUiState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit
) {
    val isListening = uiState is RealTimeConversationUiState.Listening
    val isProcessing = uiState is RealTimeConversationUiState.Processing
    val isSpeaking = uiState is RealTimeConversationUiState.Speaking

    // Pulsing animation for listening state
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isListening) {
                // Listening indicator ring
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    LargeFloatingActionButton(
                        onClick = onStopListening,
                        containerColor = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    ) {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "停止聆聽",
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            } else {
                LargeFloatingActionButton(
                    onClick = {
                        if (!isProcessing && !isSpeaking) {
                            onStartListening()
                        }
                    },
                    containerColor = if (isProcessing || isSpeaking)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "開始說話",
                        modifier = Modifier.size(36.dp),
                        tint = if (isProcessing || isSpeaking)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Text(
                text = when {
                    isListening -> "點擊停止"
                    isProcessing -> "正在處理..."
                    isSpeaking -> "Bear 正在回覆..."
                    else -> "點擊開始說話"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
