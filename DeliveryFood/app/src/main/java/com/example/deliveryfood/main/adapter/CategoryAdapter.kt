package com.example.deliveryfood.main.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.deliveryfood.R
import com.example.deliveryfood.databinding.FoodCategoryBinding
import com.example.deliveryfood.detail.DetailFragment
import com.example.deliveryfood.main.model.CategoryModel
import com.example.deliveryfood.main.model.FoodModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class CategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryModel>,
    private val activity: Activity //Pasamos la actividad para obtener el Layout
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private lateinit var mAdapter: FoodAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = FoodCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        with(holder){
            binding.categoryName.text = category.name
            mAdapter = FoodAdapter(binding.root.context, category.listOfFood)
            binding.recyclerView.adapter = mAdapter
            mAdapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    Toast.makeText(context, "Ando...", Toast.LENGTH_SHORT).show()
                    val view = activity.findViewById<FragmentContainerView>(R.id.frag)
                    revealLayout(view, category.listOfFood[position])
                }
            })
            mAdapter.setOnItemCheckListener(object : FoodAdapter.OnItemCheckListener{
                override fun onItemCheck(position: Int, checkBox: View) {
                    Toast.makeText(context, "ando tambien", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(val binding: FoodCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    //Revela el segundo layout de detalles desde el centro
    private fun revealLayout(view: View, food: FoodModel) {

        Log.d("FIREBASE", food.toString())
        // get the right and bottom side
        // lengths of the reveal layout
        val x: Int = view.right / 2
        val y: Int = view.bottom / 2

        // here the starting radius of the reveal
        // layout is 0 when it is not visible
        val startRadius = 0

        // make the end radius should
        // match the while parent view
        val endRadius = kotlin.math.hypot(
            view.width.toDouble(),
            view.height.toDouble()
        ).toInt()

        // create the instance of the ViewAnimationUtils to
        // initiate the circular reveal animation
        val anim = ViewAnimationUtils.createCircularReveal(
            view, x, y,
            startRadius.toFloat(), endRadius.toFloat()
        )

        // make the invisible reveal layout to visible
        // so that upon revealing it can be visible to user
        view.visibility = View.VISIBLE
        // now start the reveal animation
        anim.start()
        //Instanciamos todos los elementos que usan las listas (por el binding no se puede)
        val othersRV = activity.findViewById<RecyclerView>(R.id.others)
        val extrasShimmer = activity.findViewById<ShimmerFrameLayout>(R.id.extrasShimmer)
        DetailFragment().getExtras(food.id, othersRV, extrasShimmer)

        val viewPager2 = activity.findViewById<ViewPager2>(R.id.imageSliderViewPager)
        val imageShimmer = activity.findViewById<ShimmerFrameLayout>(R.id.imageShimmer)
        DetailFragment().getImages(food.id, viewPager2,imageShimmer)

        val variationsRV = activity.findViewById<RecyclerView>(R.id.variation_recycler)
        val variationsShimmer = activity.findViewById<ShimmerFrameLayout>(R.id.variation_shimmer)
        DetailFragment().getVariations(food.id, variationsRV, variationsShimmer)

        val cTD = activity.findViewById<FragmentContainerView>(R.id.frag)
        cTD.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarDetail).title = food.name
    }
}