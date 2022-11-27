package com.example.deliveryfood.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryfood.databinding.FoodCardBinding
import com.example.deliveryfood.main.model.FoodModel
import com.squareup.picasso.Picasso

class FoodAdapter(
    private val context: Context,
    private var foods: List<FoodModel>
): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(val binding: FoodCardBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodCardBinding.inflate(LayoutInflater.from(context /* If error, the right way is: parent.context*/), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        with(holder){
            Picasso.get().load(food.image).into(binding.foodImage) //Image
            binding.foodName.text = food.name
            binding.foodDescription.text = food.description
            binding.foodPrice.text = food.price.toString()
            // Miss -> Rating (see documentation)
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }
}