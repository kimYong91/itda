package com.itda.android_c_teamproject.model.dto

data class FoodDTO(
    val 식품코드: String?,
    val 식품명: String,
    val 에너지: Float,
    val 단백질: Float,
    val 지방: Float,
    val 탄수화물: Float,
    val 당류: Float,
    val 나트륨: Float
)