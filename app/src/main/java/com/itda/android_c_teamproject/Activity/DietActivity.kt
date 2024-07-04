package com.itda.android_c_teamproject.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.itda.android_c_teamproject.activity.AddMealActivity
import com.itda.android_c_teamproject.databinding.ActivityDietBinding
import com.itda.android_c_teamproject.model.Diet.SharedViewModel
import java.util.Calendar

class DietActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDietBinding
    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvSelectedDate: TextView = binding.tvSelectedDate
        val calendar = Calendar.getInstance()

        tvSelectedDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDate = calendar.time
                sharedViewModel.setSelectedDate(selectedDate)
                tvSelectedDate.text = "${selectedYear}년 ${selectedMonth + 1}월 ${selectedDay}일"
            }, year, month, day)

            datePickerDialog.show()
        }

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
