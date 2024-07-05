package com.itda.android_c_teamproject.model.Diet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itda.android_c_teamproject.model.Meal
import java.util.Calendar
import java.util.Date

private const val TAG = "SharedViewModel"

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val mealDao: MealDao = MealDatabase.getDatabase(application).mealDao()

    private val _selectedDate = MutableLiveData<Date>(Date())
    val selectedDate: LiveData<Date> get() = _selectedDate

    private val _mealType = MutableLiveData<String>()
    val mealType: LiveData<String> get() = _mealType

    private val _meals = MutableLiveData<MutableList<Meal>>()
    val meals: LiveData<MutableList<Meal>> get() = _meals

    private val _totalEnergy = MutableLiveData<Float>(0f)
    val totalEnergy: LiveData<Float> get() = _totalEnergy

    private val _totalProtein = MutableLiveData<Float>(0f)
    val totalProtein: LiveData<Float> get() = _totalProtein

    private val _totalFat = MutableLiveData<Float>(0f)
    val totalFat: LiveData<Float> get() = _totalFat

    private val _totalCarbs = MutableLiveData<Float>(0f)
    val totalCarbs: LiveData<Float> get() = _totalCarbs

    init {
        // 초기화를 위한 코드 추가
        _selectedDate.observeForever { date ->
            loadTotalNutritionalValues(date)
        }
        _mealType.observeForever { mealType ->
            _selectedDate.value?.let { date ->
                loadMealsByDateAndType(date, mealType)
            }
        }
    }

    fun setMealType(mealType: String) {
        _mealType.value = mealType
    }

    fun setMeals(mealList: List<Meal>) {
        _meals.value = mealList.toMutableList()
        updateTotalNutritionalValues()
    }

    fun addMeal(meal: Meal) {
        val currentList = _meals.value?.toMutableList() ?: mutableListOf()
        currentList.add(meal)
        _meals.value = currentList
        updateTotalNutritionalValues()
    }

    private fun updateTotalNutritionalValues() {
        val currentList = _meals.value ?: return

        var totalEnergy = 0f
        var totalProtein = 0f
        var totalFat = 0f
        var totalCarbs = 0f

        for (meal in currentList) {
            totalEnergy += meal.energy
            totalProtein += meal.protein
            totalFat += meal.fat
            totalCarbs += meal.carbs
        }

        _totalEnergy.value = totalEnergy
        _totalProtein.value = totalProtein
        _totalFat.value = totalFat
        _totalCarbs.value = totalCarbs
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
        loadTotalNutritionalValues(date)
    }

    fun loadMealsByDateAndType(date: Date, mealType: String) {
        mealDao.getMealsByDateAndType(date, mealType).observeForever { mealList ->
            Log.d(TAG, "Loaded meals: $mealList")
            _meals.value = mealList.toMutableList()
        }
    }

    fun loadTotalNutritionalValues(date: Date) {
        val startOfDay = date.toStartOfDay()
        val endOfDay = date.toEndOfDay()

        mealDao.getMealsByDateRange(startOfDay, endOfDay).observeForever { mealList ->
            Log.d(TAG, "Loaded all meals for the day: $mealList")
            _meals.value = mealList.toMutableList()
            updateTotalNutritionalValues()
        }
    }
}

// Extension functions for Date to get start and end of day
fun Date.toStartOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.toEndOfDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.time
}
