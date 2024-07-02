package com.itda.android_c_teamproject.network

import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.LoginRequest
import com.itda.android_c_teamproject.model.Response.ChatResponse
import com.itda.android_c_teamproject.model.Response.LoginResponse
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.dto.UserDTO
import com.itda.android_c_teamproject.model.dto.UserFindNameDTO
import com.itda.android_c_teamproject.model.Response.UserFindNameResponse
import com.itda.android_c_teamproject.model.dto.UserFindPasswordDTO
import com.itda.android_c_teamproject.model.Response.UserFindPasswordResponse
import com.itda.android_c_teamproject.model.dto.UserHealthDTO
import com.itda.android_c_teamproject.model.dto.UserUsedNameDTO
import com.itda.android_c_teamproject.model.dto.UserPersonalDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 로그인 후 모든 정보 보기
    @GET("/itda/oneUserInfo")
    fun getUserInfo(@Header("Authorization") token: String, @Query("id") id: String): Call<User>

    // 로그인 후 건강 관련 정보 보기
    @GET("/itda/oneUserHealthDTO")
    fun getUserHealthInfo(@Header("Authorization") token: String, @Query("id") id: String): Call<UserDTO>

    @POST("/path/to/chat/api") // 실제 엔드포인트로 변경
    fun getChatResponse(@Body request: ChatRequest): Call<ChatResponse>

    // 회원 가입
    @POST("/itda/createUser")
    fun createUser(@Body user: User): Call<User>

    // 로그인
    @POST("/itda/auth")
    fun login(@Body user: LoginRequest): Call<LoginResponse>

    // 로그인 후 사용자 건강 관련 정보 업데이트
    @PATCH("/itda/oneUserHealthDTO/{id}")
    fun updateUserHealthInfo(@Header("Authorization") token: String, @Path("id") id: String, @Body userHealthDTO: UserHealthDTO): Call<UserHealthDTO>

    // 로그인 후 사용자 개인 정보 업데이트
    @PATCH("/itda/oneUserInfo/{id}")
    fun updateUserPersonalInfo(@Header("Authorization") token: String, @Path("id") id: String, @Body userPersonalDTO: UserPersonalDTO): Call<UserPersonalDTO>

    // 아이디 입력시 기존 사용자와 아이디 같은지 검사
    @GET("/itda/oneUsername")
    fun UserUsedName(@Query("id") id: String): Call<UserUsedNameDTO>

    // 비밀번호 임시 생성
    @POST("/itda/findPassword")
    fun findUserPassword(@Body userFindPasswordDTO: UserFindPasswordDTO): Call<UserFindPasswordResponse>

    // 아이디 찾기
    @POST("/itda/findUsername")
    fun findUsername(@Body userFindNameDTO: UserFindNameDTO): Call<UserFindNameResponse>

    // 아이디 삭제
    @DELETE("/itda/userDelete")
    fun userDelete(@Header("Authorization") token: String, @Query("username") username: String): Call<Void>



}