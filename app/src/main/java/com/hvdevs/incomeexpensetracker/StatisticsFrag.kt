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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.hvdevs.incomeexpensetracker.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


open class StatisticsFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private var lineArrayList: ArrayList<Entry> = arrayListOf()
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

        addPieChart(binding.chart)


        return binding.root
    }


    /**
     * @PieChart
     * */

    private val yData: ArrayList<Int> = arrayListOf(
        500, 400, 600, 200, 300, 400, 200, 500, 700, 800, 900, 1000
    )
    private val xData: ArrayList<String> = arrayListOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    private fun addPieChart(pieChart: PieChart){
        //Documentacion: https://github.com/PhilJay/MPAndroidChart
        //PieChart: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/fragments/PieChartFrag.java
        //Tuto: https://www.youtube.com/watch?v=VfLop_oLYU0
        //PieChart com.github.mikephil.charting.charts.PieChart
        //Configure PieChart
        pieChart.setUsePercentValues(true)
        pieChart.contentDescription = "Smartphones Mark"

        //Enable hole and configure
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.holeRadius = 7f
        pieChart.transparentCircleRadius = 10f

        //Enable rotation of the chart by touch
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true

        //Animation
        pieChart.animateXY(1000, 1000, Easing.EaseInOutBounce, Easing.EaseInExpo)

        //Set a chart value selected listener
        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                var index = 0
                //Display msg when value is selected
                if(e == null) return
                for (i in 0 until yData.size){
                    if (yData[i].toFloat() == e.y){
                        index = i
                    }
                }
                Toast.makeText(context, xData[index] + " / $" + yData[index] + " - " + e.data + "%", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected() {

            }

        })

        //Add data
        addData(pieChart)

        //Customize legends
        val l: Legend? = pieChart.legend
        l?.xEntrySpace = 7f
        l?.yEntrySpace = 5f

    }

    private fun addData(pieChart: PieChart) {
        val yValues: ArrayList<PieEntry> = arrayListOf()
        for (i in 0 until   yData.size) yValues.add(PieEntry(yData[i].toFloat(), i.toFloat()))

        //Create pie data set
        val dataSet = PieDataSet(yValues, "Market share")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        //Add many colors
        val colors: ArrayList<Int> = arrayListOf()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        //Instantiate pie data object now
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.GRAY)

        pieChart.data = data

        //Undo all highlights
        pieChart.highlightValue(null)

        //Update pie chart
        pieChart.invalidate()

        //Start

    }


    /**
     * @BarChart
     * */
    private fun addLineChar(lineChart: LineChart){
        getData()
        //Line Chart
        val lineDataSet = LineDataSet(lineArrayList, "Meses 2022")
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        //Add many colors
        val colors: ArrayList<Int> = arrayListOf()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        lineDataSet.color = Color.parseColor("#438883")

        lineDataSet.valueTextColor = Color.parseColor("#666666")

        //Fill data campus
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.line_chart_gradient)


        lineChart.description.isEnabled = false
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)

        //Animacion
        lineChart.animateXY(1000, 1000, Easing.EaseInOutBounce, Easing.EaseInExpo)
    }

    private fun getData(){
        lineArrayList.add(BarEntry(0f, 10f))
        lineArrayList.add(BarEntry(1f, 20f))
        lineArrayList.add(BarEntry(2f, 30f))
        lineArrayList.add(BarEntry(3f, 40f))
        lineArrayList.add(BarEntry(4f, 50f))
    }

}