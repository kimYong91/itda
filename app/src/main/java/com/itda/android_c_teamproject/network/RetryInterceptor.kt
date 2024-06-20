package com.itda.android_c_teamproject.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

// 네트워크 상태가 불안정할 때 재시도 메커니즘을 추가하여 요청을 재시도
class RetryInterceptor (private val maxRetry: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        var exception: IOException? = null
        var retryCount = 0

        while (retryCount < maxRetry) {
            try {
                response = chain.proceed(chain.request())
                return response
            } catch (e: IOException) {
                exception = e
                retryCount++
                // 로그 출력
                println("Request failed - Retry $retryCount")
            }
        }
        throw exception ?: IOException("Unknown error")
    }
}