package com.hvdevs.incomeexpensetracker

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hvdevs.incomeexpensetracker.databinding.HistoryItemBinding

class HistoryAdapter(private val history: ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    
    private var rowIndex = -1

    interface OnItemClickListener{
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    //Usamos el view binding
    class HistoryViewHolder(
        val binding: HistoryItemBinding,
                            listener: OnItemClickListener
    ):
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener { listener.onClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, mListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        with(holder){
            // La imagen: binding.iv
            binding.item.text = history[position].name
            binding.detail.text = history[position].detail
            binding.cost.text = history[position].cost
        }

        holder.binding.root.setOnClickListener {
            rowIndex = holder.adapterPosition
            Common.currentItem = history[position]
            notifyDataSetChanged()
        }
        val params = holder.binding.cv.layoutParams as ViewGroup.MarginLayoutParams
        if (rowIndex == position){
            holder.binding.cv.setBackgroundColor(R.drawable.card_history_item)
            holder.binding.item.setTextColor(Color.parseColor("#C5C5C7"))
            params.setMargins(0, 12, 0, 12)
            holder.binding.cv.layoutParams = params
            holder.binding.cv.elevation = 12f
        }
        else {
            holder.binding.cv.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.binding.item.setTextColor(Color.parseColor("#000000"))
            params.setMargins(0, 8, 0, 8)
            holder.binding.cv.layoutParams = params
            holder.binding.cv.elevation = 0f
        }
    }

    override fun getItemCount(): Int {
        return history.size
    }

    class Common{
        companion object{
            lateinit var currentItem: History
        }
    }
}