package com.bear.englishlearning.ui.screens.onboarding

import android.Manifest
import android.content.Intent
import android.os.Build
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bear.englishlearning.ui.theme.MatchGreen
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> WelcomePage()
                1 -> TtsSetupPage()
                2 -> SpeechRecognitionPage()
                3 -> MicPermissionPage()
                4 -> NotificationPermissionPage()
            }
        }

        // Page indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                val color = if (pagerState.currentPage == index)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                }) { Text("上一步") }
            } else {
                Spacer(Modifier.width(80.dp))
            }

            if (pagerState.currentPage < 4) {
                Button(onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }) { Text("下一步") }
            } else {
                Button(onClick = onComplete) { Text("開始學習！") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun OnboardingPageTemplate(
    icon: ImageVector,
    title: String,
    description: String,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        content()
    }
}

@Composable
private fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🐻", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "歡迎使用 Bear English",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "每天學三句英文，輕鬆開口說！\n讓我們先完成一些設定",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TtsSetupPage() {
    val context = LocalContext.current
    var ttsStatus by remember { mutableStateOf("checking") }

    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.isLanguageAvailable(Locale.US)
                ttsStatus = when (result) {
                    TextToSpeech.LANG_AVAILABLE,
                    TextToSpeech.LANG_COUNTRY_AVAILABLE,
                    TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE -> "available"
                    TextToSpeech.LANG_MISSING_DATA -> "missing"
                    else -> "not_supported"
                }
            } else {
                ttsStatus = "error"
            }
        }
        onDispose { tts?.shutdown() }
    }

    OnboardingPageTemplate(
        icon = Icons.Default.VolumeUp,
        title = "語音朗讀設定",
        description = "我們需要英文語音包來為你朗讀句子"
    ) {
        when (ttsStatus) {
            "checking" -> CircularProgressIndicator()
            "available" -> {
                Icon(Icons.Default.CheckCircle, "已就緒", tint = MatchGreen, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ 英文語音已就緒！", color = MatchGreen)
            }
            "missing" -> {
                OutlinedButton(onClick = {
                    val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("下載語音包")
                }
            }
            else -> {
                Text("⚠️ TTS 引擎不可用，請確認裝置設定", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun SpeechRecognitionPage() {
    val context = LocalContext.current
    var modelStatus by remember { mutableStateOf("checking") }

    DisposableEffect(Unit) {
        modelStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
        ) {
            "available"
        } else if (SpeechRecognizer.isRecognitionAvailable(context)) {
            "cloud_only"
        } else {
            "not_available"
        }
        onDispose { }
    }

    OnboardingPageTemplate(
        icon = Icons.Default.RecordVoiceOver,
        title = "語音辨識設定",
        description = "用來辨識你的口說練習內容"
    ) {
        when (modelStatus) {
            "checking" -> CircularProgressIndicator()
            "available" -> {
                Icon(Icons.Default.CheckCircle, "已就緒", tint = MatchGreen, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ 離線語音辨識已就緒！", color = MatchGreen)
            }
            "cloud_only" -> {
                Icon(Icons.Default.CheckCircle, "雲端", tint = MatchGreen, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("☁️ 將使用雲端語音辨識\n（需要網路連線）",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            else -> {
                Text("⚠️ 語音辨識不可用", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun MicPermissionPage() {
    var granted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> granted = isGranted }

    OnboardingPageTemplate(
        icon = Icons.Default.Mic,
        title = "麥克風權限",
        description = "需要麥克風來錄製你的口說練習"
    ) {
        if (granted) {
            Icon(Icons.Default.CheckCircle, "已授權", tint = MatchGreen, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("✅ 麥克風權限已授予！", color = MatchGreen)
        } else {
            Button(onClick = {
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }) {
                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("授予麥克風權限")
            }
        }
    }
}

@Composable
private fun NotificationPermissionPage() {
    var granted by remember { mutableStateOf(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> granted = isGranted }

    OnboardingPageTemplate(
        icon = Icons.Default.Notifications,
        title = "通知提醒",
        description = "開啟通知，每天提醒你複習昨天的學習重點"
    ) {
        if (granted) {
            Icon(Icons.Default.CheckCircle, "已授權", tint = MatchGreen, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("✅ 通知已啟用！", color = MatchGreen)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Button(onClick = {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) {
                    Icon(Icons.Default.Notifications, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("允許通知")
                }
            }
        }
    }
}
