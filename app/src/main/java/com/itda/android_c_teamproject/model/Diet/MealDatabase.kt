package com.itda.android_c_teamproject.model.Diet


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.itda.android_c_teamproject.model.Meal

@Database(entities = [Meal::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        fun getDatabase(context: Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "DietManage.db"
                ).fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
