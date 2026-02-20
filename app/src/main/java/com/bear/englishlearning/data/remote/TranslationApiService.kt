package com.bear.englishlearning.data.remote

import com.bear.englishlearning.data.remote.dto.TranslationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApiService {

    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langPair: String
    ): TranslationResponse

    companion object {
        const val BASE_URL = "https://api.mymemory.translated.net/"
    }
}
