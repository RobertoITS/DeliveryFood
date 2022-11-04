package com.hvdevs.incomeexpensetracker

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate


open class StatisticsFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var rLayout: RelativeLayout? = null
    var mChart: PieChart? = null
    private val yData: ArrayList<Float> = arrayListOf(
        5f, 10f, 15f, 30f, 40f
    )
    val xData: ArrayList<String> = arrayListOf(
        "Sony", "Huawei", "LG", "Apple", "Samsung"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_statistics, container, false)

        //Documentacion: https://github.com/PhilJay/MPAndroidChart
        //PieChart: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/fragments/PieChartFrag.java
        //Tuto: https://www.youtube.com/watch?v=VfLop_oLYU0

        rLayout = v.findViewById(R.id.rl)
        mChart = v.findViewById(R.id.pieChart1)
        //!Add PieChart in main layout
        //rLayout?.addView(mChart)
        //rLayout?.setBackgroundColor(Color.LTGRAY)

        //Configure PieChart
        mChart?.setUsePercentValues(true)
        mChart?.contentDescription = "Smartphones Mark"

        //Enable hole and configure
        mChart?.isDrawHoleEnabled = true
        mChart?.setHoleColor(Color.TRANSPARENT)
        mChart?.holeRadius = 7f
        mChart?.transparentCircleRadius = 10f

        //Enable rotation of the chart by touch
        mChart?.rotationAngle = 0f
        mChart?.isRotationEnabled = true

        //Set a chart value selected listener
        mChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //Display msg when value is selected
                if(e == null) return
                Toast.makeText(context, xData[e.x.toInt()] + " - " + e.data + " k ", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected() {

            }

        })

        //Add data
        addData()

        //Customize legends
        val l: Legend? = mChart?.legend
        l?.position = Legend.LegendPosition.RIGHT_OF_CHART
        l?.xEntrySpace = 7f
        l?.yEntrySpace = 5f

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

        return v
    }

    private fun addData() {
        val yVals1: ArrayList<PieEntry> = arrayListOf()
        for (i in 0 until   yData.size) yVals1.add(PieEntry(yData[i], i.toFloat()))

        val xVals: ArrayList<String> = arrayListOf()
        for (i in 0 until   xData.size) xVals.add(xData[i])

        //Create pie data set
        val dataSet = PieDataSet(yVals1, "Market share")
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

        mChart?.data = data

        //Undo all highlights
        mChart?.highlightValue(null)

        //Update pie chart
        mChart?.invalidate()

        //Start

    }

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

}