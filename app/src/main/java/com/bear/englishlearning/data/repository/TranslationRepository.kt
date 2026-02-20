package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.remote.TranslationApiService
import com.bear.englishlearning.data.remote.dto.TranslationMatch
import javax.inject.Inject
import javax.inject.Singleton

data class TranslationResult(
    val translatedText: String,
    val alternativeMeanings: List<String>,
    val exampleSentences: List<ExampleSentence>
)

data class ExampleSentence(
    val source: String,
    val translation: String
)

@Singleton
class TranslationRepository @Inject constructor(
    private val api: TranslationApiService
) {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): Result<TranslationResult> {
        return try {
            val langPair = "$sourceLang|$targetLang"
            val response = api.translate(text, langPair)
            val translated = response.responseData?.translatedText
            if (!translated.isNullOrBlank()) {
                val matches = response.matches ?: emptyList()

                // Extract alternative meanings: unique translations different from main result
                val alternativeMeanings = matches
                    .map { it.translation.trim() }
                    .filter { it.isNotBlank() && !it.equals(translated.trim(), ignoreCase = true) }
                    .distinct()
                    .take(5)

                // Extract example sentences: matches where segment differs from input (longer context)
                val inputLower = text.trim().lowercase()
                val exampleSentences = matches
                    .filter {
                        val seg = it.segment.trim().lowercase()
                        seg != inputLower && seg.length > inputLower.length && it.translation.isNotBlank()
                    }
                    .map { ExampleSentence(source = it.segment.trim(), translation = it.translation.trim()) }
                    .distinctBy { it.source.lowercase() }
                    .take(3)

                Result.success(
                    TranslationResult(
                        translatedText = translated,
                        alternativeMeanings = alternativeMeanings,
                        exampleSentences = exampleSentences
                    )
                )
            } else {
                Result.failure(Exception("Translation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
