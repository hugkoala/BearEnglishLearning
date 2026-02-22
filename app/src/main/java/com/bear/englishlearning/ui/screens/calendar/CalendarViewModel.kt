package com.bear.englishlearning.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.englishlearning.data.local.entity.DailyProgress
import com.bear.englishlearning.data.repository.DailyProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val progress: DailyProgress? = null
) {
    val hasActivity: Boolean
        get() = progress != null && (
            progress.sentencesPracticed > 0 ||
            progress.vocabularyLearned > 0 ||
            progress.conversationTurns > 0 ||
            progress.listeningQuizzes > 0 ||
            progress.memosCreated > 0 ||
            progress.translationsCount > 0
        )

    val totalActivities: Int
        get() = (progress?.sentencesPracticed ?: 0) +
                (progress?.vocabularyLearned ?: 0) +
                (progress?.conversationTurns ?: 0) +
                (progress?.listeningQuizzes ?: 0) +
                (progress?.memosCreated ?: 0) +
                (progress?.translationsCount ?: 0)

    // 0 = none, 1 = light, 2 = medium, 3 = high
    val activityLevel: Int
        get() = when {
            totalActivities == 0 -> 0
            totalActivities < 5 -> 1
            totalActivities < 15 -> 2
            else -> 3
        }
}

data class CalendarScreenState(
    val currentMonth: YearMonth = YearMonth.now(),
    val days: List<CalendarDay> = emptyList(),
    val selectedDay: CalendarDay? = null,
    val totalActiveDays: Int = 0,
    val currentStreak: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val progressRepository: DailyProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarScreenState())
    val state: StateFlow<CalendarScreenState> = _state.asStateFlow()

    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE

    init {
        loadMonth(YearMonth.now())
        loadStats()
    }

    fun previousMonth() {
        val newMonth = _state.value.currentMonth.minusMonths(1)
        loadMonth(newMonth)
    }

    fun nextMonth() {
        val newMonth = _state.value.currentMonth.plusMonths(1)
        loadMonth(newMonth)
    }

    fun selectDay(day: CalendarDay) {
        _state.value = _state.value.copy(selectedDay = day)
    }

    fun clearSelection() {
        _state.value = _state.value.copy(selectedDay = null)
    }

    private fun loadStats() {
        viewModelScope.launch {
            val totalDays = progressRepository.getTotalActiveDays()
            val streak = calculateStreak()
            _state.value = _state.value.copy(
                totalActiveDays = totalDays,
                currentStreak = streak
            )
        }
    }

    private fun loadMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            _state.value = _state.value.copy(currentMonth = yearMonth, isLoading = true)

            val firstDay = yearMonth.atDay(1)
            val lastDay = yearMonth.atEndOfMonth()

            // Calculate calendar grid start (Monday of the week containing the 1st)
            val startDayOfWeek = firstDay.dayOfWeek.value // 1=Mon, 7=Sun
            val gridStart = firstDay.minusDays((startDayOfWeek - 1).toLong())

            // Calculate grid end (fill 6 weeks = 42 days)
            val gridEnd = gridStart.plusDays(41)

            // Fetch progress data for the visible range
            val startStr = gridStart.format(dateFormat)
            val endStr = gridEnd.format(dateFormat)
            val progressList = progressRepository.getProgressRange(startStr, endStr)
            val progressMap = progressList.associateBy { it.date }

            val today = LocalDate.now()
            val days = (0L..41L).map { offset ->
                val date = gridStart.plusDays(offset)
                CalendarDay(
                    date = date,
                    isCurrentMonth = date.month == yearMonth.month && date.year == yearMonth.year,
                    isToday = date == today,
                    progress = progressMap[date.format(dateFormat)]
                )
            }

            _state.value = _state.value.copy(
                days = days,
                isLoading = false,
                selectedDay = null
            )
        }
    }

    private suspend fun calculateStreak(): Int {
        val allProgress = progressRepository.getAllProgress()
        if (allProgress.isEmpty()) return 0

        val activeDates = allProgress
            .filter {
                it.sentencesPracticed > 0 || it.vocabularyLearned > 0 ||
                it.conversationTurns > 0 || it.listeningQuizzes > 0 ||
                it.memosCreated > 0 || it.translationsCount > 0
            }
            .map { LocalDate.parse(it.date, dateFormat) }
            .toSortedSet()

        if (activeDates.isEmpty()) return 0

        var streak = 0
        var checkDate = LocalDate.now()

        // If today hasn't been active yet, start from yesterday
        if (!activeDates.contains(checkDate)) {
            checkDate = checkDate.minusDays(1)
        }

        while (activeDates.contains(checkDate)) {
            streak++
            checkDate = checkDate.minusDays(1)
        }

        return streak
    }
}
