package com.itda.android_c_teamproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itda.android_c_teamproject.databinding.ItemMealBinding
import com.itda.android_c_teamproject.model.Meal



class MealAdapter(private val meals: MutableList<Meal>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {



    interface OnItemClickListener {
        fun onItemClick(meal: Meal)
        fun onItemLongClick(meal: Meal): Boolean
    }

    class MealViewHolder(private val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal, listener: OnItemClickListener) {
            binding.mealContent.text = meal.content
            itemView.setOnClickListener {
                listener.onItemClick(meal)
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClick(meal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position], listener)
    }

    override fun getItemCount() = meals.size

    fun updateData(newMeals: List<Meal>) {
        meals.clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }

    fun addMeal(meal: Meal) {
        meals.add(meal)
        notifyItemInserted(meals.size - 1)
    }
}
