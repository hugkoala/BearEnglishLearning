package com.bear.englishlearning.ui.screens.dailytask

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bear.englishlearning.data.local.entity.Sentence
import com.bear.englishlearning.domain.conversation.GeneratedConversation
import com.bear.englishlearning.domain.scenario.GeneratedSentence
import com.bear.englishlearning.domain.vocabulary.VocabularyWord
import com.bear.englishlearning.ui.components.BearIcon
import com.bear.englishlearning.ui.theme.MatchGreen
import java.util.Locale

@Composable
fun DailyTaskScreen(
    onNavigateToListening: () -> Unit = {},
    onNavigateToVocabulary: () -> Unit = {},
    onNavigateToConversation: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: DailyTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentMode by viewModel.mode.collectAsStateWithLifecycle()
    val vocabularyPreview by viewModel.vocabularyPreview.collectAsStateWithLifecycle()
    val conversationPreview by viewModel.conversationPreview.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.setLanguage(Locale.US)
                ttsReady = true
            }
        }
        onDispose { tts?.shutdown() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Mode toggle chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentMode == DailyTaskMode.PRESET,
                onClick = { viewModel.switchMode(DailyTaskMode.PRESET) },
                label = { Text("📖 預設場景") }
            )
            FilterChip(
                selected = currentMode == DailyTaskMode.GENERATED,
                onClick = { viewModel.switchMode(DailyTaskMode.GENERATED) },
                label = { Text("🎲 隨機生成") }
            )
        }

        when (val state = uiState) {
            is DailyTaskUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DailyTaskUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is DailyTaskUiState.Success -> {
                PresetTaskContent(
                    state = state,
                    tts = tts,
                    ttsReady = ttsReady,
                    vocabularyPreview = vocabularyPreview,
                    conversationPreview = conversationPreview,
                    onComplete = { viewModel.completeTask() },
                    onNavigateToListening = onNavigateToListening,
                    onNavigateToVocabulary = onNavigateToVocabulary,
                    onNavigateToConversation = onNavigateToConversation,
                    onNavigateToCalendar = onNavigateToCalendar,
                    onNavigateToSettings = onNavigateToSettings
                )
            }
            is DailyTaskUiState.GeneratedSuccess -> {
                GeneratedTaskContent(
                    state = state,
                    tts = tts,
                    ttsReady = ttsReady,
                    vocabularyPreview = vocabularyPreview,
                    conversationPreview = conversationPreview,
                    onNavigateToListening = onNavigateToListening,
                    onNavigateToVocabulary = onNavigateToVocabulary,
                    onNavigateToConversation = onNavigateToConversation,
                    onNavigateToCalendar = onNavigateToCalendar,
                    onNavigateToSettings = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun PresetTaskContent(
    state: DailyTaskUiState.Success,
    tts: TextToSpeech?,
    ttsReady: Boolean,
    vocabularyPreview: List<VocabularyWord>,
    conversationPreview: GeneratedConversation?,
    onComplete: () -> Unit,
    onNavigateToListening: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToConversation: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BearIcon(size = 36.dp)
                        Text(
                            text = "今日任務",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "學習日曆")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "場景：${state.scenario.titleZh}（${state.scenario.title}）",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "每日任務：${state.sentences.size} / ${state.sentenceCount} 句",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (state.task.isCompleted) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MatchGreen.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, "完成", tint = MatchGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("今日任務已完成 ✅", color = MatchGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        itemsIndexed(state.sentences) { index, sentence ->
            SentenceCard(
                index = index + 1,
                sentence = sentence,
                tts = tts,
                ttsReady = ttsReady
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!state.task.isCompleted) {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.weight(1f)
                    ) { Text("完成任務 ✅") }
                }
                FilledTonalButton(
                    onClick = onNavigateToListening,
                    modifier = Modifier.weight(1f)
                ) { Text("前往練習 🎧") }
            }
        }

        // Quick-access sections
        item {
            QuickAccessSection(
                tts = tts,
                ttsReady = ttsReady,
                vocabularyPreview = vocabularyPreview,
                conversationPreview = conversationPreview,
                onNavigateToVocabulary = onNavigateToVocabulary,
                onNavigateToListening = onNavigateToListening,
                onNavigateToConversation = onNavigateToConversation
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun GeneratedTaskContent(
    state: DailyTaskUiState.GeneratedSuccess,
    tts: TextToSpeech?,
    ttsReady: Boolean,
    vocabularyPreview: List<VocabularyWord>,
    conversationPreview: GeneratedConversation?,
    onNavigateToListening: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToConversation: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val sentences = state.generatedScenario.sentences.take(state.sentenceCount)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BearIcon(size = 36.dp)
                        Text(
                            text = "🎲 隨機場景",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onNavigateToCalendar) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "學習日曆")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "場景：${state.generatedScenario.titleZh}（${state.generatedScenario.title}）",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "每日練習：${sentences.size} 句（每天自動更換）",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        itemsIndexed(sentences) { index, sentence ->
            GeneratedSentenceCard(
                index = index + 1,
                sentence = sentence,
                tts = tts,
                ttsReady = ttsReady
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(
                onClick = onNavigateToListening,
                modifier = Modifier.fillMaxWidth()
            ) { Text("前往練習 🎧") }
        }

        // Quick-access sections
        item {
            QuickAccessSection(
                tts = tts,
                ttsReady = ttsReady,
                vocabularyPreview = vocabularyPreview,
                conversationPreview = conversationPreview,
                onNavigateToVocabulary = onNavigateToVocabulary,
                onNavigateToListening = onNavigateToListening,
                onNavigateToConversation = onNavigateToConversation
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun QuickAccessSection(
    tts: TextToSpeech?,
    ttsReady: Boolean,
    vocabularyPreview: List<VocabularyWord>,
    conversationPreview: GeneratedConversation?,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToListening: () -> Unit,
    onNavigateToConversation: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "📋 今日學習總覽",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Vocabulary Preview Card
        VocabularyPreviewCard(
            words = vocabularyPreview,
            tts = tts,
            ttsReady = ttsReady,
            onNavigateToVocabulary = onNavigateToVocabulary
        )

        // Listening Practice Card
        ListeningQuickCard(onNavigateToListening = onNavigateToListening)

        // Conversation Preview Card
        ConversationPreviewCard(
            conversation = conversationPreview,
            onNavigateToConversation = onNavigateToConversation
        )
    }
}

@Composable
private fun VocabularyPreviewCard(
    words: List<VocabularyWord>,
    tts: TextToSpeech?,
    ttsReady: Boolean,
    onNavigateToVocabulary: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Spellcheck,
                        contentDescription = "單字",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "📚 今日單字預覽",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            words.forEach { word ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${word.word} (${word.partOfSpeech})",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = word.meaningZh,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            if (ttsReady) {
                                tts?.setSpeechRate(0.9f)
                                tts?.speak(word.word, TextToSpeech.QUEUE_FLUSH, null, "vocab_${word.word}")
                            }
                        },
                        enabled = ttsReady,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "播放發音",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                onClick = onNavigateToVocabulary,
                modifier = Modifier.fillMaxWidth()
            ) { Text("查看全部 10 個單字 📖") }
        }
    }
}

@Composable
private fun ListeningQuickCard(onNavigateToListening: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Hearing,
                    contentDescription = "聽力",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "🎧 聽力練習",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "觀看 YouTube 英語影片，訓練聽力理解能力",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(
                onClick = onNavigateToListening,
                modifier = Modifier.fillMaxWidth()
            ) { Text("開始聽力練習 🎬") }
        }
    }
}

@Composable
private fun ConversationPreviewCard(
    conversation: GeneratedConversation?,
    onNavigateToConversation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Forum,
                    contentDescription = "對話",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "💬 模擬對話",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (conversation != null) {
                Text(
                    text = "推薦場景：${conversation.conversation.titleZh}（${conversation.conversation.title}）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Show first 2 lines as preview
                conversation.lines.take(2).forEach { line ->
                    Text(
                        text = "${line.speaker}: ${line.englishText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                onClick = onNavigateToConversation,
                modifier = Modifier.fillMaxWidth()
            ) { Text("開始對話練習 🗣️") }
        }
    }
}

@Composable
private fun SentenceCard(
    index: Int,
    sentence: Sentence,
    tts: TextToSpeech?,
    ttsReady: Boolean
) {
    SentenceCardContent(
        index = index,
        englishText = sentence.englishText,
        chineseText = sentence.chineseText,
        pronunciationTip = sentence.pronunciationTip,
        tts = tts,
        ttsReady = ttsReady
    )
}

@Composable
private fun GeneratedSentenceCard(
    index: Int,
    sentence: GeneratedSentence,
    tts: TextToSpeech?,
    ttsReady: Boolean
) {
    SentenceCardContent(
        index = index,
        englishText = sentence.englishText,
        chineseText = sentence.chineseText,
        pronunciationTip = sentence.pronunciationTip,
        tts = tts,
        ttsReady = ttsReady
    )
}

@Composable
private fun SentenceCardContent(
    index: Int,
    englishText: String,
    chineseText: String,
    pronunciationTip: String,
    tts: TextToSpeech?,
    ttsReady: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "句子 $index",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Row {
                    // Slow speed button
                    IconButton(
                        onClick = {
                            if (ttsReady) {
                                tts?.setSpeechRate(0.7f)
                                tts?.speak(englishText, TextToSpeech.QUEUE_FLUSH, null, "slow_$index")
                            }
                        },
                        enabled = ttsReady
                    ) {
                        Icon(
                            Icons.Default.SlowMotionVideo,
                            contentDescription = "慢速播放",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    // Normal speed button
                    IconButton(
                        onClick = {
                            if (ttsReady) {
                                tts?.setSpeechRate(1.0f)
                                tts?.speak(englishText, TextToSpeech.QUEUE_FLUSH, null, "normal_$index")
                            }
                        },
                        enabled = ttsReady
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "正常速度播放",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = englishText,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chineseText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "💡 $pronunciationTip",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
