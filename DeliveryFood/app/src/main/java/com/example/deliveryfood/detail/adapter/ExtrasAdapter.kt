package com.example.deliveryfood.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryfood.databinding.ExtrasCardBinding
import com.example.deliveryfood.detail.model.VariationsExtras

class ExtrasAdapter (private var list: ArrayList<VariationsExtras>): RecyclerView.Adapter<ExtrasAdapter.ExtrasViewHolder>() {
    class ExtrasViewHolder(val binding: ExtrasCardBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtrasViewHolder {
        val binding = ExtrasCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExtrasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExtrasViewHolder, position: Int) {
        val binding = holder.binding
        binding.text.text = list[position].type
    }

    override fun getItemCount(): Int {
        return list.size
    }
}