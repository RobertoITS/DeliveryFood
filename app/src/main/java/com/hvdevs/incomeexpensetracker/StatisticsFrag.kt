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
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


open class StatisticsFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var chart: PieChart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_statistics, container, false)

        //Documentacion: https://github.com/PhilJay/MPAndroidChart
        //PieChart: https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/fragments/PieChartFrag.java
        //Tuto: https://www.youtube.com/watch?v=VfLop_oLYU0

        chart = v.findViewById(R.id.pieChart1)
        chart?.description?.isEnabled = false

        val tf = Typeface.createFromAsset(requireContext().assets, "OpenSans-Light.ttf")

        chart?.setCenterTextTypeface(tf)
        chart?.centerText = generateCenterText()
        chart?.setCenterTextSize(10f)
        chart?.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        chart?.holeRadius = 45f
        chart?.transparentCircleRadius = 50f

        val l: Legend? = chart?.legend
        l?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l?.orientation = Legend.LegendOrientation.VERTICAL
        l?.setDrawInside(false)

        chart?.data = generatePieData()

        return v
    }

    private fun generateCenterText(): SpannableString {
        val s = SpannableString("Revenues\nQuarters 2015")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

    /**
     * generates less data (1 DataSet, 4 values)
     * @return PieData
     */
    private fun generatePieData(): PieData {
        val tf = Typeface.createFromAsset(context?.assets, "OpenSans-Regular.ttf");
        val count = 4
        val entries1: ArrayList<PieEntry> = ArrayList()
        for (i in 0 until count) {
            entries1.add(PieEntry((Math.random() * 60 + 40).toFloat(), "Quarter " + (i + 1)))
        }
        val ds1 = PieDataSet(entries1, "Quarterly Revenues 2015")
        ds1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f
        val d = PieData(ds1)
        d.setValueTypeface(tf)
        return d
    }

}