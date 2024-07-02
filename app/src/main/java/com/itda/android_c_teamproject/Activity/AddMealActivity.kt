package com.itda.android_c_teamproject.Activity
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

private const val TAG = "AddMealActivity"
class AddMealActivity : AppCompatActivity(), MealAdapter.OnItemClickListener {
    private lateinit var mealDatabase: MealDatabase
    private lateinit var mealAdapter: MealAdapter
    private lateinit var binding: ActivityAddMealBinding
    private lateinit var foodAdapter: FoodAdapter
    private val sharedViewModel: SharedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

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

        sharedViewModel.meals.observe(this, Observer { meals ->
            Log.d(TAG, "Meals updated: $meals")
            mealAdapter.updateData(meals)
        })

        binding.addButton.setOnClickListener {
            Log.d(TAG, "Add button clicked")
            val intent = Intent(this, MealActivity::class.java)
            val selectedMealType = sharedViewModel.mealType.value
            if (selectedMealType != null) {
                intent.putExtra("mealType", selectedMealType)
                Log.d(TAG, "Launching MealActivity with mealType: $selectedMealType")
            } else {
                Log.e(TAG, "MealType is null, cannot launch MealActivity")
            }
            addMealLauncher.launch(intent)
        }

        val mealTypeFromIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getStringExtra("mealType") // 수정된 부분: 최신 메서드 사용
        } else {
            @Suppress("DEPRECATION")
            intent.getStringExtra("mealType")
        }
        if (mealTypeFromIntent != null) {
            sharedViewModel.setMealType(mealTypeFromIntent)
            loadMeals(mealTypeFromIntent)
        } else {
            Log.e(TAG, "Received null mealType from Intent")
        }

    }

    private fun saveMeals(meals: List<Meal>) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                meals.forEach {
                    Log.d(TAG, "Inserting meal: $it")
                    mealDatabase.mealDao().insert(it) }

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

    private fun loadMeals(mealType: String) {
        mealDatabase.mealDao().getMealsByType(mealType).observe(this, Observer { mealsList ->
            Log.d(TAG, "Loaded meals: $mealsList")
            sharedViewModel.setMeals(mealsList)
        })
    }

    override fun onItemClick(meal: Meal) {
        // 보기 기능 : 클릭된 식사 항목의 상세 정보를 새로운 액티비티에 전달
        val intent = Intent(this, MealDetailActivity::class.java)
        intent.putExtra("meal", meal)
        startActivity(intent)
        Log.d(TAG, "Viewing meal: $meal")
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

    // 길게 클릭하면 삭제 확인 다이얼로그 표시
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
