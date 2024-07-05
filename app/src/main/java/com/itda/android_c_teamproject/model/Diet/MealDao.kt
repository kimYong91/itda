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
    @Query("SELECT * FROM DietFood WHERE strftime('%Y-%m-%d', date / 1000, 'unixepoch') = strftime('%Y-%m-%d', :date / 1000, 'unixepoch') " +
            "AND mealType = :mealType")
    fun getMealsByDateAndType(date: Date, mealType: String): LiveData<List<Meal>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Meal): Long

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM DietFood")
    fun getAllMeals(): LiveData<List<Meal>> // LiveData를 반환하도록 수정
}
