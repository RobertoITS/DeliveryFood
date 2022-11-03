package com.hvdevs.incomeexpensetracker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AccountListViewAdapter(
    context: Context,
    private val options: ArrayList<String>,
    private val images: ArrayList<Int>
    ): BaseAdapter() {

    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as (LayoutInflater)

    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): Any {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = layoutInflater.inflate(R.layout.custom_list_items, parent, false)
        val tv: TextView = rowView.findViewById(R.id.tv)
        tv.text = options[position]
        val iv: ImageView = rowView.findViewById(R.id.iv)
        iv.setImageResource(images[position])
        return rowView
    }

}