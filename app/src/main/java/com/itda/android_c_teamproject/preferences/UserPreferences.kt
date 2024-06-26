package com.itda.android_c_teamproject.preferences

import android.util.Log
import com.itda.android_c_teamproject.model.dto.UserDTO

object UserPreferences {

    //  사용자 정보를 받아서 프롬 프트 생성

    fun createPrompt1(userdto: UserDTO, exerciseType: String, exerciseDurationTime: String, exerciseDurationDay: String): String {
        val username = userdto.username
        val userGender = userdto.userGender
        val userHeight = userdto.userHeight
        val userWeight = userdto.userWeight
        val userAge = userdto.userAge
        val basalMetabolism = userdto.basalMetabolism

        // 생성할 프롬 프트 내용
        val prompt = """
            이름은 $username 님,
            성별은 $userGender, 키는 $userHeight cm, 몸무게는 $userWeight kg, 나이는 $userAge 세, 기초대사량은 $basalMetabolism kcal 입니다.
            하고자 하는 운동 종류는 $exerciseType 이고, 운동 시간은 $exerciseDurationTime 입니다.
            이 정보를 바탕으로 $exerciseDurationDay 동안의 운동 스케줄을 만들어 주세요.
        """.trimIndent()

        Log.d("createPrompt1", "Generated prompt: $prompt")
        return prompt
    }
}
