package com.bear.englishlearning.ui.screens.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.Memo
import com.bear.englishlearning.data.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {

    val allMemos: StateFlow<List<Memo>> = memoRepository.getAllMemos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun saveMemo(content: String, scenarioTitle: String? = null) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val tomorrow9am = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0)
            ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val memo = Memo(
                content = content,
                relatedScenarioTitle = scenarioTitle,
                createdAt = now,
                nextReviewAt = tomorrow9am
            )
            memoRepository.insertMemo(memo)
            _saveSuccess.value = true
        }
    }

    fun markAsReviewed(memoId: Long) {
        viewModelScope.launch {
            memoRepository.markAsReviewed(memoId)
        }
    }

    fun deleteMemo(memoId: Long) {
        viewModelScope.launch {
            memoRepository.deleteMemo(memoId)
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun getUnreviewedCount(): Int {
        return allMemos.value.count { !it.isReviewed && it.nextReviewAt <= System.currentTimeMillis() }
    }
}
