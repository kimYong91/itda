package com.itda.android_c_teamproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.itda.android_c_teamproject.databinding.ItemFoodBinding
import com.itda.android_c_teamproject.model.dto.FoodDTO

class FoodAdapter(private var foodList: List<FoodDTO>, private val addFood: (FoodDTO) -> Unit) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(), Filterable {

    private var filteredFoodList: List<FoodDTO> = foodList

    class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodDTO, addFood: (FoodDTO) -> Unit) {
            binding.foodName.text = food.식품명
            binding.foodEnergy.text = "에너지: ${food.에너지} kcal"
            binding.foodProtein.text = "단백질: ${food.단백질} g"
            binding.foodFat.text = "지방: ${food.지방} g"
            binding.foodCarbs.text = "탄수화물: ${food.탄수화물} g"

            binding.addButton.setOnClickListener {
                addFood(food)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(filteredFoodList[position], addFood)
    }

    override fun getItemCount() = filteredFoodList.size

    fun updateData(newFoodList: List<FoodDTO>) {
        foodList = newFoodList
        filteredFoodList = newFoodList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredFoodList = if (charString.isEmpty()) {
                    foodList
                } else {
                    foodList.filter {
                        it.식품명.contains(charString, true)
                    }
                }
                return FilterResults().apply { values = filteredFoodList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFoodList = if (results?.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    results.values as List<FoodDTO>
                } else {
                    emptyList()
                }
                notifyDataSetChanged()
            }
        }
    }
}
