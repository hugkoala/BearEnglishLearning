package com.bear.englishlearning.domain.model

enum class WordStatus {
    MATCH,
    MISSING,
    EXTRA
}

data class DiffWord(
    val word: String,
    val status: WordStatus
)

data class SpeechDiffResult(
    val diffWords: List<DiffWord>,
    val matchedCount: Int,
    val targetWordCount: Int,
    val spokenWordCount: Int,
    val accuracy: Float
) {
    val accuracyPercent: String
        get() = "${(accuracy * 100).toInt()}%"
}
