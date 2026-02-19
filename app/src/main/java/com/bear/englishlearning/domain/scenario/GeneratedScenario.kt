package com.bear.englishlearning.domain.scenario

/**
 * A generated scenario with its sentences, not tied to Room DB.
 */
data class GeneratedScenario(
    val title: String,
    val titleZh: String,
    val category: String,
    val sentences: List<GeneratedSentence>
)

data class GeneratedSentence(
    val englishText: String,
    val chineseText: String,
    val pronunciationTip: String
)
