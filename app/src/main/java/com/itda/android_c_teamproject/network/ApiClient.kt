package com.itda.android_c_teamproject.network

import com.itda.android_c_teamproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Retrofit 인스턴스 설정 (ApiClient.kt)
object ApiClient {
    private const val BASE_URL = "https://api.openai.com/"

    // 타임 아웃 시간 설정을 크게 늘리기
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 타임 아웃 시간 설정
    private val client = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor())
        .addInterceptor(RetryInterceptor(3)) // 5회 재시도 설정
        .addInterceptor(loggingInterceptor)
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// AuthInterceptor 를 사용 하여 API 키를 헤더에 포함 시키는 방법
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()
        return chain.proceed(request)
    }
}