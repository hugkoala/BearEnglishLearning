package com.bear.englishlearning.ui.screens.memo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bear.englishlearning.data.local.entity.Memo
import com.bear.englishlearning.ui.theme.MatchGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoListScreen(
    onCreateMemo: () -> Unit,
    onReview: () -> Unit,
    viewModel: MemoViewModel = hiltViewModel()
) {
    val memos by viewModel.allMemos.collectAsStateWithLifecycle()
    val unreviewedCount = memos.count {
        !it.isReviewed && it.nextReviewAt <= System.currentTimeMillis()
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateMemo,
                icon = { Icon(Icons.Default.Add, "新增") },
                text = { Text("新增筆記") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "📒 備忘錄",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (unreviewedCount > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📖 有 $unreviewedCount 則筆記待複習",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "複習昨天的學習重點",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            FilledTonalButton(onClick = onReview) {
                                Text("去複習")
                            }
                        }
                    }
                }
            }

            if (memos.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📝", style = MaterialTheme.typography.headlineLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "還沒有筆記\n練習後來記錄心得吧！",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }

            items(memos, key = { it.memoId }) { memo ->
                MemoCard(
                    memo = memo,
                    onDelete = { viewModel.deleteMemo(memo.memoId) }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun MemoCard(memo: Memo, onDelete: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (memo.isReviewed) {
                        Icon(
                            Icons.Default.CheckCircle,
                            "已複習",
                            tint = MatchGreen,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("已複習", color = MatchGreen, style = MaterialTheme.typography.labelSmall)
                    } else if (memo.nextReviewAt <= System.currentTimeMillis()) {
                        Icon(
                            Icons.Default.Schedule,
                            "待複習",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("待複習", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Row {
                    Text(
                        text = dateFormat.format(Date(memo.createdAt)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "刪除", modifier = Modifier.padding(0.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = memo.content,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            memo.relatedScenarioTitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📍 $it",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
