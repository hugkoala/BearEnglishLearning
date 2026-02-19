package com.bear.englishlearning.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.Memo
import com.bear.englishlearning.data.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {

    private val _memosToReview = MutableStateFlow<List<Memo>>(emptyList())
    val memosToReview: StateFlow<List<Memo>> = _memosToReview.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMemosToReview()
    }

    private fun loadMemosToReview() {
        viewModelScope.launch {
            _isLoading.value = true
            val memos = memoRepository.getMemosToReview(System.currentTimeMillis())
            _memosToReview.value = memos
            _isLoading.value = false
        }
    }

    fun markAsReviewed(memoId: Long) {
        viewModelScope.launch {
            memoRepository.markAsReviewed(memoId)
            _memosToReview.value = _memosToReview.value.filter { it.memoId != memoId }
        }
    }

    fun markAllAsReviewed() {
        viewModelScope.launch {
            _memosToReview.value.forEach { memo ->
                memoRepository.markAsReviewed(memo.memoId)
            }
            _memosToReview.value = emptyList()
        }
    }
}
