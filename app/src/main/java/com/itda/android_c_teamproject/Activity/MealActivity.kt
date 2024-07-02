package com.itda.android_c_teamproject.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.adapter.FoodAdapter
import com.itda.android_c_teamproject.databinding.ActivityMealBinding
import com.itda.android_c_teamproject.model.Diet.SharedViewModel
import com.itda.android_c_teamproject.model.Meal
import com.itda.android_c_teamproject.model.dto.FoodDTO
import com.itda.android_c_teamproject.network.ApiService
import com.itda.android_c_teamproject.network.DietRetrofitClient
import com.itda.android_c_teamproject.network.RetrofitClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MealActivity"

class MealActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityMealBinding
    private val selectedMeals = mutableListOf<Meal>()

    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        foodAdapter = FoodAdapter(emptyList()) { food ->
            addFood(food)
        }
        binding.recyclerView.adapter = foodAdapter

        apiService = DietRetrofitClient.retrofitInstance.create(ApiService::class.java)

        val currentMealType = intent.getStringExtra("mealType")
        if (currentMealType != null) {
            sharedViewModel.setMealType(currentMealType)
            Log.d(TAG, "Received mealType from Intent: $currentMealType")
        } else {
            Log.e(TAG, "MealType is null in Intent")
        }

        sharedViewModel.mealType.observe(this, Observer { mealType ->
            Log.d(TAG, "onCreate: mealType=$mealType")
            if (mealType != null) {
                loadFoods()
            }
        })


        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Search query submitted: $query")
                query?.let {
                    foodAdapter.filter.filter(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Search query text changed: $newText")
                foodAdapter.filter.filter(newText)
                return false
            }
        })
        findViewById<Button>(R.id.complete_button).setOnClickListener {
            Log.d(TAG, "Complete button clicked")
            completeSelection()
        }
        loadFoods()

    }

    private fun loadFoods() {
        lifecycleScope.launch {
            val foods = withContext(Dispatchers.IO) {
                try {
                    val response = apiService.getAllFoods().execute()
                    if (response.isSuccessful) {
                        val foodList = response.body() ?: emptyList()
                        Log.d(TAG, "Server response: $foodList")
                        foodList
                    } else {
                        Log.e(TAG, "Server response error: ${response.errorBody()?.string()}")
                        Log.e(TAG, "Server response error: ${response.code()}")
                        emptyList()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading foods: ${e.message}", e)
                    emptyList()
                }
            }
            Log.d(TAG, "Foods loaded: $foods")
            foodAdapter.updateData(foods)
        }
    }


    fun addFood(food: FoodDTO) {
        Toast.makeText(this, "${food.식품명} 추가됨", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Food added: $food")

        val mealType = sharedViewModel.mealType.value
        if (mealType != null) {
            val newMeal = Meal(
                mealType = mealType,
                content = food.식품명,
                energy = food.에너지,
                protein = food.단백질,
                fat = food.지방,
                carbs = food.탄수화물
            )
            selectedMeals.add(newMeal)
            sharedViewModel.addMeal(newMeal)
            Log.d(TAG, "New meal created: $newMeal")
        } else {
            Log.e(TAG, "Meal type is null, cannot create new meal")
        }
    }


    private fun completeSelection() {
        Log.d(TAG, "Completing selection with meals: $selectedMeals")
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra("selected_meals", ArrayList(selectedMeals))
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
