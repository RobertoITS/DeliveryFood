package com.example.deliveryfood.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryfood.databinding.FoodCategoryBinding
import com.example.deliveryfood.main.model.CategoryModel

class CategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = FoodCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        with(holder){
            binding.categoryName.text = category.name
            binding.recyclerView.adapter = FoodAdapter(binding.root.context, category.listOfFood)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(val binding: FoodCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}