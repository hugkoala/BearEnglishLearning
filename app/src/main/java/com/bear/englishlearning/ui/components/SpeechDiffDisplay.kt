package com.bear.englishlearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bear.englishlearning.domain.model.DiffWord
import com.bear.englishlearning.domain.model.SpeechDiffResult
import com.bear.englishlearning.domain.model.WordStatus
import com.bear.englishlearning.ui.theme.ExtraBgLight
import com.bear.englishlearning.ui.theme.ExtraOrange
import com.bear.englishlearning.ui.theme.MatchBgLight
import com.bear.englishlearning.ui.theme.MatchGreen
import com.bear.englishlearning.ui.theme.MissingBgLight
import com.bear.englishlearning.ui.theme.MissingRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpeechDiffDisplay(
    result: SpeechDiffResult,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AccuracyScoreBar(result)
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            result.diffWords.forEach { diffWord ->
                WordChip(diffWord)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        DiffLegend()
    }
}

@Composable
private fun AccuracyScoreBar(result: SpeechDiffResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "準確度",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result.accuracyPercent,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = when {
                    result.accuracy >= 0.8f -> MatchGreen
                    result.accuracy >= 0.5f -> ExtraOrange
                    else -> MissingRed
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { result.accuracy },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = when {
                    result.accuracy >= 0.8f -> MatchGreen
                    result.accuracy >= 0.5f -> ExtraOrange
                    else -> MissingRed
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${result.matchedCount} / ${result.targetWordCount} 個單詞正確",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WordChip(diffWord: DiffWord) {
    val (bgColor, textColor, decoration) = when (diffWord.status) {
        WordStatus.MATCH -> Triple(MatchBgLight, MatchGreen, TextDecoration.None)
        WordStatus.MISSING -> Triple(MissingBgLight, MissingRed, TextDecoration.LineThrough)
        WordStatus.EXTRA -> Triple(ExtraBgLight, ExtraOrange, TextDecoration.None)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = diffWord.word,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = textColor,
            fontWeight = if (diffWord.status == WordStatus.MATCH) FontWeight.Medium else FontWeight.Bold,
            fontSize = 16.sp,
            textDecoration = decoration
        )
    }
}

@Composable
private fun DiffLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LegendItem(color = MatchGreen, label = "正確")
        LegendItem(color = MissingRed, label = "漏說")
        LegendItem(color = ExtraOrange, label = "多說")
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
