package com.example.traderapp.ui.screens.market

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme

@Composable
fun MiniLineChart(prices: List<Double>) {
    if (prices.isEmpty()) return

    val chartColor = MaterialTheme.colorScheme.primary.toArgb()

    AndroidView(factory = { context ->
        LineChart(context).apply {
            val entries = prices.mapIndexed { index, price ->
                Entry(index.toFloat(), price.toFloat())
            }

            val dataSet = LineDataSet(entries, "").apply {
                color = chartColor
                setDrawValues(false)
                setDrawCircles(false)
                lineWidth = 1.5f
            }

            data = LineData(dataSet)

            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
        }
    },
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    )
}
