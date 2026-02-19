package com.bear.englishlearning.domain.vocabulary

data class VocabularyWord(
    val word: String,
    val partOfSpeech: String,
    val phonetic: String,
    val meaningEn: String,
    val meaningZh: String,
    val exampleSentence: String,
    val exampleZh: String,
    val category: String
)
