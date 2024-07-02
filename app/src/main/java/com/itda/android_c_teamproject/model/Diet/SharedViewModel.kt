package com.itda.android_c_teamproject.model.Diet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itda.android_c_teamproject.model.Meal

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val mealDao: MealDao = MealDatabase.getDatabase(application).mealDao()

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
        loadMealsFromDatabase() // 데이터베이스에서 초기 데이터를 로드합니다.
    }

    fun setMealType(mealType: String) {
        _mealType.value = mealType
    }
    fun setMeals(mealList: List<Meal>) {
        _meals.value = mealList.toMutableList()
        updateTotalNutritionalValues()
    }


    fun addMeal(meal: Meal) {
        val currentList = _meals.value ?: mutableListOf()
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

    private fun loadMealsFromDatabase() {
        mealDao.getAllMeals().observeForever { mealList ->
            _meals.value = mealList.toMutableList()
            updateTotalNutritionalValues()
        }
    }

}
