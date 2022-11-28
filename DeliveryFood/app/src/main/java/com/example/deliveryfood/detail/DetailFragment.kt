package com.example.deliveryfood.detail

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.deliveryfood.R
import com.example.deliveryfood.R.*
import com.example.deliveryfood.databinding.FragmentDetailBinding
import com.example.deliveryfood.detail.adapter.ExtrasAdapter
import com.example.deliveryfood.detail.adapter.SliderAdapter
import com.example.deliveryfood.detail.adapter.VariationsAdapter
import com.example.deliveryfood.detail.model.ImageSlider
import com.example.deliveryfood.detail.model.VariationsExtras
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.FirebaseFirestore

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Controla la rotacion del fabbutton
    var rotate = false
    //Controla la expancion del appbar de la pantalla de detalles
    var misAppbar2Expand = true
    //El viewPager2 (pasa las imagenes)
    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler()
    /**
     * @param: Listas para recolectar info:
     * */
    //Lista de variedades y su adaptador
    var variationsList: ArrayList<VariationsExtras> = arrayListOf()
    private lateinit var vAdapter: VariationsAdapter
    //Lista de imagenes y su adaptador
    var imagesList: ArrayList<ImageSlider> = arrayListOf()
    private lateinit var iAdapter: SliderAdapter
    //Lista de extras y su adaptador
    var extrasList: ArrayList<VariationsExtras> = arrayListOf()
    private lateinit var eAdapter: ExtrasAdapter

    //Manejo de las cantidades
    var quantity = 1
    private var price: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        quantityPicker()

        binding.toolbarDetail.setNavigationOnClickListener {
            unRevealLayoutFun()
        }

        //Rotamos el fab button
        binding.add.setOnClickListener {
            fabRotateAnim(it)
        }

        //Listener del cambio en el scroll del appbar
        //Lo que se hace aca es tomar el valor del rango de scroll del appbar y el desplazamiento vertical
        //del NestedScrollView, para esconder o mostrar las vistas
        binding.appbarDetail.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBasExpandControl(appBarLayout, verticalOffset)
        }

        return binding.root
    }

    //Control del expand del appBar
    private fun appBasExpandControl(
        appBarLayout: AppBarLayout,
        verticalOffset: Int
    ) {
        val scrollRange = appBarLayout.totalScrollRange
        val fraction = 1f * (scrollRange + verticalOffset) / scrollRange
        if (fraction < 0.35 && misAppbar2Expand) {
            //Hide here
            misAppbar2Expand = false
            //Se cambia la escala de la vista
            binding.buttonsPanel.animate().scaleX(0f).scaleY(0f)
        }
        if (fraction > 0.27 && !misAppbar2Expand) {
            //Show here
            misAppbar2Expand = true
            binding.buttonsPanel.animate().scaleX(1f).scaleY(1f)
        }
    }

    @SuppressLint("ResourceType")
    //Animacion de la rotacion del FabButton
    private fun fabRotateAnim(view: View) {
        val fabRotate = AnimationUtils.loadAnimation(context, animator.fab_rotate)
        val fabRotateOriginPosition = AnimationUtils.loadAnimation(context, animator.fab_rotate_origin_position)
        if (!rotate) {
            rotate = true
            view.startAnimation(fabRotate)
            view.background.setTint(Color.parseColor("#FF0000"))
            view.animate().scaleX(0.8f).scaleY(0.8f)
        }
        else {
            rotate = false
            view.startAnimation(fabRotateOriginPosition)
            view.background.setTint(Color.parseColor("#FF03DAC5"))
            view.animate().scaleX(1f).scaleY(1f)
        }
    }

    //Revela el segundo layout de detalles desde el centro
    private fun unRevealLayoutFun() {
        val mRevealLayout = requireActivity().findViewById<FragmentContainerView>(R.id.frag)

        // get the right and bottom side lengths
        // of the reveal layout
        val x: Int = mRevealLayout.right / 2
        val y: Int = mRevealLayout.bottom / 2

        // here the starting radius of the reveal layout is its full width
        val startRadius: Int = kotlin.math.max(mRevealLayout.width, mRevealLayout.height)

        // and the end radius should be zero at this
        // point because the layout should be closed
        val endRadius = 0

        // create the instance of the ViewAnimationUtils
        // to initiate the circular reveal animation
        val anim = ViewAnimationUtils.createCircularReveal(
            mRevealLayout, x, y,
            startRadius.toFloat(), endRadius.toFloat()
        )

        // now as soon as the animation is ending, the reveal
        // layout should also be closed
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                mRevealLayout.visibility = View.GONE

                //resetea la posicion del appbar y el nestedscrollview
                binding.appbarDetail.setExpanded(true)
                binding.detailScroll.scrollTo(0, 0)
                    quantity = 1
                    binding.quantity.setText(quantity.toString())
                    binding.price.text = ""

                    //Limpiamos las listas
                    variationsList.clear()
                    extrasList.clear()
                    imagesList.clear()
                    updateVariations(variationsList, binding.variationRecycler, binding.variationShimmer)
                    updateExtras(extrasList, binding.others, binding.extrasShimmer)
                    updateImages(imagesList, viewPager2, binding.imageShimmer)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })

        // start the closing animation
        anim.start()
    }

    //Funcion para manejar la cantidad de pedidos
    @SuppressLint("SetTextI18n")
    private fun quantityPicker() {
        //Colocamos por defecto 1 en el edittext
        binding.quantity.setText("1")
        //El contenido del edittext lo pasamos a INT
        quantity = Integer.parseInt(binding.quantity.text.toString())
        //Sumamos o restamos pedidos
        binding.less.setOnClickListener {
            if (quantity != 1)
                quantity -= 1
            binding.quantity.setText(quantity.toString())
            val total = quantity * price
            binding.price.text = "$$total"
        }
        binding.more.setOnClickListener {
            quantity += 1
            binding.quantity.setText(quantity.toString())
            val total = quantity * price
            binding.price.text = "$$total"
        }
    }

    /**------------------------------------SliderImage-----------------------------------------*/
    /**
     * Slider Image View
     *      Se crea desde que la vista (Fragment) es creado
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se tiene que colocar una vez que se crea la vista
        viewPager2 = binding.imageSliderViewPager

        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.25f
        }

        viewPager2.setPageTransformer(compositePageTransformer)
        //Hasta aca, ver las animaciones

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })
    }
    //Variable tipo Runnable para ejecutar el codigo siempre y cuando este activo
    private val sliderRunnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
    /**------------------------------------SliderImage-----------------------------------------*/

    /**------------------------------------DATABASE QUERY-----------------------------------------*/
    //Obtenemos las imagenes
    fun getImages(
        id: String,
        viewPager2: ViewPager2,
        imageShimmer: ShimmerFrameLayout
    ) {
        val db = FirebaseFirestore.getInstance()
        imagesList = arrayListOf()
        db.collection("food/${id}/images/").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty){
                Log.e("FIREBASE", "Lista vacía ${id}")
            } else {
                for (dc in snapshot){
                    val image = dc.toObject(ImageSlider::class.java)
                    imagesList.add(image)
                }
                updateImages(imagesList, viewPager2, imageShimmer)
            }
        }
    }
    //Actualizamos las listas de imagen
    @SuppressLint("NotifyDataSetChanged")
    fun updateImages(
        imagesList: ArrayList<ImageSlider>,
        viewPager2: ViewPager2,
        imageShimmer: ShimmerFrameLayout) {
        //El adaptador
        iAdapter = SliderAdapter(imagesList, viewPager2)
        viewPager2.adapter = iAdapter
        iAdapter.notifyDataSetChanged()

        //Paramos el shimmer
        imageShimmer.stopShimmer()
        imageShimmer.visibility = View.GONE
    }

    //Obtenemos los extras
    fun getExtras(
        id: String,
        rv: RecyclerView,shimmer:
        ShimmerFrameLayout) {
        val db = FirebaseFirestore.getInstance()
        extrasList = arrayListOf()
        db.collection("food/${id}/extras/").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty){
                Log.e("FIREBASE", "Lista vacía")
            } else {
                for (dc in snapshot) {
                    extrasList.add(dc.toObject(VariationsExtras::class.java))
                }
                updateExtras(extrasList, rv, shimmer)
            }
        }
    }
    //Actualizamos la lista de extras
    @SuppressLint("NotifyDataSetChanged")
    fun updateExtras(
        extrasList: ArrayList<VariationsExtras>,
        rv: RecyclerView,
        shimmer: ShimmerFrameLayout
    ) {
        //Adaptador
        eAdapter = ExtrasAdapter(extrasList)
        rv.adapter = eAdapter
        //Para evitar el error del gridlayout (lista en 0 valores), colocamos la condicion:
        if (extrasList.size == 0) {
            rv.layoutManager = LinearLayoutManager(context)
        } else {
            /**Usamos el gridlayoutmanager para agrandar el layout y que se ajuste a la pantalla
             * La cantidad de filas la define el tamaño de la lista, en modo horizontal*/
            GridLayoutManager(context, extrasList.size / 2, RecyclerView.VERTICAL, false).apply {
                rv.layoutManager = this
            }
        }
        eAdapter.notifyDataSetChanged()
        //Paramos el shimmer
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
    }

    //Obtenemos las variedades
    fun getVariations(
        id: String,
        rv: RecyclerView,
        shimmer: ShimmerFrameLayout
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection("food/${id}/variations/").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty){
                Log.e("FIREBASE", "Lista vacía")
            } else {
                for (dc in snapshot) {
                    variationsList.add(dc.toObject(VariationsExtras::class.java))
                }
                updateVariations(variationsList, rv, shimmer)
            }
        }
    }
    //Actualizamos la lista de variedades:
    @SuppressLint("NotifyDataSetChanged")
    fun updateVariations(
        variationsList: ArrayList<VariationsExtras>,
        rv: RecyclerView,
        shimmer: ShimmerFrameLayout
    ) {
        //El adaptador
        vAdapter = VariationsAdapter(variationsList)
        rv.adapter = vAdapter

        //Desactivamos el scroll
        rv.isNestedScrollingEnabled = false

        if (variationsList.isEmpty()){
            rv.layoutManager = LinearLayoutManager(context)
        } else {
            /**Usamos el gridlayoutmanager para agrandar el layout y que se ajuste a la pantalla
             * La cantidad de filas la define el tamaño de la lista, en modo horizontal*/
            GridLayoutManager(context, variationsList.size, RecyclerView.HORIZONTAL, false).apply {
                rv.layoutManager = this
            }
        }
        vAdapter.notifyDataSetChanged()

        //Paramos el shimmer
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE

        /**Aqui sobreescribimos la funcion creada en el adapter
         * usando interface:*/
        vAdapter.setOnSingleItemCheckListener(object : VariationsAdapter.OnItemSingleCheckListener{
            var lastChecked: RadioButton? = null
            var lastCheckedPos = 0
            @SuppressLint("SetTextI18n")
            override fun onItemSingleCheck(position: Int, radioButton: View) {
                //Obtenemos el radioButton actual
                val rb = radioButton as RadioButton
                //Pasamos el "tag" de ese rB, y dejamos constancia de la ultima
                //posicion de donde se hizo "click"
                val clickedPos = (rb.tag as Int).toInt()
                if (rb.isChecked) {
                    if (lastChecked != null) {
                        //Aqui, si se cumplen las condiciones, el ultimo tocado
                        //deja de esta checkeado
                        lastChecked!!.isChecked = false
                    }
                    //Actualizamos los datos
                    lastChecked = rb
                    lastCheckedPos = clickedPos
                } else lastChecked = null
                price = variationsList[position].price!!.toLong()
                //TODO Larga un NullPointerException!!
//                binding.price.text = "$${price * quantity}"
            }
        })
    }
    /**------------------------------------DATABASE QUERY-----------------------------------------*/
}