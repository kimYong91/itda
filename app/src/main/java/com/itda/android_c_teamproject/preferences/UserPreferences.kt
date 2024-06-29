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
            이 정보를 바탕으로 무슨 요일에 쉬고 무슨 요일에 어떤 추천 운동을 할지, 운동 순서와 그 운동에 맞는 쉬는시간, 그리고 그 운동의 소비 칼로리와 그날 총 운동 칼로리를 정리해서 식단도 같이 스케줄에 넣어 만들어줘. 
            예를 들어 
            [무슨 요일 -무슨 부위]
            1. 무슨 운동(무슨 부위): 몇 세트 x 몇 회
            세트 사이 쉬는 사간: 몇 분
            세트당 소비 칼로리: 몇 칼로리
            2. 무슨 운동(무슨 부위): 몇 세트 x 몇 회
            세트 사이 쉬는 사간: 몇 분
            세트당 소비 칼로리: 몇 칼로리
            3. ....
            4. ....
            ....
            오늘 하루 총 소비 칼로리: 몇 칼로리
            
            아침 식단
            1. 무슨 음식 몇 개: 개당 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            점심 식단
            1. 무슨 음식 몇 개: 개당 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            저녁 식단
            1. 무슨 음식 몇 개: 개당 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            운동 식단 (아침, 점심, 저녁 대체)
            1. 무슨 음식 몇 개: 개당 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            
            [무슨 요일 -무슨 부위-]
            1. 무슨 운동(무슨 부위): 몇 세트 x 몇 회
            세트 사이 쉬는 사간: 몇 분
            세트당 소비 칼로리: 몇 칼로리
            2. 무슨 운동(무슨 부위): 몇 세트 x 몇 회
            세트 사이 쉬는 사간: 몇 분
            세트당 소비 칼로리: 몇 칼로리
            3. ....
            4. ....
            ....
            오늘 하루 총 소비 칼로리: 몇 칼로리
            
            아침 식단
            1. 무슨 음식: 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            점심 식단
            1. 무슨 음식: 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            저녁 식단
            1. 무슨 음식: 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            운동 식단 (아침, 점심, 저녁 대체)
            1. 무슨 음식: 음식 칼로리
            2. .....
            ....
            총 섭취 칼로리
            그리고 다른 사족들은 다 빼고, 식단은 한국에서 쉽게 구할 수 있는 제료들만 추천해주고, 한글로 보여줘. 추가로 각 추천 운동의 세트당 소비 칼로리도 알려줘.
            """.trimIndent()

        Log.d("createPrompt1", "Generated prompt: $prompt")
        return prompt
    }
}
