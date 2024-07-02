package com.itda.android_c_teamproject.Activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityMealDetailBinding
import com.itda.android_c_teamproject.model.Meal

private const val TAG = "MealDetailActivity"
class MealDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인챈트로부터 Meal 객체 수신
        val meal: Meal? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("meal", Meal::class.java) // 수정된 부분: 최신 메서드 사용
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("meal")
        }


        if (meal != null) {
            Log.d(TAG, "Displaying meal details: $meal")
            // Meal 객체의 데이터를 UI에 설정
            binding.mealType1.text = meal.mealType
            binding.mealContent1.text = meal.content
            binding.mealEnergy.text = "Energy: ${meal.energy} kcal"
            binding.mealProtein.text = "Protein: ${meal.protein} g"
            binding.mealFat.text = "Fat: ${meal.fat} g"
            binding.mealCarbs.text = "Carbohydrates: ${meal.carbs} g"
        } else {
            Log.e(TAG, "No meal data received")
            // meal 객체가 null 인 경우 처리
        }
    }
}