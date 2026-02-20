package com.bear.englishlearning.data.repository

import com.bear.englishlearning.data.remote.TranslationApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationRepository @Inject constructor(
    private val api: TranslationApiService
) {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): Result<String> {
        return try {
            val langPair = "$sourceLang|$targetLang"
            val response = api.translate(text, langPair)
            val translated = response.responseData?.translatedText
            if (!translated.isNullOrBlank()) {
                Result.success(translated)
            } else {
                Result.failure(Exception("Translation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
