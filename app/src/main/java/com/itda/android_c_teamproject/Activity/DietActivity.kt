package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.itda.android_c_teamproject.databinding.ActivityDietBinding
import com.itda.android_c_teamproject.model.Diet.SharedViewModel

class DietActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDietBinding
    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 총 영양 정보를 관찰하여 UI 업데이트
        sharedViewModel.totalEnergy.observe(this, Observer { updateTotalNutritionalValues() })
        sharedViewModel.totalProtein.observe(this, Observer { updateTotalNutritionalValues() })
        sharedViewModel.totalFat.observe(this, Observer { updateTotalNutritionalValues() })
        sharedViewModel.totalCarbs.observe(this, Observer { updateTotalNutritionalValues() })

        binding.btnBreakfast.setOnClickListener {
            sharedViewModel.setMealType("breakfast")
            openMealActivity("breakfast")
        }
        binding.btnLunch.setOnClickListener {
            sharedViewModel.setMealType("lunch")
            openMealActivity("lunch")
        }
        binding.btnDinner.setOnClickListener {
            sharedViewModel.setMealType("dinner")
            openMealActivity("dinner")
        }
        binding.btnCompleteDiet.setOnClickListener {
            finish()
        }
    }

    private fun openMealActivity(mealType: String) {
        val intent = Intent(this, AddMealActivity::class.java)
        intent.putExtra("mealType", mealType)
        startActivity(intent)
    }

    private fun updateTotalNutritionalValues() {
        val totalEnergy = sharedViewModel.totalEnergy.value ?: 0f
        val totalProtein = sharedViewModel.totalProtein.value ?: 0f
        val totalFat = sharedViewModel.totalFat.value ?: 0f
        val totalCarbs = sharedViewModel.totalCarbs.value ?: 0f

        val totalNutritionalValues = """
            Total Energy: $totalEnergy kcal
            Total Protein: $totalProtein g
            Total Fat: $totalFat g
            Total Carbs: $totalCarbs g
        """.trimIndent()

        binding.tvTotalNutritionalValues.text = totalNutritionalValues
    }
}
