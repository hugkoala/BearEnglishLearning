package com.bear.englishlearning.ui.screens.listening

import android.Manifest
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.core.content.ContextCompat
import com.bear.englishlearning.ui.components.BearIcon
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bear.englishlearning.ui.components.SpeechDiffDisplay
import com.bear.englishlearning.ui.components.YouTubePlayerComposable
import com.bear.englishlearning.ui.theme.MissingRed
import java.util.Locale

@Composable
fun ListeningQuizScreen(
    viewModel: ListeningQuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.setLanguage(Locale.US)
                ttsReady = true
            }
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        onDispose {
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.error!!, color = MaterialTheme.colorScheme.error)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BearIcon(size = 32.dp)
                Text(
                    text = "聽力測驗",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            uiState.scenario?.let {
                Text(
                    text = "${it.titleZh}（${it.title}）",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // YouTube Player
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📺 相關影片",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when {
                        uiState.isLoadingVideos -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator() }
                        }
                        uiState.videos.isNotEmpty() && !uiState.videoPlayerError -> {
                            val currentVideo = uiState.videos[uiState.currentVideoIndex]
                            YouTubePlayerComposable(
                                videoId = currentVideo.videoId,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                onError = { viewModel.onVideoError() }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentVideo.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                            // Video navigation
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.skipToPreviousVideo() },
                                    enabled = uiState.currentVideoIndex > 0
                                ) {
                                    Icon(Icons.Default.SkipPrevious, "上一部")
                                }
                                Text(
                                    text = "${uiState.currentVideoIndex + 1} / ${uiState.videos.size}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                IconButton(
                                    onClick = { viewModel.skipToNextVideo() },
                                    enabled = uiState.currentVideoIndex < uiState.videos.size - 1
                                ) {
                                    Icon(Icons.Default.SkipNext, "下一部")
                                }
                                IconButton(onClick = { viewModel.clearCacheAndRetry() }) {
                                    Icon(Icons.Default.Refresh, "重新載入")
                                }
                            }
                            // Show auto-skip status
                            uiState.lastPlayerError?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        uiState.videoPlayerError -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "⚠️ 所有影片都無法播放",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                uiState.lastPlayerError?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(onClick = { viewModel.clearCacheAndRetry() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("清除快取並重試")
                                }
                            }
                        }
                        uiState.videoError != null -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "⚠️ ${uiState.videoError}",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                OutlinedButton(onClick = { viewModel.clearCacheAndRetry() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("重試")
                                }
                            }
                        }
                        else -> {
                            Text(
                                text = "沒有找到相關影片",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Sentence Selector
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🗣 模擬對話 — 選擇句子練習",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.sentences.forEachIndexed { index, _ ->
                            FilterChip(
                                selected = uiState.selectedSentenceIndex == index,
                                onClick = { viewModel.selectSentence(index) },
                                label = { Text("${index + 1}") }
                            )
                        }
                    }

                    val selectedSentence = uiState.sentences.getOrNull(uiState.selectedSentenceIndex)
                    if (selectedSentence != null) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = selectedSentence.englishText,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = selectedSentence.chineseText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 ${selectedSentence.pronunciationTip}",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            IconButton(
                                onClick = {
                                    if (ttsReady) {
                                        tts?.setSpeechRate(0.7f)
                                        tts?.speak(selectedSentence.englishText, TextToSpeech.QUEUE_FLUSH, null, "slow")
                                    }
                                },
                                enabled = ttsReady
                            ) {
                                Icon(Icons.Default.SlowMotionVideo, "慢速")
                            }
                            IconButton(
                                onClick = {
                                    if (ttsReady) {
                                        tts?.setSpeechRate(1.0f)
                                        tts?.speak(selectedSentence.englishText, TextToSpeech.QUEUE_FLUSH, null, "normal")
                                    }
                                },
                                enabled = ttsReady
                            ) {
                                Icon(Icons.Default.PlayArrow, "正常速度")
                            }
                        }
                    }
                }
            }
        }

        // Speech Practice Section
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎤 口說練習",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mic button
                    val hasMicPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED

                    FilledIconButton(
                        onClick = {
                            if (hasMicPermission && !uiState.isListening) {
                                val listener = viewModel.createRecognitionListener()
                                speechRecognizer?.setRecognitionListener(listener)
                                speechRecognizer?.startListening(viewModel.createRecognizerIntent())
                            } else if (uiState.isListening) {
                                speechRecognizer?.stopListening()
                            }
                        },
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (uiState.isListening)
                                MissingRed else MaterialTheme.colorScheme.primary
                        ),
                        enabled = hasMicPermission
                    ) {
                        Icon(
                            imageVector = if (uiState.isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = if (uiState.isListening) "停止" else "開始錄音",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (uiState.isListening) "正在聆聽..." else if (!hasMicPermission) "需要麥克風權限" else "按下麥克風開始說話",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Recognized text
                    if (uiState.recognizedText.isNotEmpty() && uiState.diffResult == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "你說的：${uiState.recognizedText}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Diff result
                    uiState.diffResult?.let { diffResult ->
                        Spacer(modifier = Modifier.height(16.dp))
                        SpeechDiffDisplay(result = diffResult)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            IconButton(onClick = { viewModel.clearResult() }) {
                                Icon(Icons.Default.Refresh, "再試一次")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "再試一次",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
