package com.example.deliveryfood.main.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.example.deliveryfood.detail.DetailFragment
import com.example.deliveryfood.R
import com.example.deliveryfood.R.*
import com.example.deliveryfood.main.adapter.CategoryAdapter
import com.example.deliveryfood.databinding.ActivityMainBinding
import com.example.deliveryfood.main.model.CategoryModel
import com.example.deliveryfood.main.model.FoodModel
import com.example.deliveryfood.main.model.Resource
import com.example.deliveryfood.main.mvvm.data.network.FoodRepoImplement
import com.example.deliveryfood.main.mvvm.domain.FoodUseCaseImplement
import com.example.deliveryfood.main.mvvm.presentation.viewmodel.FoodViewModel
import com.example.deliveryfood.main.mvvm.presentation.viewmodel.FoodViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityMainBinding
    //ViewModel
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            FoodViewModelFactory(
                FoodUseCaseImplement(FoodRepoImplement())
            )
        )[FoodViewModel::class.java]
    }
    //List
    private var listFood: ArrayList<FoodModel> = arrayListOf()
    private var categories: ArrayList<CategoryModel> = arrayListOf()

    //El menu del searchview
    private lateinit var searchMenu: Menu
    private lateinit var itemSearch: MenuItem

    //Controla la visibilidad del searchToolbar
    private var visibility: Boolean = false

    private var misAppbar2Expand = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWidget()

        getData()

        binding.appbarMain.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBasExpandControl(appBarLayout, verticalOffset)
        }



    }

    private fun initWidget() {
        addFragment()
        //Colocamos el Toolbar
        setSupportActionBar(binding.toolbarMain)
        //Inizializamos el serchView
        setSearchToolbar()
    }

    private fun addFragment() {
        val t = supportFragmentManager.beginTransaction()
        t.add(id.frag, DetailFragment()).commit()
    }

    private fun getData(){
        /**
         * Get the data from the database
         * Uses ViewModel
         * */
        if (categories.size == 0) {
            viewModel.fetchData.observe(this) { result ->
                when (result){
                    //Manage 3 states from the Resource class file
                    is Resource.Loading -> { /* When is bringing the data from de database */
                        binding.progress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> { /* When obtain the data */
                        binding.progress.visibility = View.GONE
                        ordenarLista(result.data)
                    }
                    is Resource.Failure -> { /* When failure to bring the data */
                        binding.progress.visibility = View.GONE
                        Toast.makeText(this, "??Error en la carga de datos!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initTabLayout() {
        for (category in categories) {
            binding.tabs.addTab(binding.tabs.newTab().setText(category.name))
        }
    }

    private fun initMediator() {
        TabbedListMediator(
            binding.foodRv,
            binding.tabs,
            categories.indices.toList(),
            true
        ).attach()
    }

    private fun ordenarLista(foodArrayList: ArrayList<FoodModel>) {
        var category = ""
        for (n in foodArrayList) {
            if (category == n.category) break //Retorna si la categoria ya se agrego
            else { //Sino, continuamos con la siguiente
                category = n.category.toString()
                val list: ArrayList<FoodModel> = arrayListOf()
                for (m in foodArrayList) {
                    if (m.category == category && !listFood.contains(m)) {
                        list.add(m)
                        listFood.add(m)
                    }
                }
                categories.add(CategoryModel(category, list))
            }
        }
        initTabLayout()
        updateListFood(categories)
    }

    //Actualiza la lista del recycler
    @SuppressLint("NotifyDataSetChanged")
    private fun updateListFood(listFood: ArrayList<CategoryModel>) {
        binding.foodRv.adapter = CategoryAdapter(this, listFood, this)
        initMediator()
    }

    /**
     * Para lograr que esto funcione, llamamos las vistas desde el layout
     * instanciado
     * */
    //Oculta el layout
    private fun unRevealLayout() {
        val layout = this.findViewById<FragmentContainerView>(id.frag)

        val appbarDetail = layout.findViewById<AppBarLayout>(R.id.appbarDetail)
        val detailScroll = layout.findViewById<NestedScrollView>(R.id.detailScroll)
        val quantity = layout.findViewById<EditText>(R.id.quantity)
        val price = layout.findViewById<TextView>(R.id.price)

        val othersRV = layout.findViewById<RecyclerView>(R.id.others)
        val extrasShimmer = layout.findViewById<ShimmerFrameLayout>(R.id.extrasShimmer)


        val viewPager2 = layout.findViewById<ViewPager2>(R.id.imageSliderViewPager)
        val imageShimmer = layout.findViewById<ShimmerFrameLayout>(R.id.imageShimmer)


        val variationsRV = layout.findViewById<RecyclerView>(R.id.variation_recycler)
        val variationsShimmer = layout.findViewById<ShimmerFrameLayout>(R.id.variation_shimmer)

        // get the right and bottom side lengths
        // of the reveal layout
        val x: Int = layout.right / 2
        val y: Int = layout.bottom / 2

        // here the starting radius of the reveal layout is its full width
        val startRadius: Int = kotlin.math.max(layout.width, layout.height)

        // and the end radius should be zero at this
        // point because the layout should be closed
        val endRadius = 0

        // create the instance of the ViewAnimationUtils
        // to initiate the circular reveal animation
        val anim = ViewAnimationUtils.createCircularReveal(
            layout, x, y,
            startRadius.toFloat(), endRadius.toFloat()
        )

        // now as soon as the animation is ending, the reveal
        // layout should also be closed
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                layout.visibility = View.GONE

                //resetea la posicion del appbar y el nestedscrollview
                appbarDetail.setExpanded(true)
                detailScroll.scrollTo(0, 0)

                DetailFragment().quantity = 1
                quantity.setText(DetailFragment().quantity.toString())

                price.text = ""

                //Limpiamos las listas
                DetailFragment().variationsList.clear()
                DetailFragment().extrasList.clear()
                DetailFragment().imagesList.clear()
                DetailFragment().updateVariations(DetailFragment().variationsList, variationsRV, variationsShimmer)
                DetailFragment().updateExtras(DetailFragment().extrasList, othersRV, extrasShimmer)
                DetailFragment().updateImages(DetailFragment().imagesList, viewPager2, imageShimmer)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })

        // start the closing animation
        anim.start()
    }

    /**-------------------------------------SEARCH VIEW---------------------------------------*/
    /**
     * Metodos de busqueda
     *      Inicializamos el searchView, sus animaciones
     *      Contiene los metodos de busqueda en las listas
     *      TODO Falta implementar las busquedas!
     * */
    //Inflamos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //Las opciones del click a los items del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Maneja la seleccion de items
        return when (item.itemId){
            id.action_status -> {
                Toast.makeText(this, "checkedList.size.toString()", Toast.LENGTH_SHORT).show()
                true
            }
            id.action_search -> {
                circleRevealAnimation(isShow = true)
                itemSearch.expandActionView()
                true
            }
            id.action_settings -> {
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //Inicializamos el searchView
    @SuppressLint("CutPasteId", "SoonBlockedPrivateApi")
    private fun initSearchView(){
        val searchView = searchMenu.findItem(id.action_filter_search).actionView as SearchView
        //Activa/desactiva submit button en el teclado
        searchView.isSubmitButtonEnabled = false
        //Cambia el boton de cerrar
        val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeButton.setImageResource(drawable.ic_close)
        //Configuramos colores de texto y pista
        val txtSearch = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = "Buscar.."
        txtSearch.setHintTextColor(Color.GRAY)
        txtSearch.setTextColor(Color.BLACK)
        //Colocamos el cursor
        val searchTextView =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.isAccessible = true
            mCursorDrawableRes[searchTextView] =
                drawable.search_cursor //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Metodos de busqueda - SearchView
        /**Este metodo se puede implementar en la clase MainActivity, pero lo vamos
         * a hacer desde el iniciador del searchView*/
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //if (query.isNotEmpty())//
                searchFood(query)
                searchView.clearFocus()
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                //if (newText.isNotEmpty())
                searchFood(newText)
                return false
            }
        })
    }
    //Pasamos los valores del searchView
    private fun setSearchToolbar() {
        binding.searchToolbar.inflateMenu(menu.menu_search)
        searchMenu = binding.searchToolbar.menu
        binding.searchToolbar.setNavigationOnClickListener {
            circleRevealAnimation(isShow = false)
        }
        itemSearch = searchMenu.findItem(id.action_filter_search)
        MenuItemCompat.setOnActionExpandListener(itemSearch, object : MenuItemCompat.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                //Hacer algo cuando expande
                //Se esconde el titulo
                binding.toolbarLayoutMain.isTitleEnabled = false
                return true
            }
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                circleRevealAnimation(isShow = false)
                //Se muestra el titulo
                binding.toolbarLayoutMain.isTitleEnabled = true
                return true
            }
        })
        initSearchView()
    }
    //Animacion del searchView
    private fun circleRevealAnimation(isShow: Boolean) {
        val view = binding.searchToolbar
        var width = view.width
        //Las medidas 48 son en dp (density-independent pixels)
        width -= 1 * 48 - 48 / 2
        //Las medidas 36 son en dp (density-independent pixels)
        width -= 36
        val cx = width
        val cy = view.height / 2
        val anim: Animator = if (isShow) ViewAnimationUtils.createCircularReveal(
            view, cx, cy, 0f, width.toFloat())
        else ViewAnimationUtils.createCircularReveal(view, cx, cy, width.toFloat(), 0f)
        anim.duration = 220.toLong()
        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            }
        })
        // make the view visible and start the animation
        if (isShow) {
            view.visibility = View.VISIBLE
            visibility = isShow
        }
        // start the animation
        anim.start()
    }
    //El Buscardor
    private fun searchFood(query: String) {
        var foodName = query
        if (foodName.isNotEmpty()) foodName =
            foodName.substring(0, 1).uppercase(Locale.getDefault()) + foodName.substring(1)
                .lowercase(Locale.getDefault())
        val results: ArrayList<CategoryModel> = ArrayList()
        for (category in categories){
            listFood = arrayListOf()
            for (food in category.listOfFood){
                if (food.name != null && food.name!!.contains(foodName)) {
                    listFood.add(food)
                }
            }
            results.add(CategoryModel(category.name, listFood))
        }
        updateListFood(results)
    }
    /**-------------------------------------SEARCH VIEW---------------------------------------*/

    @Deprecated("Ver posible cambio")
    override fun onBackPressed() {
        if (binding.frag.isVisible){
            unRevealLayout()
        } else {
            super.onBackPressed()
        }
    }

    //Control del expand del appBar
    private fun appBasExpandControl(
        appBarLayout: AppBarLayout,
        verticalOffset: Int
    ) {
        val scrollRange = appBarLayout.totalScrollRange
        val fraction = 1f * (scrollRange + verticalOffset) / scrollRange
        if (abs(verticalOffset) >= appBarLayout.totalScrollRange){

        }
        else {

        }

        if (fraction < 0.35 && misAppbar2Expand) {
            //Hide here
            misAppbar2Expand = false
            //Se cambia la escala de la vista
            binding.imageIcon.animate().scaleX(1f).scaleY(1f)
        }
        if (fraction > 0.27 && !misAppbar2Expand) {
            //Show here
            misAppbar2Expand = true
            binding.imageIcon.animate().scaleX(0f).scaleY(0f)
        }
    }
}