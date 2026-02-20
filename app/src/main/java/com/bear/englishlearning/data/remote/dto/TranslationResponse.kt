package com.bear.englishlearning.data.remote.dto

import com.squareup.moshi.Json

data class TranslationResponse(
    @Json(name = "responseData") val responseData: ResponseData?,
    @Json(name = "responseStatus") val responseStatus: Int? = null,
    @Json(name = "matches") val matches: List<TranslationMatch>? = null
)

data class ResponseData(
    @Json(name = "translatedText") val translatedText: String = "",
    @Json(name = "match") val match: Double? = null
)

data class TranslationMatch(
    @Json(name = "segment") val segment: String = "",
    @Json(name = "translation") val translation: String = "",
    @Json(name = "source") val source: String = "",
    @Json(name = "target") val target: String = "",
    @Json(name = "quality") val quality: String = "",
    @Json(name = "match") val match: Double = 0.0
)
