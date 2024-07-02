package com.itda.android_c_teamproject.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DietRetrofitClient {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "http://10.100.203.145:8080/"

    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
}