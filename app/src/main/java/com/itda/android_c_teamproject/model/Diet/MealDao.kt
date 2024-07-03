package com.itda.android_c_teamproject.model.Diet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itda.android_c_teamproject.model.Meal
import java.util.Date

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal): Long

    @Query("SELECT * FROM meal_table_A WHERE mealType = :mealType")
    fun getMealsByType(mealType: String): LiveData<List<Meal>>

    @Query("SELECT * FROM meal_table_A")
    fun getAllMeals(): LiveData<List<Meal>> // LiveData를 반환하도록 수정

    @Query("SELECT * FROM meal_table_A WHERE date = :date")
    fun getMealsByDate(date: Date): LiveData<List<Meal>>

    @Delete
    suspend fun delete(meal: Meal)
}
