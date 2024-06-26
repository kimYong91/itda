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
            ${exerciseGoal}을(를) 목표로 ${exerciseFacility}에서 ${exerciseType}을(를) 하루에 ${exerciseDurationTimeInput}시간씩 하려고해
            이 정보를 바탕으로 무슨 운동을 하고 식단은 어떻게 할지를 적절한 쉬는시간도 같이 포함해서 ${exerciseDurationDayInput}동안의 운동 스케줄을 만들어 주고 
            하루 하루 운동 소비 칼로리 알려주고 총 칼로리 계산해 줘. 그리고 내가 말한 건강상태, 운동취향, 하루 식사량, 어떤 운동을 하는지, 운동 목표, 운동 장소, 운동 시간과 일수도 알려줘
            """.trimIndent()

        Log.d("createPrompt1", "Generated prompt: $prompt")
        return prompt
    }
}
