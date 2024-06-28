package com.itda.android_c_teamproject.preferences

import android.util.Log
import com.itda.android_c_teamproject.model.dto.UserDTO

object UserPreferences {

    //  사용자 정보를 받아서 프롬 프트 생성

    fun createPrompt1(
        userdto: UserDTO,
        selectedJob: String,
        health: String,
        exercisePreference: String,
        dailyFoodIntake: String,
        exerciseGoal: String,
        exerciseFacility: String,
        exerciseType: String,
        exerciseDurationTimeInput: String,
        exerciseDurationDayInput: String
    ): String {
        val userGender = userdto.userGender
        val userHeight = userdto.userHeight
        val userWeight = userdto.userWeight
        val userAge = userdto.userAge
        val basalMetabolism = userdto.basalMetabolism

        // 생성할 프롬 프트 내용
        val prompt = """
            나이 ${userAge}세, 키 ${userHeight}cm, 몸무게 ${userWeight}kg, 기초대사량 ${basalMetabolism}kcal에 성별은 ${userGender}이고,
            일은 ${selectedJob}일을 하는데 건강은 ${health}이고, 운동 취향은 ${exercisePreference}이고, 하루 ${dailyFoodIntake} 먹는데,
            ${exerciseGoal}을(를) 목표로 ${exerciseFacility}에서 ${exerciseType}을(를) 한주에 한 주에 ${exerciseDurationDayInput}일 운동하고, 하루에 ${exerciseDurationTimeInput}시간씩 운동 하려고해
            이 정보를 바탕으로 추천 운동과 운동 순서 하고 그 운동에 맞는 쉬는시간과 식단을 보기 편한 스케줄로 만들고 하루 하루 운동 소비 칼로리와 총 소비 칼로리도 같이 보여줘. 
	        그리고 글은 운동에 관련된 내용만 나오게 하고, 식단은 한국에서 쉽게 구할 수 있는 제료들만 추천해주고, 한글로 보여줘. 추가로 각 추천 운동의 세트당 소비 칼로리도 알려줘.
            """.trimIndent()

        Log.d("createPrompt1", "Generated prompt: $prompt")
        return prompt
    }
}
