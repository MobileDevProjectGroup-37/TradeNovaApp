

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*

// --- For alignment, arrangement, spacing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// --- Hilt ViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// --- Logging
import android.util.Log

// --- Colors (you can place them in a separate Theme file if you prefer)
import androidx.compose.ui.graphics.Color

// --- Our custom ViewModel & Data
import com.example.traderapp.data.model.UserData
import com.example.traderapp.viewmodel.LeaderboardViewModel
val UpholdGreen = Color(0xFF0EAD69)
val UpholdLightGreen = Color(0xFF7CDB8A)
val UpholdGrayBackground = Color(0xFFF5F5F5)
val UpholdWhite = Color(0xFFFFFFFF)
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
                color = UpholdGreen,
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
                color = if (userData.profit >= 0.0) UpholdGreen else Color.Red
            )
        }
    }
}
