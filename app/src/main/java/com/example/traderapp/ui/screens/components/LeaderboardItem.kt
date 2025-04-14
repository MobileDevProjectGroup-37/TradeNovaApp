import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
// --- For alignment, arrangement, spacing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// --- Colors (you can place them in a separate Theme file if you prefer)
import androidx.compose.ui.graphics.Color
// --- Our custom ViewModel & Data
import com.example.traderapp.data.model.UserData

@Composable
fun LeaderboardItem(
    rank: Int,
    userData: UserData
) {
    // Comments in English as requested.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1) Rank number
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(40.dp)
            )

            // 2) User main info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = userData.email, // or userData.username if you have it
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                // You could show something else here, e.g. "trade volume" or "balance"
            }

            // 3) Profit / ROI
            Text(
                text = "${userData.profit}%",
                style = MaterialTheme.typography.titleLarge,
                color = if (userData.profit >= 0.0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            )
        }
    }
}
