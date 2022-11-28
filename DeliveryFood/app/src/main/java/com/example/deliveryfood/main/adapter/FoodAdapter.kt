package com.example.deliveryfood.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryfood.databinding.FoodCardBinding
import com.example.deliveryfood.main.model.FoodModel
import com.squareup.picasso.Picasso

class FoodAdapter(
    private val context: Context,
    private var foods: List<FoodModel>
): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    /**Creamos la funcion del checkBoxListener*/
    lateinit var mChecked: OnItemCheckListener
    interface OnItemCheckListener {
        fun onItemCheck(position: Int, checkBox: View)
    }
    fun setOnItemCheckListener(checked: OnItemCheckListener){
        mChecked = checked
    }

    /**Creamos la funcion del clickListener*/
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    /**En el ViewHolder le pasamos al constructor primario el listener del click y el check*/
    class FoodViewHolder(val binding: FoodCardBinding,
                         listener: OnItemClickListener,
                         checked: OnItemCheckListener)
        : RecyclerView.ViewHolder(binding.root){
        /**Inicializamos el click y el check*/
        init {
            binding.root.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                checked.onItemCheck(layoutPosition, binding.checkbox)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = FoodCardBinding.inflate(LayoutInflater.from(context /* If error, the right way is: parent.context*/), parent, false)
        return FoodViewHolder(binding, mListener, mChecked)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        with(holder){
            Picasso.get().load(food.image).into(binding.foodImage) //Image
            binding.foodName.text = food.name
            binding.foodDescription.text = food.description
            binding.foodPrice.text = food.price.toString()
            // Miss -> Rating (see documentation)
            if (food.check){
                //Si el objeto tiene esta condicion, el checkbox pasa a chequeado
                binding.checkbox.isChecked = true
            }
        }
    }

    override fun getItemCount(): Int {
        return foods.size
    }
}