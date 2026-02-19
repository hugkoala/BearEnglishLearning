package com.bear.englishlearning.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.preferences.AppPreferences
import com.bear.englishlearning.data.preferences.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    val difficulty: StateFlow<Difficulty> = appPreferences.difficulty
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Difficulty.BEGINNER)

    val dailyTaskCount: StateFlow<Int> = appPreferences.dailyTaskCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5)

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            appPreferences.setDifficulty(difficulty)
        }
    }

    fun setDailyTaskCount(count: Int) {
        viewModelScope.launch {
            appPreferences.setDailyTaskCount(count)
        }
    }
}
