package com.bear.englishlearning.domain.speech

import com.bear.englishlearning.domain.model.DiffWord
import com.bear.englishlearning.domain.model.SpeechDiffResult
import com.bear.englishlearning.domain.model.WordStatus

object WordDiffEngine {

    fun compare(targetSentence: String, spokenText: String): SpeechDiffResult {
        val targetWords = TextNormalizer.normalize(targetSentence)
        val spokenWords = TextNormalizer.normalize(spokenText)

        if (targetWords.isEmpty() && spokenWords.isEmpty()) {
            return SpeechDiffResult(emptyList(), 0, 0, 0, 1.0f)
        }

        if (targetWords.isEmpty()) {
            return SpeechDiffResult(
                diffWords = spokenWords.map { DiffWord(it, WordStatus.EXTRA) },
                matchedCount = 0, targetWordCount = 0,
                spokenWordCount = spokenWords.size, accuracy = 0.0f
            )
        }

        if (spokenWords.isEmpty()) {
            return SpeechDiffResult(
                diffWords = targetWords.map { DiffWord(it, WordStatus.MISSING) },
                matchedCount = 0, targetWordCount = targetWords.size,
                spokenWordCount = 0, accuracy = 0.0f
            )
        }

        val lcsTable = buildLcsTable(targetWords, spokenWords)
        val diffWords = backtrackDiff(targetWords, spokenWords, lcsTable)
        val matchedCount = diffWords.count { it.status == WordStatus.MATCH }

        return SpeechDiffResult(
            diffWords = diffWords,
            matchedCount = matchedCount,
            targetWordCount = targetWords.size,
            spokenWordCount = spokenWords.size,
            accuracy = matchedCount.toFloat() / targetWords.size.toFloat()
        )
    }

    private fun buildLcsTable(target: List<String>, spoken: List<String>): Array<IntArray> {
        val m = target.size
        val n = spoken.size
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 1..m) {
            for (j in 1..n) {
                dp[i][j] = if (target[i - 1] == spoken[j - 1]) {
                    dp[i - 1][j - 1] + 1
                } else {
                    maxOf(dp[i - 1][j], dp[i][j - 1])
                }
            }
        }
        return dp
    }

    private fun backtrackDiff(
        target: List<String>,
        spoken: List<String>,
        dp: Array<IntArray>
    ): List<DiffWord> {
        val result = mutableListOf<DiffWord>()
        var i = target.size
        var j = spoken.size

        while (i > 0 || j > 0) {
            when {
                i > 0 && j > 0 && target[i - 1] == spoken[j - 1] -> {
                    result.add(DiffWord(target[i - 1], WordStatus.MATCH))
                    i--; j--
                }
                j > 0 && (i == 0 || dp[i][j - 1] >= dp[i - 1][j]) -> {
                    result.add(DiffWord(spoken[j - 1], WordStatus.EXTRA))
                    j--
                }
                else -> {
                    result.add(DiffWord(target[i - 1], WordStatus.MISSING))
                    i--
                }
            }
        }

        return result.reversed()
    }
}
