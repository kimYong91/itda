package com.itda.android_c_teamproject.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itda.android_c_teamproject.Activity.MealActivity
import com.itda.android_c_teamproject.Activity.MealDetailActivity
import com.itda.android_c_teamproject.adapter.FoodAdapter
import com.itda.android_c_teamproject.adapter.MealAdapter
import com.itda.android_c_teamproject.databinding.ActivityAddMealBinding
import com.itda.android_c_teamproject.model.Diet.MealDatabase
import com.itda.android_c_teamproject.model.Diet.SharedViewModel
import com.itda.android_c_teamproject.model.Meal
import com.itda.android_c_teamproject.model.dto.FoodDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "AddMealActivity"

class AddMealActivity : AppCompatActivity(), MealAdapter.OnItemClickListener {
    private lateinit var mealDatabase: MealDatabase
    private lateinit var mealAdapter: MealAdapter
    private lateinit var binding: ActivityAddMealBinding
    private lateinit var foodAdapter: FoodAdapter
    private val sharedViewModel: SharedViewModel by viewModels()

    private val selectedMeals = mutableListOf<Meal>()

    private val addMealLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newMeals = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableArrayListExtra("selected_meals", Meal::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableArrayListExtra<Meal>("selected_meals")
            }
            newMeals?.let {
                Log.d(TAG, "New meals received: $it")
                saveMeals(it)
                it.forEach { meal ->
                    sharedViewModel.addMeal(meal)
                    mealAdapter.addMeal(meal)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealDatabase = MealDatabase.getDatabase(this)

        mealAdapter = MealAdapter(mutableListOf(), this)
        foodAdapter = FoodAdapter(emptyList()) { food ->
            addFood(food)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mealAdapter

        sharedViewModel.meals.observe(this) { meals ->
            Log.d(TAG, "Meals updated: $meals")
            mealAdapter.updateData(meals)
        }

        binding.addButton.setOnClickListener {
            Log.d(TAG, "Add button clicked")
            val intent = Intent(this, MealActivity::class.java)
            sharedViewModel.mealType.value?.let { selectedMealType ->
                intent.putExtra("mealType", selectedMealType)
                Log.d(TAG, "Launching MealActivity with mealType: $selectedMealType")
                addMealLauncher.launch(intent)
            } ?: Log.e(TAG, "MealType is null, cannot launch MealActivity")
        }

        // 인텐트에서 mealType과 date를 가져와서 ViewModel에 설정
        val mealTypeFromIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getStringExtra("mealType")
        } else {
            @Suppress("DEPRECATION")
            intent.getStringExtra("mealType")
        }

        val dateFromIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("date", Date::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("date") as? Date
        } ?: Date()

        if (mealTypeFromIntent != null) {
            sharedViewModel.setMealType(mealTypeFromIntent)
            sharedViewModel.setSelectedDate(dateFromIntent)
            loadMeals(dateFromIntent, mealTypeFromIntent)
        } else {
            Log.e(TAG, "Received null mealType from Intent")
        }
    }

    private fun saveMeals(meals: List<Meal>) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                meals.forEach {
                    Log.d(TAG, "Inserting meal: $it")
                    mealDatabase.mealDao().insert(it)
                }

                withContext(Dispatchers.Main) {
                    meals.forEach { meal ->
                        sharedViewModel.addMeal(meal)
                        mealAdapter.addMeal(meal)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting meal: ${e.message}", e)
            }
        }
    }

    private fun loadMeals(date: Date, mealType: String) {
        mealDatabase.mealDao().getMealsByDateAndType(date, mealType).observe(this) { mealsList ->
            Log.d(TAG, "Loaded meals: $mealsList")
            sharedViewModel.setMeals(mealsList)
        }
    }

    override fun onItemClick(meal: Meal) {
        val intent = Intent(this, MealDetailActivity::class.java).apply {
            putExtra("meal", meal)
        }
        startActivity(intent)
        Log.d(TAG, "Viewing meal: $meal")
    }

    fun addFood(food: FoodDTO) {
        Toast.makeText(this, "${food.식품명} 추가됨", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Food added: $food")

        sharedViewModel.mealType.value?.let { mealType ->
            val newMeal = Meal(
                mealType = mealType,
                content = food.식품명,
                energy = food.에너지,
                protein = food.단백질,
                fat = food.지방,
                carbs = food.탄수화물,
                date = sharedViewModel.selectedDate.value ?: Date()
            )
            selectedMeals.add(newMeal)
            sharedViewModel.addMeal(newMeal)
            Log.d(TAG, "New meal created: $newMeal")
        } ?: Log.e(TAG, "Meal type is null, cannot create new meal")
    }

    override fun onItemLongClick(meal: Meal): Boolean {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("이 식사를 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        mealDatabase.mealDao().delete(meal)
                        withContext(Dispatchers.Main) {
                            sharedViewModel.setMeals(sharedViewModel.meals.value?.filter { it.id != meal.id } ?: listOf())
                            mealAdapter.updateData(sharedViewModel.meals.value ?: listOf())
                            Log.d(TAG, "Meal deleted: $meal")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting meal: ${e.message}", e)
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
        return true
    }
}
