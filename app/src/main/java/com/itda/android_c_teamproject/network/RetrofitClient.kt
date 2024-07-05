package com.itda.android_c_teamproject.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private val BASE_URL = "http://13.209.37.105:8080"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())// Scalars 변환기(기본타입) 설정
            .build()
            .create(ApiService::class.java)
    }
}