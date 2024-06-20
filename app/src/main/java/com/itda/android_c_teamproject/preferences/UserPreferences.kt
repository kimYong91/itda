package com.itda.android_c_teamproject.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

// 사용자가 가입할 때 입력한 정보를 저장 하고 불러 오는 방법을 추가
object UserPreferences {

    private const val PREFS_NAME = "user_prefs"
    private const val KEY_GENDER = "gender"
    private const val KEY_HEIGHT = "height"
    private const val KEY_WEIGHT = "weight"
    private const val KEY_AGE = "age"
    private const val KEY_BMR = "bmr"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserInfo(
        context: Context,
        gender: String,
        height: Int,
        weight: Int,
        age: Int,
        bmr: Int
    ) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_GENDER, gender)
        editor.putInt(KEY_HEIGHT, height)
        editor.putInt(KEY_WEIGHT, weight)
        editor.putInt(KEY_AGE, age)
        editor.putInt(KEY_BMR, bmr)
        editor.apply()
    }

    fun getUserInfo(context: Context): Map<String, Any> {
        val prefs = getPreferences(context)
        val gender = prefs.getString(KEY_GENDER, "N/A") ?: "N/A"
        val height = prefs.getInt(KEY_HEIGHT, 0)
        val weight = prefs.getInt(KEY_WEIGHT, 0)
        val age = prefs.getInt(KEY_AGE, 0)
        val bmr = calculateBMR(
            mapOf(
                "gender" to gender,
                "height" to height,
                "weight" to weight,
                "age" to age
            )
        )

        // 로그 추가
        Log.d("UserInfo", "Gender: $gender, Height: $height, Weight: $weight, Age: $age, BMR: $bmr")


        // 저장된 정보를 포함한 사용자 정보를 반환
        return mapOf(
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "age" to age,
            "bmr" to bmr
        )
    }

    // 사용자 정보를 기반 으로 미리 정의된 프롬프트를 생성 하는 함수를 추가
    // BMR 계산 함수
    fun calculateBMR(userInfo: Map<String, Any>): Int {
        val gender = userInfo["gender"] as? String ?: "N/A"
        val height = userInfo["height"] as? Int ?: 0
        val weight = userInfo["weight"] as? Int ?: 0
        val age = userInfo["age"] as? Int ?: 0

        return when (gender.lowercase()) {
            "male", "남자" -> (88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)).toInt()
            "female", "여자" -> (447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)).toInt()
            "N/A", "n/a" -> 0
            else -> throw IllegalArgumentException("Invalid gender: $gender")
        }
    }

    fun createPrompt1(userInfo: Map<String, Any>): String {
        val gender = userInfo["gender"] as? String ?: "N/A"
        val height = userInfo["height"] as? Int ?: 0
        val weight = userInfo["weight"] as? Int ?: 0
        val age = userInfo["age"] as? Int ?: 0
        val bmr = userInfo["bmr"] as? Int ?: 0

        // 로그 추가
        Log.d(
            "CreatePrompt1",
            "Gender: $gender, Height: $height, Weight: $weight, Age: $age, BMR: $bmr"
        )

        return "성별은 $gender,키는 $height cm, 몸무게는 $weight kg, 나이는 $age, 기초대사량이 $bmr 인 사람이 주 4회 30분 정도 산보 정도의 세기로 달리기를 하면서 다이어트를 하려고 한다. 하루에 얼마나 달려야 할까?"
    }

    fun createPrompt2(userInfo: Map<String, Any>): String {
        // 필요한 경우 다른 내용을 작성
        return "Prompt 2: 예시 문구입니다."
    }

    fun createPrompt3(userInfo: Map<String, Any>): String {
        // 필요한 경우 다른 내용을 작성
        return "Prompt 3: 예시 문구입니다."
    }

    fun createPrompt4(userInfo: Map<String, Any>): String {
        // 필요한 경우 다른 내용을 작성
        return "Prompt 4: 예시 문구입니다."
    }
}
