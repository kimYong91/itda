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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DietActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDietBinding
    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvSelectedDate: TextView = binding.tvSelectedDate
        val calendar = Calendar.getInstance()

        // 현재 날짜를 텍스트 뷰에 설정
        tvSelectedDate.text = dateFormat.format(calendar.time)

        // 텍스브뷰 클릭 시 DatePickerDialog를 사용하여 날짜 선택
        tvSelectedDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDate = calendar.time
                sharedViewModel.setSelectedDate(selectedDate)
                tvSelectedDate.text = dateFormat.format(selectedDate)
                sharedViewModel.loadTotalNutritionalValues(selectedDate) // 전체 날짜의 영양 정보를 로드합니다.
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
        sharedViewModel.setMealType(mealType) // mealType 설정
        val intent = Intent(this, AddMealActivity::class.java)
        intent.putExtra("mealType", mealType)
        intent.putExtra("date", sharedViewModel.selectedDate.value)
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
