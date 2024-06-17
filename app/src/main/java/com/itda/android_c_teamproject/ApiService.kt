package com.itda.android_c_teamproject

import com.itda.android_c_teamproject.model.LoginRequest
import com.itda.android_c_teamproject.model.LoginResponse
import com.itda.android_c_teamproject.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // 사용자 로그인 후 모든 정보 보기
    @GET("/itda/oneUserInfo")
    fun getUserInfo(): Call<User>

    // 사용자 로그인 후 건강 관련 정보 보기
    @GET("/itda/oneUserHealthDTO")
    fun getUserHealthInfo(): Call<User>

    // 회원 가입
    @POST("/itda/createUser")
    fun createUser(@Body user: User): Call<User>

    // 로그인
    @POST("/itda/auth")
    fun login(@Body user: LoginRequest): Call<LoginResponse>

    // 로그인 후 사용자 건강 관련 정보 업데이트
    @POST("/itda/oneUserHealthDTO/{id}")
    fun updateUserHealthInfo(@Header("Authorization") token: String, id: String, @Body user: User): Call<User>

    // 로그인 후 사용자 개인 정보 업데이트
    @POST("/itda/oneUserInfo/{id}")
    fun updateUserPersonalDTO(@Header("Authorization") token: String, id: String, @Body user: User): Call<User>

}