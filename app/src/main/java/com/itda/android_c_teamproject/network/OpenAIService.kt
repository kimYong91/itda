package com.itda.android_c_teamproject.network

import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API 인터페이스 정의 (OpenAIService.kt)
interface OpenAIService {
    //    val apiKey: String
    //        get() = BuildConfig.api_key

    // @Headers("Authorization: Bearer ${BuildConfig.api_key}")
    @POST("v1/chat/completions")

    fun getChatResponse(@Body request: ChatRequest): Call<ChatResponse>
}

// Retrofit 인스턴스 생성
// val openAIService: OpenAIService = NetworkModule.retrofit.create(OpenAIService::class.java)
