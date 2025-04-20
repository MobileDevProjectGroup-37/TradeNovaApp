package com.example.traderapp.ui.screens.portfolio

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.traderapp.ui.screens.components.texts.SubTitle
import com.example.traderapp.viewmodel.BalancePoint
import com.example.traderapp.viewmodel.PortfolioViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("DefaultLocale")
@Composable
fun PortfolioBalanceSection(
    balance: Double,
    percentageChange: Double,
    isLoading: Boolean
) {
    val portfolioViewModel: PortfolioViewModel = viewModel()
    portfolioViewModel.trackBalance(balance)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubTitle(
            text = "Portfolio Balance",
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "$${String.format("%.2f", balance)}",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${String.format("%+.2f", percentageChange)}%",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val axisTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
        BalanceLineChart(
            balanceHistory = portfolioViewModel.balanceHistory,
            axisTextColor = axisTextColor
        )
    }
}

@Composable
fun BalanceLineChart(
    balanceHistory: List<BalancePoint>,
    axisTextColor: Int
) {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            val entries = balanceHistory.mapIndexed { index, point ->
                Entry(index.toFloat(), point.balance.toFloat())
            }

            val dataSet = LineDataSet(entries, "Balance History").apply {
                color = Color.GREEN
                setDrawValues(false)
                setDrawCircles(false)
                lineWidth = 3f
            }

            data = LineData(dataSet)

            description.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false

            val minY = entries.minOfOrNull { it.y } ?: 0f
            val maxY = entries.maxOfOrNull { it.y } ?: 0f

            axisLeft.textColor = axisTextColor
            axisLeft.textSize = 14f
            axisLeft.axisMinimum = minY - 2f
            axisLeft.axisMaximum = maxY + 2f

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(true)
                granularity = 1f
                textColor = axisTextColor
                textSize = 14f
                setLabelRotationAngle(0f)

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in balanceHistory.indices) {
                            val time = balanceHistory[index].timestamp
                            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                            formatter.format(Date(time))
                        } else ""
                    }
                }
            }

            isDragEnabled = true
            setScaleEnabled(false)
            setVisibleXRangeMaximum(6f)
            moveViewToX(entries.size.toFloat())

            invalidate()
        }
    },
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    )
}
