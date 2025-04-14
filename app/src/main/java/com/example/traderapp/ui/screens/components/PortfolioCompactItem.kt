package com.example.traderapp.ui.screens.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.traderapp.R


@Composable
fun PortfolioCompactItem(
    name: String,
    price: Double,
    changePercent: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.question_icon),
                contentDescription = "$name logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = (if (changePercent >= 0) "+" else "") +
                            String.format("%.2f", changePercent) + "%",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (changePercent >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
