package com.hvdevs.incomeexpensetracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.hvdevs.incomeexpensetracker.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


open class StatisticsFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val yData: ArrayList<Float> = arrayListOf(
        5f, 10f, 15f, 30f, 40f
    )
    val xData: ArrayList<String> = arrayListOf(
        "Sony", "Huawei", "LG", "Apple", "Samsung"
    )

    private var barArrayList: ArrayList<Entry> = arrayListOf()
    private var labels: ArrayList<String> = arrayListOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        val list: ArrayList<History> = arrayListOf(
            History("Netflix", "Pago mensual", "$1400"),
            History("YouTube", "Pago mensual", "$140"),
            History("Internet", "Pago mensual", "$4400"),
            History("Tarjeta", "Pago mensual", "$10400"),
            History("Prestamos", "Pago mensual", "$14400"),
            History("Prime", "Pago mensual", "$1300"),
            History("Telefono", "6 cuotas", "$2400")
        )

        binding.rv.layoutManager = LinearLayoutManager(context)
        val adapter = HistoryAdapter(list)
        binding.rv.adapter = adapter
        adapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                Toast.makeText(context, list[position].toString(), Toast.LENGTH_SHORT).show()
            }

        })

        getData()
        //Bar Chart
        val barDataSet = LineDataSet(barArrayList, "Meses 2022")
        val barData = LineData(barDataSet)
        binding.lChart.data = barData
        //Add many colors
        val colors: ArrayList<Int> = arrayListOf()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        barDataSet.color = Color.parseColor("#438883")
        //val color: ArrayList<Int> = arrayListOf(Color.BLACK)
        //barDataSet.setValueTextColors(color)
        barDataSet.valueTextColor = Color.parseColor("#666666")
        barDataSet.setDrawFilled(true)
        barDataSet.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.line_chart_gradient)
        binding.lChart.description.isEnabled = false
        val xAxis = binding.lChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lChart.axisRight.setDrawGridLines(false)
        binding.lChart.axisLeft.setDrawGridLines(false)
        binding.lChart.xAxis.setDrawGridLines(false)
        //Animacion
        binding.lChart.animateXY(1000, 1000, Easing.EaseInOutBounce, Easing.EaseInExpo)
        return binding.root
    }

    private fun getData(){
        barArrayList.add(BarEntry(0f, 10f))
        barArrayList.add(BarEntry(1f, 20f))
        barArrayList.add(BarEntry(2f, 30f))
        barArrayList.add(BarEntry(3f, 40f))
        barArrayList.add(BarEntry(4f, 50f))
    }

//    private fun addData() {
//        val yVals1: ArrayList<PieEntry> = arrayListOf()
//        for (i in 0 until   yData.size) yVals1.add(PieEntry(yData[i], i.toFloat()))
//
//        val xVals: ArrayList<String> = arrayListOf()
//        for (i in 0 until   xData.size) xVals.add(xData[i])
//
//        //Create pie data set
//        val dataSet = PieDataSet(yVals1, "Market share")
//        dataSet.sliceSpace = 3f
//        dataSet.selectionShift = 5f
//
//        //Add many colors
//        val colors: ArrayList<Int> = arrayListOf()
//
//        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//
//        colors.add(ColorTemplate.getHoloBlue())
//        dataSet.colors = colors
//
//        //Instantiate pie data object now
//        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
//        data.setValueTextSize(11f)
//        data.setValueTextColor(Color.GRAY)
//
//        binding.pieChart1.data = data
//
//        //Undo all highlights
//        binding.pieChart1.highlightValue(null)
//
//        //Update pie chart
//        binding.pieChart1.invalidate()
//
//        //Start
//
//    }

//    private fun generateCenterText(): SpannableString {
//        val s = SpannableString("Revenues\nQuarters 2015")
//        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
//        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
//        return s
//    }
//
//    /**
//     * generates less data (1 DataSet, 4 values)
//     * @return PieData
//     */
//    private fun generatePieData(): PieData {
//        val tf = Typeface.createFromAsset(context?.assets, "OpenSans-Regular.ttf");
//        val count = 4
//        val entries1: ArrayList<PieEntry> = ArrayList()
//        for (i in 0 until count) {
//            entries1.add(PieEntry((Math.random() * 60 + 40).toFloat(), "Quarter " + (i + 1)))
//        }
//        val ds1 = PieDataSet(entries1, "Quarterly Revenues 2015")
//        ds1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
//        ds1.sliceSpace = 2f
//        ds1.valueTextColor = Color.WHITE
//        ds1.valueTextSize = 12f
//        val d = PieData(ds1)
//        d.setValueTypeface(tf)
//        return d
//    }

//    //Documentacion: https://github.com/PhilJay/MPAndroidChart
//    //PieChart: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/fragments/PieChartFrag.java
//    //Tuto: https://www.youtube.com/watch?v=VfLop_oLYU0
//    //PieChart com.github.mikephil.charting.charts.PieChart
//    //Configure PieChart
//    binding.pieChart1.setUsePercentValues(true)
//    binding.pieChart1.contentDescription = "Smartphones Mark"
//
//    //Enable hole and configure
//    binding.pieChart1.isDrawHoleEnabled = true
//    binding.pieChart1.setHoleColor(Color.TRANSPARENT)
//    binding.pieChart1.holeRadius = 7f
//    binding.pieChart1.transparentCircleRadius = 10f
//
//    //Enable rotation of the chart by touch
//    binding.pieChart1.rotationAngle = 0f
//    binding.pieChart1.isRotationEnabled = true
//
//    //Set a chart value selected listener
//    binding.pieChart1.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
//        override fun onValueSelected(e: Entry?, h: Highlight?) {
//            //Display msg when value is selected
//            if(e == null) return
//            Toast.makeText(context, xData[e.x.toInt()] + " - " + e.data + " k ", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onNothingSelected() {
//
//        }
//
//    })

    //Add data
//    addData()

    //Customize legends
//    val l: Legend? = binding.pieChart1.legend
//    l?.position = Legend.LegendPosition.RIGHT_OF_CHART
//    l?.xEntrySpace = 7f
//    l?.yEntrySpace = 5f

//        chart = v.findViewById(R.id.pieChart1)
//        chart?.description?.isEnabled = false
//
//        val tf = Typeface.createFromAsset(requireContext().assets, "OpenSans-Light.ttf")
//
//        chart?.setCenterTextTypeface(tf)
//        chart?.centerText = generateCenterText()
//        chart?.setCenterTextSize(10f)
//        chart?.setCenterTextTypeface(tf)
//
//        // radius of the center hole in percent of maximum radius
//
//        // radius of the center hole in percent of maximum radius
//        chart?.holeRadius = 45f
//        chart?.transparentCircleRadius = 50f
//
//        val l: Legend? = chart?.legend
//        l?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l?.orientation = Legend.LegendOrientation.VERTICAL
//        l?.setDrawInside(false)
//
//        chart?.data = generatePieData()


}